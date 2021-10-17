# Java-web学习笔记012-JDBC+SSM



### Spring MVC ,Spring Boot

什么是 Spring
Spring 是一个大的框架

JB: Java Bean
EJB: 在 JB 的基础上发展, 加了很多新的规定和工具

JB 和 EJB 都是为了更好的组件化

Spring MVC
建立在 Spring 框架上的 MVC

Spring boot
Spring 是一个框架, 框架就有配置
比如服务器运行的端口, 数据库的名字, 密码, 编码格式
这套配置很难写
为了简化这些配置的写法, 有人提前写好了一套配置, 这就是 spring boot



#### 需要做的事情

------------
1. 在 application.properties 里面改密码
2. 执行 schema.sql 里面的代码初始化数据库
3. 用 ide 打开这个项目，等初始化好后，点右上角的 SsmDevelopment 运行


#### 项目需要关心的结构

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




#### 其他

----

delegate build/run to gradle
这个现在要选，因为用了 spring boot，就是 tomcat 跑的了，不是 gradle run 跑，没 stderr stdout 问题。

java 终端不能输出中文的问题，找到一个可以设置 -Dfile.encoding=UTF-8 的地方



#### HelloWorldController

```java

package kybmig.ssm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloWorldController {

    @GetMapping("/demo")
    public ModelAndView demoRoute(@RequestParam(name="name", required=false, defaultValue="默认名字测试") String name) {

        // model 说的是给模板引擎的 model
        // view 说的是模板名字，没有后缀
        // view 可以自动补全，也可以直接跳转
        ModelAndView mv = new ModelAndView("hello");
        mv.addObject("name", name);
        return mv;
    }

    @GetMapping("/demo1")
    // 会自动从 reqeust 里面找名字为 usopp 的参数
    public ModelAndView demoRoute1(String usopp) {

        // model 说的是给模板引擎的 model
        // view 说的是模板名字，没有后缀
        // view 可以自动补全，也可以直接跳转
        ModelAndView mv = new ModelAndView("hello");
        mv.addObject("name", usopp);
        return mv;
    }


    @GetMapping("/demo2")
    // 从 HttpServletRequest 里面取参数
    public ModelAndView demoRoute2(HttpServletRequest request) {

        // model 说的是给模板引擎的 model
        // view 说的是模板名字，没有后缀
        // view 可以自动补全，也可以直接跳转
        ModelAndView mv = new ModelAndView("hello");
        String name = request.getParameter("name");
        mv.addObject("name", name);
        return mv;
    }


    // 动态路由
    // 如果访问 /demo3/xubai, 那么 xubai 就会当做参数传给路由函数
    @GetMapping("/demo3/{name}")
    // 从 HttpServletRequest 里面取参数
    public ModelAndView demoRoute2(@PathVariable String name) {

        // model 说的是给模板引擎的 model
        // view 说的是模板名字，没有后缀
        // view 可以自动补全，也可以直接跳转
        ModelAndView mv = new ModelAndView("hello");
        mv.addObject("name", name);
        return mv;
    }
}
```

#### TodoController

```java
package kybmig.ssm.controller;
import kybmig.ssm.Utility;
import kybmig.ssm.mapper.TodoMapper;
import kybmig.ssm.model.TodoModel;
import kybmig.ssm.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TodoController {

    // 现在如果要用 todoService, 必须在 Controller 里面声明一个 TodoService 属性
    // 同时在构造器函数中, 传入 TodoService 这个参考, 并赋值
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todo/add")
    public String add(String content) {
        TodoModel todo = todoService.addBySQL(content);
        Utility.log("todo add id %s", todo.getId());
        return "redirect:/todo";
    }

    @GetMapping("/todo/delete")
    public String deleteMapper(Integer id) {
        todoService.deleteByIdBySQL(id);
        return "redirect:/todo";
    }

    // 相当于下面的写法
    // @GetMapping("/todo/delete")
    // public String deleteMapper(HttpServletRequest request) {
    //     Integer id = Integer.valueOf(request.getParameter("id");)
    //     todoService.deleteById(id);
    //     return "redirect:/todo";
    // }


    @GetMapping("/todo/edit")
    public ModelAndView edit(Integer id) {
        TodoModel todo = todoService.findByIdBySQL(id);

        ModelAndView m = new ModelAndView("todo_edit");
        m.addObject("todo", todo);
        return m;
    }

    @PostMapping("/todo/update")
    public String updateMapper(Integer id, String content) {
        todoService.updateBySQL(id, content);
        return "redirect:/todo";
    }

    @GetMapping("/todo")
    public ModelAndView index() {

        List<TodoModel> todos = todoService.allBySQL();
        ModelAndView m = new ModelAndView("todo_index");
        m.addObject("todos", todos);
        return m;
    }
}

```

