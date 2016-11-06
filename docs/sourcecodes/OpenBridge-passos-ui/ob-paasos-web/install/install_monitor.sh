#!/bin/bash

# 安装监控
# wangxinxiang@yihecloud.com
# 2016-05-16

# example
# sudo sh install_monitor.sh --db_host=localhost --db_port=3306 --db_user=root --db_pwd=xxxxxx --docker_hub=xxx.xxx.xxx:5000
echo "请使用以下安装命令"
echo "sudo sh install_monitor.sh --db_host=localhost --db_port=3306 --db_user=root --db_pwd=xxxxxx --insecure_registry=xxx.xxx.xxx:5000"
echo "=================安装日志===================="

OPTS=$(getopt -o : --long db_host:,db_port:,db_user:,db_pwd:,insecure_registry: -- "$@" )
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  exit 1
fi

eval set -- "$OPTS"
db_host="localhost"
db_port="3306"
db_user="root"
db_pwd=""
insecure_registry="127.0.0.1:5000"

while true; do
  case "$1" in
  --db_host) db_host=$2; shift 2;;
  --db_port) db_port=$2; shift 2;;
  --db_user) db_user=$2; shift 2;;
  --db_pwd) db_pwd=$2; shift 2;;
  --insecure_registry) insecure_registry=$2; shift 2;;
  --) shift; break;;
  esac
done

# init
source ./env_init.sh

#安装mysql-client
if command_exists mysql; then
  echo "mysql client exist!"
else
  yum -y install mariadb  
fi

#安装docker
if [ $insecure_registry == "127.0.0.1:5000" ]; then
  source $dest_path_script/docker.sh --insecure_registry=$insecure_registry
fi

# 导入sql
mysql -h $db_host -P $db_port -u $db_user --password="$db_pwd" < sql/dashboard-db-schema.sql
mysql -h $db_host -P $db_port -u $db_user --password="$db_pwd" < sql/graph-db-schema.sql

echo "清理监控组件..."
docker rm -f dashboard
docker rm -f agent

echo "安装监控组件..."
# dashboard
docker run -d --restart=always -p 8081:8081 \
  -e DB_HOST=$db_host \
  -e DB_PORT=$db_port \
  -e DB_USER=$db_user \
  -e DB_PWD=$db_pwd \
  -e QUERY_ADDR=$host_ip \
  --name dashboard \
  $insecure_registry/dashboard:1.0

# agent
sudo docker run -d --restart=always \
  -e HOSTNAME="\"$host_ip\"" \
  -e TRANSFER_ADDR="[\"$host_ip:8433\",\"$host_ip:8433\"]" \
  -e TRANSFER_INTERVAL="60" \
  -v /:/rootfs:ro \
  -v /var/run:/var/run:rw \
  -v /sys:/sys:ro \
  -v /var/lib/docker/:/var/lib/docker:ro \
  -p 1988:1988 \
  --name agent \
  $insecure_registry/agent:1.9

# Install service
tar -zvxf open-falcon.tar.gz -C /opt
falcon_path="/opt/open-falcon"

# graph
cat <<EOF >$falcon_path/graph/cfg.json
{
  "debug": true,
  "http": {
    "enabled": true,
    "listen": "0.0.0.0:6071"
  },
  "rpc": {
    "enabled": true,
    "listen": "0.0.0.0:6070"
  },
  "rrd": {
    "storage": "/data/open-falcon/graph/6070"
  },
  "db": {
    "dsn": "$db_user:$db_pwd@tcp($db_host:$db_port)/graph?loc=Local&parseTime=true",
    "maxIdle": 4
  },
  "callTimeout": 5000,
  "migrate": {
    "enabled": false,
    "concurrency": 2,
    "replicas": 500,
    "cluster": {
      "graph-00" : "127.0.0.1:6070"
    }
  }
}
EOF
echo "[Unit]
Description=flacon graph initialization
[Service]
WorkingDirectory=$falcon_path/graph
ExecStart=/bin/bash -c '$falcon_path/graph/falcon-graph -c $falcon_path/graph/cfg.json'
Restart=on-failure

[Install]
WantedBy=multi-user.target
" > /lib/systemd/system/falcon-graph.service

# query
cat <<EOF >$falcon_path/query/cfg.json
{
    "debug": "true",
    "http": {
        "enabled":  true,
        "listen":   "0.0.0.0:9966"
    },
    "graph": {
        "connTimeout": 1000,
        "callTimeout": 5000,
        "maxConns": 32,
        "maxIdle": 32,
        "replicas": 500,
        "cluster": {
            "graph-00": "127.0.0.1:6070"
        }
    },
    "api": {
        "query": "http://127.0.0.1:9966",
        "dashboard": "http://127.0.0.1:8081",
        "max": 500
    }
}
EOF
echo "[Unit]
Description=flacon query initialization
[Service]
WorkingDirectory=$falcon_path/query
ExecStart=/bin/bash -c '$falcon_path/query/falcon-query -c $falcon_path/query/cfg.json'
Restart=on-failure

[Install]
WantedBy=multi-user.target
" > /lib/systemd/system/falcon-query.service

# transfer
cat <<EOF >$falcon_path/transfer/cfg.json
{
    "debug": true,
    "minStep": 30,
    "http": {
        "enabled": true,
        "listen": "0.0.0.0:6060"
    },
    "rpc": {
        "enabled": true,
        "listen": "0.0.0.0:8433"
    },
    "socket": {
        "enabled": true,
        "listen": "0.0.0.0:4444",
        "timeout": 3600
    },
    "judge": {
        "enabled": false,
        "batch": 200,
        "connTimeout": 1000,
        "callTimeout": 5000,
        "maxConns": 32,
        "maxIdle": 32,
        "replicas": 500,
        "cluster": {
            "judge-00" : "127.0.0.1:6080"
        }
    },
    "graph": {
        "enabled": true,
        "batch": 200,
        "connTimeout": 1000,
        "callTimeout": 5000,
        "maxConns": 32,
        "maxIdle": 32,
        "replicas": 500,
        "cluster": {
            "graph-00" : "127.0.0.1:6070"
        }
    },
    "tsdb": {
        "enabled": false,
        "batch": 200,
        "connTimeout": 1000,
        "callTimeout": 5000,
        "maxConns": 32,
        "maxIdle": 32,
        "retry": 3,
        "address": "127.0.0.1:8088"
    }
}
EOF
echo "[Unit]
Description=flacon transfer initialization
[Service]
WorkingDirectory=$falcon_path/transfer
ExecStart=/bin/bash -c '$falcon_path/transfer/falcon-transfer -c $falcon_path/transfer/cfg.json'
Restart=on-failure

[Install]
WantedBy=multi-user.target
" > /lib/systemd/system/falcon-transfer.service

systemctl daemon-reload
systemctl start falcon-graph
systemctl start falcon-query
systemctl start falcon-transfer
systemctl status falcon-graph
systemctl status falcon-query
systemctl status falcon-transfer
