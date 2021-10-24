# Java-web学习笔记014-操作系统及数据库、安全

#### 操作系统、内存管理及垃圾回收

````
早期的电脑:
一个电脑上只能跑一个程序, 这个程序可以访问所有的物理硬件
一个电脑上能跑多个程序, 但是依赖程序自己让出控制权, 此时的程序还是可以访问所有的物理硬件

操作系统
1. 内存空间的虚拟化
2. cpu 的虚拟化

一个进程就是一个程序, 程序看起来就是自己独占整个电脑

好处:
不同程序无法互相干扰了

坏处:
切换程序的时候, 是有开销的, 这就是上下文切换

CPU, 内存

操作系统
--------
- 任务调度
    - 进程
        - 隔离内存
        - 通信麻烦
    - 线程
        - 线程池
            因为创建销毁线程是有开销的, 开销比较大
            所以就会事先创建好 N 个线程, 需要用的时候拿一个空的线程来用, 这就叫线程池
        - 调度最小单元
        - 通信方便
            同一进程的不同线程之间能共享数据
        - 多线程资源抢占
            需要原子操作来保证执行顺序
            比如用锁来保证, 抢到锁的才能执行下面的操作
        - 多线程死锁
            如果有两把锁, 存钱和取钱的双方取到两把锁后再执行操作,
            如果取锁顺序不一致, 那么就会导致双方互相各取到一把锁
            互相等待对方释放锁
            就会造成死锁
    - 栈和堆
        栈用来存函数调用时的变量
        堆用来存一些更长久的数据, 这些数据在函数调用结束后还会使用, 所以要在栈之外找个地方存放
    - IO 输入输出
    - 协程
        又有个名字就做用户态线程
        实际上就是在一个线程里面, 自己主动交出控制权给其他函数来执行
    - 并发并行
        并发就是一段时间内, 执行多个任务. 可以是先执行 A 再执行 B
        并行就是同一时间, 在一个确定的时间点上, 有多个任务在执行. 要求 A 和 B 必须同时运行
- 内存管理
    - 虚拟内存
    - 内存泄漏
        定时重启
        有些不能停机, 加钱, 买内存
    - 自动内存管理（GC 垃圾回收）
        - 循环引用
        - 非托管资源
        - 标记清除 分代回收

- 驱动程序（硬件管理）
    用来操作硬件的程序, 操作系统通过驱动来操作硬件
- 文件系统（冯诺依曼结构的电脑实际上可以不包含外存）
    控制信息(比如什么系统, 版本)
    索引(就是文件数据存放的地址)
    数据(文件数据真正存放的地方)
- 操作系统： 
    驱动（硬件的抽象）-》
    进程（操作系统的抽象，独立的资源和内存）-》
    线程 （更容易资源共享，更容易使用多核）    



自动回收
--------
```
class A{}

public class Main {
    public static void test() {
        A a1 = new A();
        A a2 = new A();
        A a3 = a1;
    }

    public static void main(String[] args) {
        test();

        int c = 5;
    }
}

```

垃圾回收的策略

早期策略

引用计数
每个对象实例, 有一个引用计数
每个变量被赋值这个对象的引用的时候,
计数器都会 + 1
当计数器到 0 的时候, 就会自动回收这个变量

引用计数会碰到循环引用这个问题, 那么循环引用的两个对象就无法被回收了

优点:
快, 夹杂在程序运行之中的

缺点:
无法检测出循环引用, 无法回收循环引用的对象


现在的策略
跟踪收集器(Tracing Collector)

把所有的引用对象当成一个树, 或者是图
从这个树的根节点(GC Roots)出发, 找出所有连接的树的节点
这些可以连接的对象, 就叫可达对象, 或者叫存活对象
其余对象就是不可达对象, 就是垃圾


怎么来回收垃圾
1. 标记-清理
标记了所有的存活对象, 和垃圾对象
然后再遍历一遍, 删除所有的垃圾所占的空间
简单方便, 但是会产生内存碎片


2. 标记-整理
标记了所有的存活对象的时候
把这些对象统一放到一个地方
这就没有内存碎片了
实质上就是主动进行碎片整理


3.复制
直接把堆拆成两部分
一段时间只用一部分
在垃圾回收的时候, 把存活对象赋值到另一部分的堆空间上
当前的内存就直接清空



分代收集算法
就是根据对象存活周期的不同
把内存分成几块, 不同区域的内存选择不同的垃圾回收算法

Java 的堆空间里面分成两大部分