#### Todomapper.java

```java
package kybmig.ssm.mapper;

import kybmig.ssm.model.TodoModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

// 这个是 spring 用来在 controller 进行依赖注入的。
@Repository
// 这个是 mybaits spring boot 用来自动跟 xml 联系起来，并注入到 spring 的 session 里面。
@Mapper
public interface TodoMapper {
    void insertTodo(TodoModel todo);

    List<TodoModel> selectAllTodo();

    TodoModel selectTodo(int id);

    void updateTodo(TodoModel todo);

    void deleteTodo(int id);
}
```

#### Todomapper.xml

```xml
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 和 java mapper 的类名一样，从包名开始打，有自动补全-->
<mapper namespace="kybmig.ssm.mapper.TodoMapper">
    <!-- resultType 和 mapper 中的方法返回值一样，不能省，从包名开始打，有自动补全-->
    <!--  里面的 sql 语句，有自动补全,要配置 ide 的 datasource -->
    <select id="selectAllTodo" resultType="kybmig.ssm.model.TodoModel">
        SELECT * FROM ssm.todo
    </select>

    <!-- 传参数方式1，直接传入一个基本类型 int，这个 int 会被绑定到 ${id} -->
    <select id="selectTodo" resultType="kybmig.ssm.model.TodoModel">
        SELECT * FROM ssm.todo WHERE id = ${id}
    </select>

    <!-- 传参方式2，传入一个对象。content 代表这个对象的 content 属性，注意这个对象要有 getter setter -->
    <!-- useGeneratedKeys keyProperty 当我传入一个对象的时候，这两个属性可以把数据库创建的 id 赋值给这个对象 -->
    <insert id="insertTodo" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO ssm.todo (content) VALUES (#{content})
    </insert>
    
    <update id="updateTodo">
        UPDATE ssm.todo SET content = #{content} WHERE id = #{id}
    </update>

    <delete id="deleteTodo">
        DELETE FROM ssm.todo WHERE id = #{id}
    </delete>
</mapper>
```

#### TodoModel.java

```java
package kybmig.ssm.model;

public class TodoModel {
    private Integer id;
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

```

#### application.properties

```
# mysql 有两个 utf8 标准，一定要用新的 4字节 utf8mb4
# For 8.0.13 and later: You can use characterEncoding=UTF-8 to use utf8mb4
spring.datasource.url=jdbc:mysql://localhost:3306/ssm?characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# 请求和响应的 log
logging.level.org.springframework.web=DEBUG
# mybatis session 和 sql log
logging.level.org.mybatis=DEBUG
logging.level.kybmig.ssm.mapper=DEBUG


# 服务器运行端口
server.port=9000

```

#### schema.sql

```
DROP DATABASE IF EXISTS ssm;
CREATE DATABASE ssm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ssm;

CREATE TABLE Todo
(
    `id`      INT          NOT NULL AUTO_INCREMENT,
    `content`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE Weibo
(
    `id`      INT          NOT NULL AUTO_INCREMENT,
    `content`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);
```

#### Topic mybatis 版本

ssm

spinrg, spring mvc, mybatis

1.
Topic mybatis 版本

    1. 新建一个 TopicModel
    2. 新建一个 Topic 表
        1. workbench 创建一个表
        2. 复制记录创表语句
    3. 新建一个 TopicMapper
    4. 新建一个 TopicMapper.xml
    5. 新建一个 TopicService
    6. 新建一个 TopicController
        1. 实现 index 路由
        2. 实现 add 路由
    7. 做页面, topic_index


