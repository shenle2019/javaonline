# Java-web学习笔记011-部署



#### 部署
让你的代码能在服务器上跑起来

常见的部署
1. 把代码放到服务器上, 在服务器上安装 gradle, jdk, 运行代码

2. java 的特有部署方式, 把代码, 依赖库, 都打包成一个压缩包, 叫 jar
  把 jar 包上传到服务器, 可以直接运行这个 jar 包. 不过还是要安装 jdk.

  


#### jar 部署过程

一. 本地 jar 运行
1. 打包 jar 包
2. 本地命令行运行 jar 包

cd build/libs
java -Dfile.encoding=UTF-8  -jar usoppMVC-1.0.jar

在本地跑得时候, 解决各种读文件的路径问题

3. 新建数据库的
需要新建 db 文件夹
需要新建对应的 txt 文件

二. 服务器部署

1. 安装 jdk

apt install openjdk-11-jdk

2. 上传 jar

scp usoppMVC-1.0.jar ubuntu@122.51.44.243:/tmp/server.jar


3.
在 /tmp 目录下
建立 db 文件夹
在 db 文件夹里, 建立 txt 文件数据库


4. 运行

在 /tmp 目录下
java -jar  /tmp/server.jar

后台运行

java -jar /tmp/server.jar &

如果有 java 进程在运行
杀进程
killall -9 java

5.
在浏览器访问页面

xxxx:9000

xxxx 是你的服务器 ip 地址

如果不能访问
    1. 查看防火墙 9000 端口是否打开
        ufw status
        2. 如果防火墙 9000 端口没有打开
        ufw allow 9000
            3. 如果防火墙 9000 端口打开了
       查看腾讯云控制台的安全组配置, 看是否打开了 9000 端口

三. debug 步骤

1. 复制下来报错日志

2. 在本地开发模式下复现, 解决
重复同样的操作, 看是否能复现同样的报错

3. 在本地 jar 模式下复现, 解决
重复同样的操作, 看是否能复现同样的报错

4. 重新部署服务器, 重复操作, 看问题是否解决

   


#### 服务器中文乱码问题
1. 编辑下面的文件, 不要拼错
nano /etc/environment

2.加入下面的内容, 保存退出
LC_CTYPE="en_US.UTF-8"
LC_ALL="en_US.UTF-8"