1. 新生代
这块区域的对象特定就是, 存活对象少, 垃圾多

2. 老年代
存活时间比较长的对象
特定就是存活对象多, 垃圾少




下面的例子叫做循环引用
---------------------
```

class A{
    public A foo;

}

public class Main {
    public static void test() {
        A a = new A();
        A b = new A();
        
        a.foo = b;
        b.foo = a;
    }

    public static void main(String[] args) {
        test();
    }
}
```
````



#### 数据库的几个概念

````
1. 事务
希望几个 SQL 语句
要不就全部执行
要不就全部不执行


2. 联表查询

```
SELECT Topic.id as topic_id, Topic.title as topic_titile, TopicComment.id as comment_id, TopicComment.content as comment_title
From Topic, TopicComment
where Topic.id = 1 and Topic.id = TopicComment.topicId
```

等价于下面的写法

```

SELECT Topic.id as topic_id, Topic.title as topic_titile, TopicComment.id as comment_id, TopicComment.content as comment_title
From Topic inner join TopicComment
on Topic.id = TopicComment.topicId
where Topic.id = 1

```

新需求
希望 TopicComment 找不到数据, 但是 Topic 能找到数据的情况下
返回满足 Topic.id = 1 的 topic 数据

```
SELECT Topic.id as topic_id, Topic.title as topic_titile, TopicComment.id as comment_id, TopicComment.content as comment_title
From Topic left join TopicComment
on Topic.id = TopicComment.topicId
where Topic.id = 3
```

left join 就是 on 的条件不成立的时候, 根据 where 的条件, 仍然能返回数据
如果用 left join, 就会返回左表的数据, 右表没有数据, 就用 null 来填充

right join 反之


3. 外键
外键就是在两个表的两个字段A, B之间, 建立了一个约束关系
要求其中一个表的字段 A 的取值, 必须在另一个表的字段 B 的现有值之内
````

#### [xss攻击和csrf攻击的定义及区别](https://www.cnblogs.com/itsuibi/p/10752868.html)

1.CSRF的基本概念、缩写、全称

CSRF（Cross-site request forgery）：跨站请求伪造。

PS：中文名一定要记住。英文全称，如果记不住也拉倒。

2.CSRF的攻击原理