实现编辑功能
1. edit 路由, 返回 topic_edit 页面
2. update 路由, 接受 post 数据, 更新数据


实现文章浏览功能
1. detai 路由
2. detail 页面
3. 在 index 页面实现点击文章标题, 跳转到 detail 页面

2.
User mybatis
实现用户的注册和登录



#### 写一个不带用户的 WeiboController

```java

1. 实现 WeiboModel
2. 实现 WeiboService
3. 实现 WeiboController
    1. 实现路由 /weibo
    2. 实现路由 /weibo/add
4. 实现 weibo_index.ftl 页面

1.WeiboModel

package kybmig.ssm.model;

public class WeiboModel {
    private Integer id;
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


2.WeiboService:

package kybmig.ssm.service;


import kybmig.ssm.model.ModelFactory;
import kybmig.ssm.model.WeiboModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WeiboService {
    public WeiboModel add(String content) {
        ArrayList<WeiboModel> ms = load();


        WeiboModel m = new WeiboModel();
        m.setContent(content);
        Integer id = ms.size() + 1;
        m.setId(id);
        ms.add(m);
        save(ms);
        return m;
    }


    public void update(Integer id, String content) {
        ArrayList<WeiboModel> weibos = load();
        for (int i = 0; i < weibos.size(); i++) {
            WeiboModel e = weibos.get(i);
            if (e.getId().equals(id)) {
                e.setContent(content);
            }
        }
        save(weibos);
    }


    public void deleteById(Integer id) {
        ArrayList<WeiboModel> ms = load();

        for (int i = 0; i < ms.size(); i++) {
            WeiboModel e = ms.get(i);
            if (e.getId().equals(id)) {
                ms.remove(e);
            }
        }
        save(ms);
    }


    public  WeiboModel findById(Integer id) {
        ArrayList<WeiboModel> weibos = load();
        for (int i = 0; i < weibos.size(); i++) {
            WeiboModel e = weibos.get(i);
            if (e.getId().equals(id)) {
                return e;
            }
        }

        // 索引的用法
        // weibo weibo = indexInteger.get(id);
        return null;
    }


    public  ArrayList<WeiboModel> all() {
        return load();
    }

    public ArrayList<WeiboModel> load() {
        String className = WeiboModel.class.getSimpleName();

        ArrayList<WeiboModel> ms = ModelFactory.load(className, 2, modelData -> {
            Integer id = Integer.valueOf(modelData.get(0));
            String content = modelData.get(1);

            WeiboModel m = new WeiboModel();
            m.setId(id);
            m.setContent(content);
            return m;
        });
        return ms;
    }



    public static void save(ArrayList<WeiboModel> list) {
        String className = WeiboModel.class.getSimpleName();
        ModelFactory.save(className, list, model -> {
            StringBuilder s = new StringBuilder();
            s.append(model.getId());
            s.append("\n");
            s.append(model.getContent());
            s.append("\n");
            return s.toString();
        });
    }
}


3.WeiboController:

package kybmig.ssm.controller;

import kybmig.ssm.Utility;
import kybmig.ssm.model.WeiboModel;
import kybmig.ssm.service.WeiboService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class WeiboController {

    // 现在如果要用 weiboService, 必须在 Controller 里面声明一个 WeiboService 属性
    // 同时在构造器函数中, 传入 WeiboService 这个参考, 并赋值
    private WeiboService weiboService;

    public WeiboController(WeiboService weiboService) {
        this.weiboService = weiboService;
    }

    @PostMapping("/weibo/add")
    public String add(String content) {
        WeiboModel weibo = weiboService.add(content);
        Utility.log("weibo add id %s", weibo.getId());
        return "redirect:/weibo";
    }

    @GetMapping("/weibo/delete")
    public String deleteMapper(Integer id) {
        weiboService.deleteById(id);
        return "redirect:/weibo";
    }

//    // 相当于下面的写法
//    // @GetMapping("/weibo/delete")
//    // public String deleteMapper(HttpServletRequest request) {
//    //     Integer id = Integer.valueOf(request.getParameter("id");)
//    //     weiboService.deleteById(id);
//    //     return "redirect:/weibo";
//    // }
//
//
    @GetMapping("/weibo/edit")
    public ModelAndView edit(Integer id) {
        WeiboModel weibo = weiboService.findById(id);

        ModelAndView m = new ModelAndView("weibo_edit");
        m.addObject("weibo", weibo);
        return m;
    }

    @PostMapping("/weibo/update")
    public String updateMapper(Integer id, String content) {
        weiboService.update(id, content);
        return "redirect:/weibo";
    }

    @GetMapping("/weibo")
    public ModelAndView index() {

        List<WeiboModel> weibos = weiboService.all();
        ModelAndView m = new ModelAndView("weibo_index");
        m.addObject("weibos", weibos);
        return m;
    }
}


4.weibo_index.ftl

<!DOCTYPE HTML>
<html>
<head>
    <title>ssm weibo</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<form action="/weibo/add" method="POST">
    <input type="text" name="content" placeholder="请输入weibo">
    <br>
    <button type="submit">添加</button>
</form>

<div>

    <#list weibos as t>
    <h3>${t.id} : ${t.content}</h3>
    <a href="/weibo/edit?id=${t.id}">编辑</a>
    <a href="/weibo/delete?id=${t.id}">删除</a>
</#list>
</div>
</body>
</html>


5. WeiboMapper

package kybmig.ssm.mapper;

import kybmig.ssm.model.WeiboModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

// 这个是 spring 用来在 controller 进行依赖注入的。
@Repository
// 这个是 mybaits spring boot 用来自动跟 xml 联系起来，并注入到 spring 的 session 里面。
@Mapper
public interface WeiboMapper {
    void insertWeibo(WeiboModel weibo);

    List<WeiboModel> selectAllWeibo();

    WeiboModel selectWeibo(int id);

    void updateWeibo(WeiboModel weibo);

    void deleteWeibo(int id);
}


实现删除微博功能
1. 实现 WeiboController 的 delete 路由
2. 实现 WeiboService 的 deleteById 函数
    
1.WeiboController

package kybmig.ssm.controller;

import kybmig.ssm.Utility;
import kybmig.ssm.model.WeiboModel;
import kybmig.ssm.service.WeiboService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class WeiboController {

    // 现在如果要用 weiboService, 必须在 Controller 里面声明一个 WeiboService 属性
    // 同时在构造器函数中, 传入 WeiboService 这个参考, 并赋值
    private WeiboService weiboService;

    public WeiboController(WeiboService weiboService) {
        this.weiboService = weiboService;
    }

    @PostMapping("/weibo/add")
    public String add(String content) {
        WeiboModel weibo = weiboService.add(content);
        Utility.log("weibo add id %s", weibo.getId());
        return "redirect:/weibo";
    }

    @GetMapping("/weibo/delete")
    public String deleteMapper(Integer id) {
        weiboService.deleteById(id);
        return "redirect:/weibo";
    }

//    // 相当于下面的写法
//    // @GetMapping("/weibo/delete")
//    // public String deleteMapper(HttpServletRequest request) {
//    //     Integer id = Integer.valueOf(request.getParameter("id");)
//    //     weiboService.deleteById(id);
//    //     return "redirect:/weibo";
//    // }
//
//
    @GetMapping("/weibo/edit")
    public ModelAndView edit(Integer id) {
        WeiboModel weibo = weiboService.findById(id);

        ModelAndView m = new ModelAndView("weibo_edit");
        m.addObject("weibo", weibo);
        return m;
    }

    @PostMapping("/weibo/update")
    public String updateMapper(Integer id, String content) {
        weiboService.update(id, content);
        return "redirect:/weibo";
    }

    @GetMapping("/weibo")
    public ModelAndView index() {

        List<WeiboModel> weibos = weiboService.all();
        ModelAndView m = new ModelAndView("weibo_index");
        m.addObject("weibos", weibos);
        return m;
    }
}


2.WeiboService

package kybmig.ssm.service;


import kybmig.ssm.model.ModelFactory;
import kybmig.ssm.model.WeiboModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WeiboService {
    public WeiboModel add(String content) {
        ArrayList<WeiboModel> ms = load();


        WeiboModel m = new WeiboModel();
        m.setContent(content);
        Integer id = ms.size() + 1;
        m.setId(id);
        ms.add(m);
        save(ms);
        return m;
    }


    public void update(Integer id, String content) {
        ArrayList<WeiboModel> weibos = load();
        for (int i = 0; i < weibos.size(); i++) {
            WeiboModel e = weibos.get(i);
            if (e.getId().equals(id)) {
                e.setContent(content);
            }
        }
        save(weibos);
    }


    public void deleteById(Integer id) {
        ArrayList<WeiboModel> ms = load();

        for (int i = 0; i < ms.size(); i++) {
            WeiboModel e = ms.get(i);
            if (e.getId().equals(id)) {
                ms.remove(e);
            }
        }
        save(ms);
    }


    public  WeiboModel findById(Integer id) {
        ArrayList<WeiboModel> weibos = load();
        for (int i = 0; i < weibos.size(); i++) {
            WeiboModel e = weibos.get(i);
            if (e.getId().equals(id)) {
                return e;
            }
        }

        // 索引的用法
        // weibo weibo = indexInteger.get(id);
        return null;
    }


    public  ArrayList<WeiboModel> all() {
        return load();
    }

    public ArrayList<WeiboModel> load() {
        String className = WeiboModel.class.getSimpleName();

        ArrayList<WeiboModel> ms = ModelFactory.load(className, 2, modelData -> {
            Integer id = Integer.valueOf(modelData.get(0));
            String content = modelData.get(1);

            WeiboModel m = new WeiboModel();
            m.setId(id);
            m.setContent(content);
            return m;
        });
        return ms;
    }



    public static void save(ArrayList<WeiboModel> list) {
        String className = WeiboModel.class.getSimpleName();
        ModelFactory.save(className, list, model -> {
            StringBuilder s = new StringBuilder();
            s.append(model.getId());
            s.append("\n");
            s.append(model.getContent());
            s.append("\n");
            return s.toString();
        });
    }
}


3.WeiboMapper:

package kybmig.ssm.mapper;

import kybmig.ssm.model.WeiboModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

// 这个是 spring 用来在 controller 进行依赖注入的。
@Repository
// 这个是 mybaits spring boot 用来自动跟 xml 联系起来，并注入到 spring 的 session 里面。
@Mapper
public interface WeiboMapper {
    void insertWeibo(WeiboModel weibo);

    List<WeiboModel> selectAllWeibo();

    WeiboModel selectWeibo(int id);

    void updateWeibo(WeiboModel weibo);

    void deleteWeibo(int id);
}

实现修改微博功能
1. 实现 WeiboController
    1. 实现 edit 路由, 返回一个编辑页面 weibo_edit.ftl
    2. 实现 update 路由
2. WeiboService 实现 update 函数
3. 实现 weibo_edit.ftl 页面
    
1.WeiboController

    @GetMapping("/weibo/edit")
    public ModelAndView edit(Integer id) {
        WeiboModel weibo = weiboService.findById(id);

        ModelAndView m = new ModelAndView("weibo_edit");
        m.addObject("weibo", weibo);
        return m;
    }

    @PostMapping("/weibo/update")
    public String updateMapper(Integer id, String content) {
        weiboService.update(id, content);
        return "redirect:/weibo";
    }

2.WeiboService


    public void update(Integer id, String content) {
        ArrayList<WeiboModel> weibos = load();
        for (int i = 0; i < weibos.size(); i++) {
            WeiboModel e = weibos.get(i);
            if (e.getId().equals(id)) {
                e.setContent(content);
            }
        }
        save(weibos);
    }



3.weibo_edit.ftl

<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>修改 WEIBO 的页面</title>
</head>
<body>
    <h1>修改 WEIBO</h1>
    <form action="/weibo/update" method="post">
        <input name="id" placeholder="id" value="${weibo.id}">
        <input name="content" placeholder="content" value="${weibo.content}">
        <br>
        <button type="submit">提交修改</button>
    </form>
</body>
</html>
   
仿照 TodoService 里面的 addBySQL
实现 WeiboService 的 addBySQL     
    
public MysqlDataSource getDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("123");
        dataSource.setServerName("127.0.0.1");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("ssm");
        dataSource.setURL(dataSource.getUrl() + "?serverTimezone=UTC");
//        dataSource.setURL(dataSource.getUrl() + "?serverTimezone=UTC");
        return dataSource;
    }


    // 有 sql 注入风险的执行方式
    public void addBySQLNotSave(String content) {
        MysqlDataSource ds = this.getDataSource();

        try {
            Connection connection = ds.getConnection();
            Statement statement = connection.createStatement();

            String sql = String.format("Insert into `Weibo` (content) values ('%s')", content);
            statement.execute(sql);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public WeiboModel addBySQL(String content) {
        WeiboModel m = new WeiboModel();
        m.setContent(content);

        MysqlDataSource ds = this.getDataSource();

        String sql = "Insert into `weibo` (content) values (?)";
        try (
                Connection connection = ds.getConnection();
                // 设置 flag
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, content);
            statement.executeUpdate();

            try(ResultSet rs = statement.getGeneratedKeys()) {
                rs.first();
                // 取到自增 id
                Integer id = rs.getInt("GENERATED_KEY");
                m.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return m;
    }

仿照 TodoController
实现一个 WeiboController
要求实现增删改查的功能和路由
    
1.weiboController:

package kybmig.ssm.controller;

import kybmig.ssm.Utility;
import kybmig.ssm.mapper.WeiboMapper;
import kybmig.ssm.model.WeiboModel;
import kybmig.ssm.service.WeiboService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class WeiboController {

    // 现在如果要用 todoService, 必须在 Controller 里面声明一个 TodoService 属性
    // 同时在构造器函数中, 传入 TodoService 这个参考, 并赋值
    private WeiboService WeiboService;

    public WeiboController(WeiboService WeiboService) {
        this.WeiboService = WeiboService;
    }

    @GetMapping("/weibo")
    public ModelAndView index() {

        List<WeiboModel> weibos = WeiboService.all();
        ModelAndView m = new ModelAndView("weibo/weibo_index");
        m.addObject("weibos", weibos);
        return m;
    }

    @GetMapping("/weibo/detail/{id}")
    public ModelAndView detail(@PathVariable Integer id) {
        WeiboModel weibo = WeiboService.findById(id);
        ModelAndView m = new ModelAndView("weibo/weibo_detail");
        m.addObject("weibo", weibo);
        return m;
    }

    @PostMapping("/weibo/add")
    public ModelAndView add(String title, String content) {
        WeiboModel weibo = WeiboService.add(title, content);
        Utility.log("weibo add id %s", weibo.getId());
        return new ModelAndView("redirect:/weibo");
    }

    // @GetMapping("/todo/delete")
    // public String deleteMapper(Integer id) {
    //     todoService.deleteById(id);
    //     return "redirect:/todo";
    // }

    // 相当于下面的写法
    @GetMapping("/weibo/delete")
    public ModelAndView deleteMapper(HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        WeiboService.deleteById(id);
        return new ModelAndView("redirect:/weibo");
    }


    @GetMapping("/weibo/edit")
    public ModelAndView edit(Integer id) {
        WeiboModel weibo = WeiboService.findById(id);

        ModelAndView m = new ModelAndView("weibo/weibo_edit");
        m.addObject("weibo", weibo);
        return m;
    }

    @PostMapping("/weibo/update")
    public ModelAndView updateMapper(Integer id, String title, String content) {
        WeiboService.update(id, title, content);
        return new ModelAndView("redirect:/weibo");
    }
}



2.weibo_index.ftl

<!DOCTYPE HTML>
<html>
<head>
    <title>Weibo</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<form action="/weibo/add" method="POST">
    <input type="text" name="title" placeholder="请输入 title">
    <br>
    <input type="text" name="content" placeholder="请输入 weibo">
    <br>
    <button type="submit">添加</button>
</form>

<div>

    <#list weibos as t>
    <h3>
        <a href="/weibo/detail/${t.id}">${t.id} : ${t.title}</a>
    </h3>
    <a href="/weibo/edit?id=${t.id}">编辑</a>
    <a href="/weibo/delete?id=${t.id}">删除</a>
    </#list>
</div>
</body>
</html>


3.weibo_edit.ftl

<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>修改 Weibo 的页面</title>
</head>
<body>
    <h1>修改 Weibo</h1>
    <form action="/weibo/update" method="post">
<#--        <h3>id</h3>-->
        <input name="id" placeholder="id" value="${weibo.id}" hidden>
        <br>
        <h3>标题</h3>
        <input name="title" placeholder="title" value="${weibo.title}">
        <br>
        <h3>内容</h3>
        <input name="content" placeholder="content" value="${weibo.content}">
        <br>
        <button type="submit">提交修改</button>
    </form>
</body>
</html>



4.weibo_detail.ftl

<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>${weibo.title}</title>
</head>
<body>
    <h1>${weibo.title}</h1>

    <p>${weibo.content}</p>

</body>
</html>
    
    
```





