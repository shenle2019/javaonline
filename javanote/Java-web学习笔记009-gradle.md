# Java-web学习笔记009-gradle



### gradle作用

一句话，打包用的。



### 主要功能概括

- 差异管理
- 依赖管理
- 项目部署



1. Gradle***是一种构建工具\***,它可以帮你管理项目中的差异,依赖,编译,打包,部署......,你可以定义满足自己需要的构建逻辑,写入到build.gradle中供日后复用.
2. Gradle***不是一种编程语言\***,它不能帮你实现软件中的任何实际功能



### 通俗理解

ant可以自动化打包逻辑。

maven也可以自动化打包，相比于ant，它多做的事实帮你下载jar包。

但是maven的打包逻辑太死板，定制起来太麻烦，不如ant好用。

gradle就是又能自动下载jar包，又能自己写脚本，并且脚本写起来还比ant还用的这么个东西。



备注：

Ant和Maven都是基于XML的构建工具，Gradle是用Groovy编写的构建工具，Groovy是JVM衍生的与JAVA语法高度兼容的动态强类型语言。

Gradle通过编写一个名为build.gradle的脚本文件对项目进行设置，再根据这个脚本对项目进行构建（复杂的项目也有其他文件）
Gradle 脚本本质上就是Groovy脚本，只不过高度利用了groovy的语法糖，例如省略方法参数括号和省略句尾分号等，让代码看起来像DSL。
所以几乎所有java和groovy支持的语法，它的脚本都支持。（使用JAVA和GROOVY的SDK应该也是没问题的）

Gradle支持基于惯例的构建，并有丰富的适合不同情景下的插件库，光有官方用户手册支持的就有20多种，方便构建。

Gradle里有两个基本概念：项目(projects)和任务(tasks)。项目由多个任务组成，一个项目可以理解为提供给不同设备的构建版本，如桌面版、网页版、安卓版、iOS版等等，也可以理解为一种行为，例如部署应用到生产环境。任务相当于Ant的target，可以理解成一个构建中原子性的工作，例如编译、打包、执行等。需要注意的是，Ant中他自己的命令例如javac、copy等也叫做task，但Ant的task远没有Gradle的task那么自由。

Gradle的构建分两个阶段，第一阶段是设置阶段(configuration phase)，分析构建脚本，处理依赖关系和执行顺序等，脚本本身也需要依赖来完成自身的分析。第二阶段是执行阶段(execution phase)，此阶段真正构建项目并执行项目下的各个任务。

Ant与Maven对于Gradle，前者编写容易，但功能有限，需要人工操作的过程也多；后者依托于庞大的依赖仓库，因此有着强大的外部依赖管理，但添加本地依赖并不方便，且项目不能灵活修改。而Gradle能很好地结合Ant与Maven各自的优点，可以随意的编写任务并组合成项目，直接利用Maven仓库，并且能很好的支持传递依赖和内部依赖。



build.gradle

```java
plugins {
    id 'java'
    // 替换了 gradle 内置的依赖管理，能利用 spring 项目本身的 maven 依赖文件
    id 'io.spring.dependency-management' version '1.0.7.RELEASE'
    id 'war'
    id 'idea'
}

group = 'kybmig'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

// 编译输出能显示中文
compileJava.options.encoding = "UTF-8"
compileTestJava.options.encoding = "UTF-8"

repositories {
    maven { url 'https://maven.aliyun.com/repository/public/' }
    mavenCentral()
}

configurations {
    // 编译后热更新配置
    developmentOnly
    runtimeClasspath {
        extendsFrom developmentOnly
    }
}

dependencies {
    // 现在都用 implementation， compile 被弃用了
    
    compile group: 'org.freemarker', name: 'freemarker', version: '2.3.28'
    
    testCompile group: 'junit', name: 'junit', version: '4.12'
}


// 让 idea 的输出目录和 gradle 一致，这样 idea 运行的时候就能利用 gradle 的编译。加速编译和运行速度。
idea{
    module{
        inheritOutputDirs = false
        outputDir = compileJava.destinationDir
        testOutputDir = compileTestJava.destinationDir
    }
}


```



### 对模板引擎的理解

模板引擎主要是将路由函数中经常要用到的一些html放到html模板中,直接在模板中修改显示内容,

这样就把要显示的内容和代码控制内容分开了,体现了写服务器过程中的MVC思想;

从而使思路清晰,易于理解和修改;



```java


package usoppMVC;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;

public class usoppTemplate {
    static Configuration config;
    //
    static {
        // static 里面的东西只会被初始化一次
        config = new Configuration(
                Configuration.VERSION_2_3_28);
        try {
            File f = new File("build/resources/main/templates");
            // Utils.log("file path<%s>", f.getAbsolutePath());
            config.setDirectoryForTemplateLoading(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config.setDefaultEncoding("utf-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        config.setWrapUncheckedExceptions(true);
    }

    public static String render(Object data, String templateFileName) {
        Template template;
        try {
            template = config.getTemplate(templateFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(result);
        try {
            template.process(data, writer);
        } catch (TemplateException | IOException e) {
            String messsage = String.format("模板 process 失败 <%s> error<%s>", data, e);
            throw new RuntimeException(messsage, e);
        }
        return result.toString();
    }
}
```

