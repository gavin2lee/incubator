#!/bin/bash

# 安装报警
# liyang@yihecloud.com
# 2016-07-25

OPTS=$(getopt -o h:P:u:p: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

mysql_host='127.0.0.1'
mysql_port="3306"
mysql_user="root"
mysql_pass=""

while true; do
  case "$1" in
  -h) mysql_host=$2; shift 2;;
  -P) mysql_port=$2; shift 2;;
  -u) mysql_user=$2; shift 2;;
  -p) mysql_pass=$2; shift 2;;
  --) shift; break;;
  esac
done

function check_opt() {
  arg="\$$1"
  if [ "$(eval echo $arg)"  = "" ]; then
    echo "[ERROR] <$1> 参数缺失！"
    usage;
    exit 1
  fi
}

function usage() {
  echo "
Usage: $0 
  -h <mysql host> 
  -u <mysql username> 
  -p <mysql password> 
  -P <mysql port>, default: 3306
"
}

# check options
check_opt "mysql_host"
check_opt "mysql_user"
check_opt "mysql_pass"


#初始化
# 获取本机IP
IP=
host_ips=(`ip addr show | grep inet | grep -v inet6 | grep brd | awk '{print $2}' | cut -f1 -d '/'`)
if [ "${host_ips[0]}" == "" ]; then
  echo "[ERROR] get ip address error"
  exit 1
else
  IP=${host_ips[0]}
  echo "[INFO] use host ip address: $IP"
fi

access_ip=$IP
mysql_conn=$mysql_host:$mysql_port

#安装redis
yum -y install redis
#安装mysql-server,mysql-client
yum -y install mariadb-server mariadb mysql-devel
#安装python, gcc
yum -y install python-virtualenv gcc

# import sql
systemctl start redis
systemctl start mariadb.service
mysql -h $mysql_host -u $mysql_user --password=$mysql_pass < sql/graph-db-schema.sql
mysql -h $mysql_host -u $mysql_user --password=$mysql_pass < sql/dashboard-db-schema.sql
mysql -h $mysql_host -u $mysql_user --password=$mysql_pass < sql/falcon-portal-db-schema.sql

# Install service
export falcon_path=`pwd`
cd $falcon_path
mkdir ./tmp
tar -zxf open-falcon-latest.tar.gz -C ./tmp/
for x in `find ./tmp/ -name "*.tar.gz"`;do \
    app=`echo $x|cut -d '-' -f2`; \
    mkdir -p $app; \
    tar -zxf $x -C $app; \
done

# fe
cat <<EOF >$falcon_path/fe/cfg.json
{
    "log": "debug",
    "company": "MI",
    "http": {
        "enabled": true,
        "listen": "0.0.0.0:1234"
    },
    "cache": {
        "enabled": true,
        "redis": "127.0.0.1:6379",
        "idle": 10,
        "max": 1000,
        "timeout": {
            "conn": 10000,
            "read": 5000,
            "write": 5000
        }
    },
    "salt": "",
    "canRegister": true,
    "ldap": {
        "enabled": false,
        "addr": "ldap.example.com:389",
        "baseDN": "dc=example,dc=com",
        "bindDN": "cn=mananger,dc=example,dc=com",
        "bindPasswd": "12345678",
        "userField": "uid",
        "attributes": ["sn","mail","telephoneNumber"]
    },
    "uic": {
        "addr": "$mysql_user:$mysql_pass@tcp($mysql_conn)/falcon_portal?charset=utf8&loc=Asia%2FChongqing",
        "idle": 10,
        "max": 100
    },
    "shortcut": {
        "falconPortal": "http://$access_ip:5050/",
        "falconDashboard": "http://$access_ip:8081/",
        "falconAlarm": "http://$access_ip:9912/"
    }
}
EOF

# portal
cd $falcon_path/portal/
virtualenv ./env
./env/bin/pip install ../python-lib/itsdangerous-0.24.tar.gz
./env/bin/pip install ../python-lib/MarkupSafe-0.23.tar.gz
./env/bin/pip install ../python-lib/Jinja2-2.7.2.tar.gz
./env/bin/pip install ../python-lib/Werkzeug-0.9.4.tar.gz
./env/bin/pip install ../python-lib/Werkzeug-0.11.10.tar.gz
./env/bin/pip install ../python-lib/gunicorn-18.0.tar.gz
./env/bin/pip install ../python-lib/six-1.10.0.tar.gz
./env/bin/pip install ../python-lib/python-dateutil-2.2.tar.gz
./env/bin/pip install ../python-lib/requests-2.3.0.tar.gz
./env/bin/pip install ../python-lib/MySQL-python-1.2.5.zip
./env/bin/pip install ../python-lib/gunicorn-19.3.0.tar.gz
./env/bin/pip install ../python-lib/Flask-0.10.1.tar.gz

cat <<EOF >$falcon_path/portal/frame/config.py
# -*- coding:utf-8 -*-
__author__ = 'Ulric Qin'

# -- app config --
DEBUG = True

