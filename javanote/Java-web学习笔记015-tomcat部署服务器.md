# Java-web学习笔记015-tomcat部署服务器

```
#java environment
export JAVA_HOME=/usr/java/jdk1.8.0_271
export CLASSPATH=.:${JAVA_HOME}/jre/lib/rt.jar:${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar
export PATH=$PATH:${JAVA_HOME}/bin


服务器上处理请求三大流程
1. 接受请求
2. 处理请求
3. 发送响应

spring mvc 框架这块我们自己写的部分, 都是在处理请求

spring boot 会内置一个服务器, 叫做 Tomcat, 来做处理请求和发送响应的事情


浏览器发送请求 -> tomcat 服务器 -> Servlet -> spring mvc 框架里面的代码


浏览器发送请求 -> http 服务器(nginx, Apache) -> tomcat 服务器 -> Servlet -> spring mvc 框架里面的代码

nginx 这种 http 服务器主要处理的就是转发请求, 返回响应, 以及返回静态资源
负载均衡




服务器部署方案


1.在服务器安装部署软件
    1.1 登录上服务器，切换为 root 用户

    1.2 安装软件
    sudo apt install nginx tomcat9 mysql-server openjdk-11-jdk

    1.3 删除 tomcat9 自带的一个目录（不删就无法运行我们自己的程序）
    sudo rm -r /var/lib/tomcat9/webapps/ROOT

    1.4 安装 mysql 数据库
    sudo mysql_secure_installation
    接下来第一个选项选 n
    然后输入密码 12345，会要求你重复输入一遍
    剩下的所有选项都是选 y


2.运行前的配置
    2.1 配置 mysql 数据库
    输入 mysql 回车，会自动以 root 用户登录数据库
    这时候就进入了 mysql 软件中

    2.2 更改 mysql 密码
        ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '你的密码';
    修改下面 3 行代码中的密码，然后复制并粘贴到 mysql 软件中，回车，就完成了数据库密码修改
    SELECT user,authentication_string,plugin,host FROM mysql.user;
    ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '12345';
    FLUSH PRIVILEGES;


3.初始化数据库
    3.1 把项目中的 schema.sql 中的语句贴入 mysql 软件中，完成项目数据库的初始化
    我在这里提供项目最初的 sql 语句供参考，这里只有 todo 表
    DROP DATABASE IF EXISTS ssm;
    CREATE DATABASE ssm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    USE ssm;
    CREATE TABLE todo
    (
       `id`      INT          NOT NULL AUTO_INCREMENT,
       `content`   VARCHAR(255) NOT NULL,
       PRIMARY KEY (`id`)
    );


mysql -u用户名 -p密码 数据库名 < 数据库名.sql

mysql -uroot -p12345 ssm < ssm.sql



4.项目打包并上传到服务器运行
    4.1 在本地编译出 .war 包
    选择 ssm[build] 并运行就会自动生成 .war 包
    生成的 .war 包路径为项目下的 build/libs/ssm-0.0.1-SNAPSHOT.war

    4.2 用 scp 或者 filezilla 把生成的 .war 包拷贝到服务器上去

    # 进入项目目录下
    cd xxxx
    scp build/libs/ssm-0.0.1-SNAPSHOT.war root@122.51.44.243:/var/lib/tomcat9/webapps/ROOT.war


    4.3 用 cp 命令把上传好的 .war 包改成 tomcat9 会自动读取的名字
    假设我们把生成的 war 包上传到了服务器的 /tmp/ssm.war
    sudo cp /tmp/ssm.war /var/lib/tomcat9/webapps/ROOT.war

    拷贝完成后，tomcat9 会自动安装我们的程序，大概需要十几秒
    这时候就可以用下面的链接访问我们的网站了（前提是你没有使用 ufw 防火墙）
    http://网站:8080/todo
    http://49.233.88.212:8080/index
   http://42.192.57.201:8080/index?pageNow=1
   


5.配置 nginx 访问
    5.1 删除 nginx 自带的默认配置，否则我们的网站配置无法使用
    sudo rm /etc/nginx/sites-enabled/default

    5.2 把项目根目录下的 ssm.nginx 文件上传到服务器


    # 进入项目目录下
    cd xxxx
    scp ssm.nginx root@122.51.44.243:/tmp/ssm.nginx


    5.3 写入 nginx 配置
    假设我们把本机的 ssm.nginx 文件上传到了服务器的 /tmp/ssm.nginx
    就可以用下面的命令把配置文件放入 nginx 的配置中
    sudo cp /tmp/ssm.nginx /etc/nginx/sites-enabled/ssm.nginx

    5.4 重启 nginx 使配置生效
    sudo service nginx restart


    这时候，就可以使用下面的链接访问我们的网站了
    http://网站/todo
   
   http://usoppp.com/index?pageNow=1
   
   http://42.192.57.201:8080/index?pageNow=1
   http://42.192.57.201:80/index?pageNow=1
   
      
      firewall-cmd --reload
      
   


6. 配置图片上传路径
    1. 在服务器上对应路径建立文件夹
        cd /var/lib/tomcat9/webapps
        mkdir images

     2. 修改图片文件夹的访问权限
        chmod 777 images

     3. 访问 http://网站/upload/index
        上传一张名字为 1.png 的图片
        上传成功后, 刷新网页, 就能显示该图片

如何调试

1. 本机用 debug, 看是否能正常运行
   1.1. 看 log 输出，有没有报错，错误可能在输出中间。
   1.2. 浏览器按 f12 看前端有没有报错。


2. 如果本地可以运行，再部署到服务器测试。
    2.1. 用 tomcat 启动程序
    2.2. 服务器 curl 本机端口, 测试是否能在服务器本地访问
        curl localhost:8080/todo


3. 用 tomcat 启动程序
    3.1. 看状态：`systemctl status 服务名`
       systemctl status tomcat9
    3.2. 看日志：`journalctl -e -u 服务名`
    3.# tomcat 日志，-e 跳到最后面，-u 指定看哪个日志
        journalctl -e -u tomcat9
        或者在 /var/log/tomcat 下查看日志文件
    3.3. 把 war 包拷贝到 /var/lib/tomcat9/webapps/下, 改名为 ROOT.war
    3.4. 在命令行用 `curl localhost:8080/todo` 来测试是否能在服务器本地访问。
    3.5. 用浏览器来来测试是否能在本机访问 `x.x.x.x`。


4. 进入 nginx
    4.1. 看状态：`systemctl status nginx`。
    4.2. 看日志：`journalctl -e -u nginx` 或者在 /var/log/nginx 下查看日志文件
    4.3. 看配置：`/etc/nginx/sites-enabled/xxx`
    4.4. 在命令行用 `curl http://127.0.0.1` 来测试是否能在服务器本地访问。
    4.5. 用浏览器来来测试是否能在本机访问 `x.x.x.x`。
    4.6. 在浏览器里按 f12 看前端有没有报错。


