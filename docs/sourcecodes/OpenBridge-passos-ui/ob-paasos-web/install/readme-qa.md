### PaaSOS 问题集锦

#### webssh不能访问
1. 登陆安装websshd服务的服务器
2. 查看当前主机docker运行的所有容器
> docker ps -a
3. 如果存在 websshd容器，并且容器为Exits状态, 执行以下命令重新启动
> docker start websshd
如果不存在websshd容器，从新启动一个，启动命令为
> docker run -d --restart=always --name websshd -p 4200:4200 -e ADDR=<kuber-master-ip> <docker-registry-ip>:5000/websshd