# -- db config --
DB_HOST = "$mysql_host"
DB_PORT = $mysql_port
DB_USER = "$mysql_user"
DB_PASS = "$mysql_pass"
DB_NAME = "falcon_portal"

# -- cookie config --
SECRET_KEY = "4e.5tyg8-u9ioj"
SESSION_COOKIE_NAME = "falcon-portal"
PERMANENT_SESSION_LIFETIME = 3600 * 24 * 30

UIC_ADDRESS = {
    'internal': 'http://127.0.0.1:1234',
    'external': 'http://$access_ip:1234',
}

UIC_TOKEN = ''

MAINTAINERS = ['$mysql_user']
CONTACT = 'ulric.qin@gmail.com'

COMMUNITY = True

try:
    from frame.local_config import *
except Exception, e:
    print "[warning] %s" % e
EOF

# hbs
cat <<EOF >$falcon_path/hbs/cfg.json
{
    "debug": true,
    "database": "$mysql_user:$mysql_pass@tcp($mysql_conn)/falcon_portal?loc=Local&parseTime=true",
    "hosts": "",
    "maxIdle": 100,
    "listen": ":6030",
    "trustable": [""],
    "http": {
        "enabled": true,
        "listen": "0.0.0.0:6031"
    }
}
EOF

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
        "enabled": true,
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

# agent
cat <<EOF >$falcon_path/agent/cfg.json
{
    "debug": true,
    "hostname": "",
    "ip": "",
    "plugin": {
        "enabled": false,
        "dir": "./plugin",
        "git": "https://github.com/open-falcon/plugin.git",
        "logs": "./logs"
    },
    "heartbeat": {
        "enabled": true,
        "addr": "127.0.0.1:6030",
        "interval": 60,
        "timeout": 1000
    },
    "transfer": {
        "enabled": true,
        "addrs": [
            "127.0.0.1:8433",
            "127.0.0.1:8433"
        ],
        "interval": 60,
        "timeout": 1000
    },
    "http": {
        "enabled": true,
        "listen": ":1988",
        "backdoor": false
    },
    "collector": {
        "ifacePrefix": ["eth", "em"]
    },
    "ignore": {
        "cpu.busy": true,
        "df.bytes.free": true,
        "df.bytes.total": true,
        "df.bytes.used": true,
        "df.bytes.used.percent": true,
        "df.inodes.total": true,
        "df.inodes.free": true,
        "df.inodes.used": true,
        "df.inodes.used.percent": true,
        "mem.memtotal": true,
        "mem.memused": true,
        "mem.memused.percent": true,
        "mem.memfree": true,
        "mem.swaptotal": true,
        "mem.swapused": true,
        "mem.swapfree": true
    }
}
EOF

# judge
cat <<EOF >$falcon_path/judge/cfg.json
{
    "debug": true,
    "debugHost": "nil",
    "remain": 11, 
    "http": {
        "enabled": true,
        "listen": "0.0.0.0:6081"
    },
    "rpc": {
        "enabled": true,
        "listen": "0.0.0.0:6080"
    },
    "hbs": {
        "servers": ["127.0.0.1:6030"],
        "timeout": 300,
        "interval": 60
    },
    "alarm": {
        "enabled": true,
        "minInterval": 300,
        "queuePattern": "event:p%v",
        "redis": {
            "dsn": "127.0.0.1:6379",
            "maxIdle": 5,
            "connTimeout": 5000,
            "readTimeout": 5000,
            "writeTimeout": 5000
        }
    }
}
EOF

# alarm
cat <<EOF >$falcon_path/alarm/cfg.json
{
    "debug": true,
    "uicToken": "",
    "http": {
        "enabled": true,
        "listen": "0.0.0.0:9912"
    },
    "queue": {
        "sms": "/sms",
        "mail": "/mail"
    },
    "redis": {
        "addr": "127.0.0.1:6379",
        "maxIdle": 5,
        "highQueues": [
            "event:p0",
            "event:p1",
            "event:p2",
            "event:p3",
            "event:p4",
            "event:p5"
        ],
        "lowQueues": [
            "event:p6"
        ],
        "userSmsQueue": "/queue/user/sms",
        "userMailQueue": "/queue/user/mail"
    },
    "api": {
        "portal": "http://127.0.0.1:5050",
        "uic": "http://127.0.0.1:1234",
        "links": "http://link.example.com"
    }
}
EOF

