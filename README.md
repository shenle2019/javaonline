# javaonline
javaonline

附件为源代码，部署完成网站为：http://usoppp.com
http://49.233.88.212/

需要做的事情
------------
1. 在 application.properties 里面改密码
2. 执行 schema.sql 里面的代码初始化数据库
3. 用 ide 打开这个项目，等初始化好后，点右上角的 SsmDevelopment 运行


项目需要关心的结构
------------------

src
    main
        java 代码
            kybmig.ssm.
                controller 路由代码
                mapper java mapper
                model 数据 model，controller 里面的 ModelAndView 的 Model 是给模板的 Model，不是一个东西
        resources 资源，包括静态资源、模板和spring 配置文件（application.properties)
            kybmig.ssm
                mapper xml mapper，xml mapper 一定要放在这里，不然 spring 找不到

gradlew/gradlew.bat 在命令行下可以用这两个直接跑 gradle 命令
build.gradle 编译、依赖和打包配置
build
    libs 打包好的 war 文件
        用 java -jar xxx.war 可以直接用内置的 tomcat 运行
        也可以放在服务器生产级的 tomcat 运行，参考后面的部署




其他
----

delegate build/run to gradle
这个现在要选，因为用了 spring boot，就是 tomcat 跑的了，不是 gradle run 跑，没 stderr stdout 问题。

java 终端不能输出中文的问题，找到一个可以设置 -Dfile.encoding=UTF-8 的地方