如果解决掉一个 bug 后还是有其他 bug，重复上面步骤。



更新代码

1. tomcat 部署更新
    1.1 在本地编译出 .war 包
    选择 ssm[build] 并运行就会自动生成 .war 包
    生成的 .war 包路径为项目下的 build/libs/ssm-0.0.1-SNAPSHOT.war

    1.2 用 scp 或者 filezilla 把生成的 .war 包拷贝到服务器上去

    1.3 用 cp 命令把上传好的 .war 包改成 tomcat9 会自动读取的名字
    假设我们把生成的 war 包上传到了服务器的 /tmp/ssm.war
    sudo cp /tmp/ssm.war /var/lib/tomcat9/webapps/ROOT.war

    拷贝完成后，tomcat9 会自动安装我们的程序，大概需要十几秒
    这时候就可以用下面的链接访问我们的网站了（前提是你没有使用 ufw 防火墙）
    http://网站:8080/todo


2. nginx 配置更新
    2.1 把项目根目录下的 ssm.nginx 文件上传到服务器

    2.2 写入 nginx 配置
        假设我们把本机的 ssm.nginx 文件上传到了服务器的 /tmp/ssm.nginx
        就可以用下面的命令把配置文件放入 nginx 的配置中
        sudo cp /tmp/ssm.nginx /etc/nginx/sites-enabled/ssm.nginx

        要记得删除 /etc/nginx/sites-enabled 目录下其他的配置文件, 否则会有 80 端口冲突

    2.3 重启 nginx 使配置生效
        sudo service nginx restart

    这时候，就可以使用下面的链接访问我们的网站了
    http://网站/todo




## 端口冲突/被占用用如下方法解决

1. 找到是哪个端口冲突
    看输出或者代码。

    报错里会有
    Caused by: java.net.BindException: Address already in use: bind
    use


2. 找到是哪个程序占用端口，并关掉。

有专门的命令 lsof 能找到端口对应程序
    1. 非 80 端口。
        1. 找到之前启动的后台程序的 id。ps aux | grep java
        2. 杀掉进程。 kill 进程id
        3. 再次检查后台程序 ps aux | grep java
        4. 如果发现程序又被启动了，比如换了个新进程 id，说明是 systemd 守护的服务。
        5. 找到进程id对应的服务 systemctl status 进程id
        6. 关掉这个服务。systemctl stop 服务名。
    2. 80 端口。除了考虑上述非 80 端口情况外，还需要把 nginx 给关了。
```