# graph
cat <<EOF >$falcon_path/graph/cfg.json
{
	"debug": false,
	"http": {
		"enabled": true,
		"listen": "0.0.0.0:6071"
	},
	"rpc": {
		"enabled": true,
		"listen": "0.0.0.0:6070"
	},
	"rrd": {
		"storage": "/opt/data/6070"
	},
	"db": {
		"dsn": "$mysql_user:$mysql_pass@tcp($mysql_conn)/graph?loc=Local&parseTime=true",
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

# dashboard
cd $falcon_path/dashboard/
virtualenv ./env
./env/bin/pip install ../python-lib/itsdangerous-0.24.tar.gz
./env/bin/pip install ../python-lib/MarkupSafe-0.23.tar.gz
./env/bin/pip install ../python-lib/Jinja2-2.7.2.tar.gz
./env/bin/pip install ../python-lib/Werkzeug-0.9.4.tar.gz
./env/bin/pip install ../python-lib/Werkzeug-0.11.10.tar.gz
./env/bin/pip install ../python-lib/gunicorn-18.0.tar.gz
./env/bin/pip install ../python-lib/six-1.10.0.tar.gz
./env/bin/pip install ../python-lib/python-dateutil-2.2.tar.gz
./env/bin/pip install ../python-lib/requests-2.3.0.tar.gz
./env/bin/pip install ../python-lib/MySQL-python-1.2.5.zip
./env/bin/pip install ../python-lib/gunicorn-19.3.0.tar.gz
./env/bin/pip install ../python-lib/Flask-0.10.1.tar.gz

cat <<EOF >$falcon_path/dashboard/rdd/config.py
#-*-coding:utf8-*-
import os

#-- dashboard db config --
DASHBOARD_DB_HOST = "$mysql_host"
DASHBOARD_DB_PORT = $mysql_port
DASHBOARD_DB_USER = "$mysql_user"
DASHBOARD_DB_PASSWD = "$mysql_pass"
DASHBOARD_DB_NAME = "dashboard"

#-- graph db config --
GRAPH_DB_HOST = "$mysql_host"
GRAPH_DB_PORT = $mysql_port
GRAPH_DB_USER = "$mysql_user"
GRAPH_DB_PASSWD = "$mysql_pass"
GRAPH_DB_NAME = "graph"

#-- app config --
DEBUG = True
SECRET_KEY = "secret-key"
SESSION_COOKIE_NAME = "open-falcon"
PERMANENT_SESSION_LIFETIME = 3600 * 24 * 30
SITE_COOKIE = "open-falcon-ck"

#-- query config --
QUERY_ADDR = "http://127.0.0.1:9966"

BASE_DIR = "/opt/install_alarm/dashboard/"
LOG_PATH = os.path.join(BASE_DIR,"log/")

try:
    from rrd.local_config import *
except:
    pass
EOF

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
    "storage": "/opt/install-alarm/data/graph/6070"
  },
  "db": {
    "dsn": "$mysql_user:$db_pwd@tcp($mysql_host:$mysql_port)/graph?loc=Local&parseTime=true",
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

# links
cd $falcon_path/links/
virtualenv ./env
./env/bin/pip install ../python-lib/itsdangerous-0.24.tar.gz
./env/bin/pip install ../python-lib/MarkupSafe-0.23.tar.gz
./env/bin/pip install ../python-lib/Jinja2-2.7.2.tar.gz
./env/bin/pip install ../python-lib/Werkzeug-0.9.4.tar.gz
./env/bin/pip install ../python-lib/Werkzeug-0.11.10.tar.gz
./env/bin/pip install ../python-lib/gunicorn-18.0.tar.gz
./env/bin/pip install ../python-lib/six-1.10.0.tar.gz
./env/bin/pip install ../python-lib/python-dateutil-2.2.tar.gz
./env/bin/pip install ../python-lib/requests-2.3.0.tar.gz
./env/bin/pip install ../python-lib/MySQL-python-1.2.5.zip
./env/bin/pip install ../python-lib/gunicorn-19.3.0.tar.gz
./env/bin/pip install ../python-lib/Flask-0.10.1.tar.gz

cat <<EOF >$falcon_path/links/frame/config.py
# -*- coding:utf-8 -*-
__author__ = 'Ulric Qin'

# -- app config --
DEBUG = True

# -- db config --
DB_HOST = "$mysql_host"
DB_PORT = $mysql_port
DB_USER = "$mysql_user"
DB_PASS = "$mysql_pass"
DB_NAME = "falcon_portal"

# -- cookie config --
SECRET_KEY = "4e.5tyg8-u9ioj"
SESSION_COOKIE_NAME = "falcon-links"
PERMANENT_SESSION_LIFETIME = 3600 * 24 * 30

try:
    from frame.local_config import *
except Exception, e:
    print "[warning] %s" % e
EOF

$falcon_path/fe/control restart
$falcon_path/portal/control restart
$falcon_path/judge/control restart
$falcon_path/alarm/control restart
$falcon_path/transfer/control restart
$falcon_path/hbs/control restart
$falcon_path/agent/control restart
$falcon_path/graph/control restart
$falcon_path/query/control restart
$falcon_path/dashboard/control restart
$falcon_path/links/control restart

#check health
#fe
curl -s 127.0.0.1:1234/health
#agent
curl -s 127.0.0.1:1988/health
#hbs
curl -s 127.0.0.1:6031/health
#transfer
curl -s 127.0.0.1:6060/health
#sender
curl -s 127.0.0.1:6066/health
#graph
curl -s 127.0.0.1:6071/health
#judge
curl -s 127.0.0.1:6081/health
#alarm
curl -s 127.0.0.1:9912/health
#query
curl -s 127.0.0.1:9966/health