![img](https://img2018.cnblogs.com/blog/941968/201904/941968-20190422203527996-279231194.jpg)

用户是网站A的注册用户，且登录进去，于是网站A就给用户下发cookie。

从上图可以看出，要完成一次CSRF攻击，受害者必须满足两个必要的条件：

（1）登录受信任网站A，并在本地生成Cookie。（如果用户没有登录网站A，那么网站B在诱导的时候，请求网站A的api接口时，会提示你登录）

（2）在不登出A的情况下，访问危险网站B（其实是利用了网站A的漏洞）。

我们在讲CSRF时，一定要把上面的两点说清楚。

温馨提示一下，cookie保证了用户可以处于登录状态，但网站B其实拿不到 cookie。

3、CSRF如何防御

方法一、Token 验证：（用的最多）

（1）服务器发送给客户端一个token；

（2）客户端提交的表单中带着这个token。

（3）如果这个 token 不合法，那么服务器拒绝这个请求。

方法二：隐藏令牌：

把 token 隐藏在 http 的 head头中。

方法二和方法一有点像，本质上没有太大区别，只是使用方式上有区别。

方法三、Referer 验证：

Referer 指的是页面请求来源。意思是，只接受本站的请求，服务器才做响应；如果不是，就拦截。

XSS

1、XSS的基本概念

XSS（Cross Site Scripting）：跨域脚本攻击。

XSS的攻击原理

XSS攻击的核心原理是：不需要你做任何的登录认证，它会通过合法的操作（比如在url中输入、在评论框中输入），向你的页面注入脚本（可能是js、hmtl代码块等）。

最后导致的结果可能是：

盗用Cookie破坏页面的正常结构，插入广告等恶意内容D-doss攻击

XSS的攻击方式

1、反射型

发出请求时，XSS代码出现在url中，作为输入提交到服务器端，服务器端解析后响应，XSS代码随响应内容一起传回给浏览器，最后浏览器解析执行XSS代码。这个过程像一次反射，所以叫反射型XSS。

2、存储型存

储型XSS和反射型XSS的差别在于，提交的代码会存储在服务器端（数据库、内存、文件系统等），下次请求时目标页面时不用再提交XSS代码。

XSS的防范措施（encode + 过滤）

XSS的防范措施主要有三个：

1、编码：

对用户输入的数据进行

HTML Entity 编码。

如上图所示，把字符转换成 转义字符。

Encode的作用是将

$var

等一些字符进行转化，使得浏览器在最终输出结果上是一样的。

比如说这段代码：

<script>alert(1)</script>

若不进行任何处理，则浏览器会执行alert的js操作，实现XSS注入。

进行编码处理之后，L在浏览器中的显示结果就是

<script>alert(1)</script>

，实现了将$var作为纯文本进行输出，且不引起JavaScript的执行。

2、过滤：

移除用户输入的和事件相关的属性。如onerror可以自动触发攻击，还有onclick等。（总而言是，过滤掉一些不安全的内容）移除用户输入的Style节点、Script节点、Iframe节点。（尤其是Script节点，它可是支持跨域的呀，一定要移除）。

3、校正

避免直接对HTML Entity进行解码。使用DOM Parse转换，校正不配对的DOM标签。备注：我们应该去了解一下

DOM Parse

这个概念，它的作用是把文本解析成DOM结构。

比较常用的做法是，通过第一步的编码转成文本，然后第三步转成DOM对象，然后经过第二步的过滤。

还有一种简洁的答案：

首先是encode，如果是富文本，就白名单。

CSRF 和 XSS 的区别

区别一：

CSRF：需要用户先登录网站A，获取 cookie。XSS：不需要登录。

区别二：（原理的区别）

CSRF：是利用网站A本身的漏洞，去请求网站A的api。XSS：是向网站 A 注入 JS代码，然后执行 JS 里的代码，篡改网站A的内容。





#### [SQL注入详解](https://www.cnblogs.com/myseries/p/10821372.html)

**一：什么是sql注入**

　　SQL注入是比较常见的网络攻击方式之一，它不是利用操作系统的BUG来实现攻击，而是针对程序员编写时的疏忽，通过SQL语句，实现无账号登录，甚至篡改数据库。

------

 二：**SQL注入攻击的总体思路**　

　　1：寻找到SQL注入的位置

　　2：判断服务器类型和后台数据库类型

　　3：针对不同的服务器和数据库特点进行SQL注入攻击

------

 三：**SQL注入攻击实例**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
String sql = "select * from user_table where username=
' "+userName+" ' and password=' "+password+" '";

--当输入了上面的用户名和密码，上面的SQL语句变成：
SELECT * FROM user_table WHERE username=
'’or 1 = 1 -- and password='’

"""
--分析SQL语句：
--条件后面username=”or 1=1 用户名等于 ” 或1=1 那么这个条件一定会成功；

--然后后面加两个-，这意味着注释，它将后面的语句注释，让他们不起作用，这样语句永远都--能正确执行，用户轻易骗过系统，获取合法身份。
--这还是比较温柔的，如果是执行
SELECT * FROM user_table WHERE
username='' ;DROP DATABASE (DB Name) --' and password=''
--其后果可想而知…
"""
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

**四：如何防御SQL注入**

　　注意：但凡有SQL注入漏洞的程序，都是因为程序要接受来自客户端用户输入的变量或URL传递的参数，并且这个变量或参数是组成SQL语句的一部分，对于用户输入的内容或传递的参数，我们应该要时刻保持警惕，这是安全领域里的「外部数据不可信任」的原则，纵观Web安全领域的各种攻击方式，大多数都是因为开发者违反了这个原则而导致的，所以自然能想到的，就是从变量的检测、过滤、验证下手，确保变量是开发者所预想的。

　　**1、检查变量数据类型和格式**

　　如果你的SQL语句是类似where id={$id}这种形式，数据库里所有的id都是数字，那么就应该在SQL被执行前，检查确保变量id是int类型；如果是接受邮箱，那就应该检查并严格确保变量一定是邮箱的格式，其他的类型比如日期、时间等也是一个道理。总结起来：只要是有固定格式的变量，在SQL语句执行前，应该严格按照固定格式去检查，确保变量是我们预想的格式，这样很大程度上可以避免SQL注入攻击。
　　比如，我们前面接受username参数例子中，我们的产品设计应该是在用户注册的一开始，就有一个用户名的规则，比如5-20个字符，只能由大小写字母、数字以及一些安全的符号组成，不包含特殊字符。此时我们应该有一个check_username的函数来进行统一的检查。不过，仍然有很多例外情况并不能应用到这一准则，比如文章发布系统，评论系统等必须要允许用户提交任意字符串的场景，这就需要采用过滤等其他方案了。

　　**2、过滤特殊符号**

　　对于无法确定固定格式的变量，一定要进行特殊符号过滤或转义处理。

　　**3、绑定变量，使用预编译语句**　　

　　MySQL的mysqli驱动提供了预编译语句的支持，不同的程序语言，都分别有使用预编译语句的方法

　　实际上，绑定变量使用预编译语句是预防SQL注入的最佳方式，使用预编译的SQL语句语义不会发生改变，在SQL语句中，变量用问号?表示，黑客即使本事再大，也无法改变SQL语句的结构

------

 **五：什么是sql预编译**

　　1.1：预编译语句是什么　

　　通常我们的一条sql在db接收到最终执行完毕返回可以分为下面三个过程：

1. 1. 　　词法和语义解析
   2. 　　优化sql语句，制定执行计划
   3. 　　执行并返回结果

　　我们把这种普通语句称作Immediate Statements。　　

　　但是很多情况，我们的一条sql语句可能会反复执行，或者每次执行的时候只有个别的值不同（比如query的where子句值不同，update的set子句值不同,insert的values值不同）。
　　如果每次都需要经过上面的词法语义解析、语句优化、制定执行计划等，则效率就明显不行了。

　　所谓预编译语句就是将这类语句中的值用占位符替代，可以视为将sql语句模板化或者说参数化，一般称这类语句叫Prepared Statements或者Parameterized Statements
　　预编译语句的优势在于归纳为：一次编译、多次运行，省去了解析优化等过程；此外预编译语句能防止sql注入。
　　当然就优化来说，很多时候最优的执行计划不是光靠知道sql语句的模板就能决定了，往往就是需要通过具体值来预估出成本代价。

　　1.2：MySQL的预编译功能

　　**注意MySQL的老版本（4.1之前）是不支持服务端预编译的，但基于目前业界生产环境普遍情况，基本可以认为MySQL支持服务端预编译。**

　　下面我们来看一下MySQL中预编译语句的使用。
　　（1）建表 首先我们有一张测试表t，结构如下所示：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
mysql> show create table t\G
*************************** 1. row ***************************
       Table: t
Create Table: CREATE TABLE `t` (
  `a` int(11) DEFAULT NULL,
  `b` varchar(20) DEFAULT NULL,
  UNIQUE KEY `ab` (`a`,`b`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

　（2）编译

　　我们接下来通过 `PREPARE stmt_name FROM preparable_stm`的语法来预编译一条sql语句

```
mysql> prepare ins from 'insert into t select ?,?';
Query OK, 0 rows affected (0.00 sec)
Statement prepared
```

　（3）执行

　　我们通过`EXECUTE stmt_name [USING @var_name [, @var_name] ...]`的语法来执行预编译语句

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
mysql> set @a=999,@b='hello';
Query OK, 0 rows affected (0.00 sec)
 
mysql> execute ins using @a,@b;
Query OK, 1 row affected (0.01 sec)
Records: 1  Duplicates: 0  Warnings: 0
 
mysql> select * from t;
+------+-------+
| a    | b     |
+------+-------+
|  999 | hello |
+------+-------+
1 row in set (0.00 sec)
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

　　可以看到，数据已经被成功插入表中。

　　MySQL中的预编译语句作用域是session级，但我们可以通过max_prepared_stmt_count变量来控制全局最大的存储的预编译语句。

```
mysql> set @@global.max_prepared_stmt_count=1;
Query OK, 0 rows affected (0.00 sec)
 
mysql> prepare sel from 'select * from t';
ERROR 1461 (42000): Can't create more than max_prepared_stmt_count statements (current value: 1)
```

当预编译条数已经达到阈值时可以看到MySQL会报如上所示的错误。

  （4）释放
　　如果我们想要释放一条预编译语句，则可以使用`{DEALLOCATE | DROP} PREPARE stmt_name`的语法进行操作:

```
mysql> deallocate prepare ins;
Query OK, 0 rows affected (0.00 sec)
```

**六：为什么PrepareStatement可以防止sql注入**

　　原理是采用了预编译的方法，先将SQL语句中可被客户端控制的参数集进行编译，生成对应的临时变量集，再使用对应的设置方法，为临时变量集里面的元素进行赋值，赋值函数setString()，会对传入的参数进行强制类型检查和安全检查，所以就避免了SQL注入的产生。下面具体分析

　（1）：为什么Statement会被sql注入

　　因为Statement之所以会被sql注入是因为SQL语句结构发生了变化。比如：

```
"select*from tablename where username='"+uesrname+  
"'and password='"+password+"'"
```

　　在用户输入'or true or'之后sql语句结构改变。

```
select*from tablename where username=''or true or'' and password=''
```

　　这样本来是判断用户名和密码都匹配时才会计数，但是经过改变后变成了或的逻辑关系，不管用户名和密码是否匹配该式的返回值永远为true;

　（2）为什么Preparement可以防止SQL注入。

　　因为Preparement样式为

```
select*from tablename where username=? and password=?
```

　　该SQL语句会在得到用户的输入之前先用数据库进行预编译，这样的话不管用户输入什么用户名和密码的判断始终都是并的逻辑关系，防止了SQL注入

　　简单总结，参数化能防注入的原因在于，语句是语句，参数是参数，参数的值并不是语句的一部分，数据库只按语句的语义跑，至于跑的时候是带一个普通背包还是一个怪物，不会影响行进路线，无非跑的快点与慢点的区别。

------

 **七：mybatis是如何防止SQL注入的**　　

### 　　1、首先看一下下面两个sql语句的区别：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<select id="selectByNameAndPassword" parameterType="java.util.Map" resultMap="BaseResultMap">
select id, username, password, role
from user
where username = #{username,jdbcType=VARCHAR}
and password = #{password,jdbcType=VARCHAR}
</select>
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<select id="selectByNameAndPassword" parameterType="java.util.Map" resultMap="BaseResultMap">
select id, username, password, role
from user
where username = ${username,jdbcType=VARCHAR}
and password = ${password,jdbcType=VARCHAR}
</select>
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

**mybatis中的#和$的区别：**

　　1、#将传入的数据都当成一个字符串，会对自动传入的数据加一个双引号。
如：where username=#{username}，如果传入的值是111,那么解析成sql时的值为where username="111", 如果传入的值是id，则解析成的sql为where username="id".　
　　2、$将传入的数据直接显示生成在sql中。
如：where username=${username}，如果传入的值是111,那么解析成sql时的值为where username=111；
如果传入的值是;drop table user;，则解析成的sql为：select id, username, password, role from user where username=;drop table user;
　　3、#方式能够很大程度防止sql注入，$方式无法防止Sql注入。
　　4、$方式一般用于传入数据库对象，例如传入表名.
　　5、一般能用#的就别用$，若不得不使用“${xxx}”这样的参数，要手工地做好过滤工作，来防止sql注入攻击。
　　6、在MyBatis中，“${xxx}”这样格式的参数会直接参与SQL编译，从而不能避免注入攻击。但涉及到动态表名和列名时，只能使用“${xxx}”这样的参数格式。所以，这样的参数需要我们在代码中手工进行处理来防止注入。**
【结论】在编写MyBatis的映射语句时，尽量采用“#{xxx}”这样的格式。若不得不使用“${xxx}”这样的参数，要手工地做好过滤工作，来防止SQL注入攻击。**

### mybatis是如何做到防止sql注入的

　　MyBatis框架作为一款半自动化的持久层框架，其SQL语句都要我们自己手动编写，这个时候当然需要防止SQL注入。其实，MyBatis的SQL是一个具有“**输入+输出**”的功能，类似于函数的结构，参考上面的两个例子。其中，parameterType表示了输入的参数类型，resultType表示了输出的参数类型。回应上文，如果我们想防止SQL注入，理所当然地要在输入参数上下功夫。上面代码中使用#的即输入参数在SQL中拼接的部分，传入参数后，打印出执行的SQL语句，会看到SQL是这样的：

```
select id, username, password, role from user where username=? and password=?
```

　　不管输入什么参数，打印出的SQL都是这样的。这是因为MyBatis启用了预编译功能，在SQL执行前，会先将上面的SQL发送给数据库进行编译；执行时，直接使用编译好的SQL，替换占位符“?”就可以了。因为SQL注入只能对编译过程起作用，所以这样的方式就很好地避免了SQL注入的问题。

　　【底层实现原理】MyBatis是如何做到SQL预编译的呢？其实在框架底层，是JDBC中的PreparedStatement类在起作用，PreparedStatement是我们很熟悉的Statement的子类，它的对象包含了编译好的SQL语句。这种“准备好”的方式不仅能提高安全性，而且在多次执行同一个SQL时，能够提高效率。原因是SQL已编译好，再次执行时无需再编译

 

资料：https://www.cnblogs.com/shenbuer/p/7875419.html

　　http://www.cnblogs.com/mmzs/p/8398405.html