#### AOP编程

```java

AOP, 面向切面编程
log()

TopicController tc = new TopicController()
tc.index()

log()
    
    
1. 给 TopicController 的 update 路由加上 ownerRequired
@Around("execution(* kybmig.ssm.controller.TopicController.updateMapper(..))")
    public ModelAndView owerRequired2(ProceedingJoinPoint joint) throws Throwable {
        Utility.log("owerRequired 正在访问的 url %s", request.getRequestURI());
        Utility.log("owerRequired 正在执行的方法 %s %s", joint.getSignature(), joint.getArgs());
        Integer userID = (Integer) request.getSession().getAttribute("user_id");
        String topicIdParameter = request.getParameter("id");
        Utility.log("currentUser(%s), topic Owner(%s)", userID, topicIdParameter);

        if (userID == null || topicIdParameter == null) {
            return new ModelAndView("redirect:/login");
        } else {
            UserModel currentUser = userService.findById(userID);
            TopicModel topicAccessed = topicService.findById(Integer.valueOf(topicIdParameter));
            if (currentUser == null || topicAccessed == null) {
                return new ModelAndView("redirect:/login");
            } else {
                if (currentUser.getId().equals(topicAccessed.getUserId())) {
                    // 执行被插入的方法
                    return (ModelAndView) joint.proceed();
                } else {
                    Utility.log("警告页面");
                    return new ModelAndView("redirect:/alert");
                }
            }
        }
    }
    
2. 给 WeiboController 的所有路由加上 loginRequired

@Around("execution(* kybmig.ssm.controller.WeiboController.*(..))")
    public ModelAndView loginRequired(ProceedingJoinPoint joint) throws Throwable {
        // 在 TodoController 下面所有方法被执行的时候调用
        // @Around 能在执行方法之前和之后处理。由 loginRequired 决定什么时候调用 controller 的方法。
        // execution 匹配方法执行。Aspect 功能非常多，除了能在方法执行的时候插入，还能在其他地方插入。
        // * kybmig.ssm.controller.TodoController.*(..)
        // 第一个 *，匹配任意的方法返回值
        // 第二个 *，匹配 TodoController 下的任意方法，可以把 * 换成具体方法名。这里有自动补全。
        // (..) 匹配任意参数签名
        // 简写 @Around("within(kybmig.ssm.controller.TodoController)")
        // ProceedingJoinPoint 正在被调用的方法
        // 返回值类型要和被处理的控制器方法类型一样。所以 TodoController 的所有方法返回值都要是 ModelAndView
        Utility.log("loginRequired 正在访问的 url %s", request.getRequestURI());
        Utility.log("loginRequired 正在执行的方法 %s %s", joint.getSignature(), joint.getArgs());
        Integer userID = (Integer) request.getSession().getAttribute("user_id");
        if (userID == null) {
            // 跳转回登陆页面
            Utility.log("loginRequired 没有 session");
            return new ModelAndView("redirect:/login");
        } else {
            UserModel u = userService.findById(userID);
            if (u == null) {
                // 跳转回登陆页面
                Utility.log("loginRequired 用户不存在 %s", userID);
                return new ModelAndView("redirect:/login");
            } else {
                // 执行被插入的方法
                return (ModelAndView) joint.proceed();
            }
        }
    }
```

