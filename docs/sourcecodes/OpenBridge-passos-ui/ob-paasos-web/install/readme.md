### 脚本说明

#### 1. 打包发布
>
./pack.sh


#### 2. 安装节点监控

##### 导入agent镜像
在镜像仓库安装脚本images目录，执行
>
docker load < agent.tar
docker push 127.0.0.1:5000/agent:1.9

##### 启动agent监控
在所有节点执行以下命令
> 
docker run -d --restart=always -e HOSTNAME="\"<本机IP>\"" \
	-e TRANSFER_ADDR="[\"<监控服务器IP>:8433\",\"<监控服务器IP>:8433\"]" \
	-e TRANSFER_INTERVAL="30" \
	-v /:/rootfs:ro \
	-v /var/run:/var/run:rw \
	-v /sys:/sys:ro \
	-v /var/lib/docker/:/var/lib/docker:ro \
	-p 1988:1988 \
	--name agent \
	"<docker仓储:5000>/agent:1.9"

如果存在大量节点需要监控，可以使用脚本自动获取本机IP
>
ip a|egrep 'inet.*brd'|awk '{print $2}'|cut -f1 -d '/'
