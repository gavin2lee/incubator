##### 执行以下命令
#### docker build -t paasos/os:3.0 .
 
# 构建镜像：


> 在Dockerfile所在的目录执行如下命令

> docker build -t 192.168.1.72:5000/openbridge/paasos-ui:3.0_20160705 .


# 使用镜像：
```
sudo docker create -it \
      -e PREFIX=os \
      -e DB_HOST=192.168.10.1 \
      -e DB_PORT=3306 \
      -e DB_NAME=openbridge \
      -e DB_USER=\
      -e DB_PSWD=\
      -e FILE_STORAGE=<> \ 
      openbridge/paasos-ui:3.0_20160705
``` 

# 环境变量说明:

```
| 环境变量           | 必须   |  默认值                          | 说明                  |
| OS_SVR_IPS        | 可选  | 172.20.0.0                      | k8s 服务IP的IP段       | 
| FILE_STORAGE      | 是    | /data/resource                  | 文件存储路径            |
| PREFIX            | 是    | app                             |  Tomcat 的 Context    |
| DB_HOST           | 是    |                                 |                      |
| DB_PORT           | 是    |                                 |      |
| DB_USER           | 是    |                                 |      |
| DB_NAME           | 是    |                                 |      |
