
#Java-web学习笔记

##前置条件
- 64 位 Windows 10 操作系统, 或者Mac 10.12 或以上
- Intelli IDEA
- Chrome 浏览器
- 安装JAVA ,JDK，配置环境变量

所有步骤均可网上搜索，自行解决，不再赘述。

##养成好习惯
- 缩进：
	- 一个层级统一缩进为 4 个空格
- 空格：
	- 操作符左右各加一个空格
	- 以逗号(,) 分隔的逗号后面加上空格，比如函数的参数
	- if, while, for 后面需要加上空格
	- 左花括号({) 前面需要加上空格
	- else 前面需要加上空格, 且 else 前面不能换行
- 换行：
	- else 不要换行，要跟在 } 后面
- 大括号
	- 每个 if / else if / else 都要加上 {}
	
- 代码格式化自检条目:
	- 每行只写一条语句
	- 函数之间空两行
	- 运算符的左右各加 1 个空格
	- 逗号前无空格，后面 1 个空格
	- 冒号前无空格，字典中的冒号后有 1 个空格
	- 参数默认值的等号左右不加空格


## 变量名命名规则（函数名也是变量名）
- 变量名只能包括 字母（汉字名也算字母），下划线(_)，美元符号（$）
- 变量名不能以数字开头
- 变量名大小写敏感（a A 是两个不同的变量）
- 只有在现代的 Java 中，才能使用汉字作为合法的变量名
- 变量名不能是 Java 的保留字（while public 这样的就是保留字
- 还有很多保留字


## 数据类型
程序中会有各种不同的数据类型, 有的是 java 自带的, 有的是用户自定义的
java 语言提供了多种常用基本类型,先学以下5种
- char: 字符, 可以放一个字符
- int: 整型, 就是放整数
- float: 单精度浮点数, 就是小数
- double: 双精度浮点数, 也是小数, 能表示更大范围的数
- boolean: 布尔类型, 也就是布尔值, 表示 真 或者 假

```Java
int x = 10;
double y = 10.1;
char c = 'a';
String s = "hello";
System.out.println(String.format("x = %d", x));
System.out.println(String.format("x = %d, y = %f", x, y));
System.out.println(String.format("c = %c, s = %s", c, s));

// 因为 System.out.println(String.format()) 这段代码太长
// 所以我们会自己实现一个 log 函数, 来简化「输出」的操作
private static void log(String format, Object... args) {
    System.out.println(String.format(format, args));
}
// 之后就可以通过下面的代码来打印数字, 字符
log("更简单的 log");
log("c = %c, s = %s", c, s);
```

## 选择控制

```Java
// if语句可以根据情况选择性的执行某些语句
//例子如下
int grade = 3;
if (grade < 7) {
    // 这句 log 只在 grade 小于 7 这个条件满足的情况下会执行
    log("这是小学生");
}

// 选择控制用多种不同的用法
// 只有 if
if (grade > 2) {
    log("条件成立");
}

// if 带 else
// if else 必定会执行一个语句
if (grade > 2){
    log("条件成立");
} else {
    log("条件不成立");
}

// 多个 if else
int grade = 8;
if (grade < 7) {
    log("小学生");
} else if (grade < 10) {
    log("初中生");
} else {
    log("高中生");
}
  
// 求绝对值
int n1 = -1;
if (n1 < 0){
    n1 = -n1;
}

// 判断奇偶
// % 是求模运算符(取余数)
int n2 = -1;
if (n2 % 2 == 0){
    log("偶数");
} else {
    log("奇数");
}
```

## 比较运算和逻辑运算
if 判断的条件(括号里的表达式)其实是一个值, 这种值叫 布尔值(Boolean)
这种值只有两种结果, 一个是真, 一个是假
在 java 语言 中, 这两种值分别是 true 和 false
1 > 2 实际上是一个值, false
if 是根据这个值来决定执行的语句的

一共有 5 个常用比较运算
分别是
相等, 不等, 小于, 大于, 小于等于, 大于等于
==, !=, <, >, <=, >=

除了比较运算符, 还有三种逻辑操作
与, 或, 非
在 java 语言中分别是
&&, ||, !

用到的地方很广, 比如登录网站的时候, 服务器做如下判断

```Java
if (用户名存在 && 密码验证成功) {
    登录成功
} else {
    登录失败
}

// 注意
// 比较运算和逻辑操作的结果是 Boolean 类型, 结果是 true 和 false
// true 就是真
// false 就是假
```

##函数
- 用来消除大段重复代码
- 主流的语言都有函数这个特性
- 函数就是一段代码的集合, 用于重复使用一段代码

所以发现一段代码有冗余的情况，则思考如何重构。
将重复内容提取出来，单独写一个函数。

```Java
public static void 函数名() {
    子函数1()
    子函数2()
	子函数3()
    子函数4()
	...
}
```

- 函数返回值

函数不仅可以合并操作重复性的代码
还可以通过计算得到一个结果, 结果就是返回值
函数可以有「返回值」
返回值的意思是函数调用的结果是一个值, 可以被赋值给变量

## 标准库
库就是指就是一系列函数的集合
标准库就是语言自带的库

// java 语言中引入标准库用的是直接使用 Math 库
// 比如在代码中直接使用 Math.PI 就可以获取到一个圆周率的数字
double pi = Math.PI;

在 Java 中使用标准库(或者其他库) 必须使用全名, 包括包的名字
比如我要用一个类型叫 BigInteger, 它放在 java.math 这个包下面
BigInterger 是一个能容纳更大数字的 int 类型
就要写
java.math.BigInteger n = java.math.BigInteger.valueOf(123)

如果我们要简化命名
就写一个
import java.math.BigInteger;

然后就能用
BigInteger n = 123123;

但是如果是 java.lang 这个包里面的东西, 比如 Math 库, 我们就不用写全称了, 可以直接写
Math.PI
System.out.print(Math.PI);
这是圆周率  Math.PI

## debug

程序一般会出现两种错误：
- 语法错误, 程序没法运行
- 逻辑错误, 程序运行了, 但是结果不是想要的

log 方法
使用 log 函数, 让程序的运行过程显形，一般来解决逻辑错误
debug 过程:
- 通过 log, 确定函数是否被调用
- 通过 log, 确定函数参数是我们想要的值

log 大法的两个作用
- 把程序的运行过程显示出来
- 把变量显示出来


