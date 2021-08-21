
# 面向对象

- 类 与 面向对象

语言自带的数据类型有限, 要表示复杂的数据, 必须有复杂的数据类型
我们可以用 HashMap 表示复杂类型
但 Java 提供一种机制被称之为 面向对象(Object Oriented)

Java 面向对象编程中的 类, 就是语言提供的自定义数据类型的机制
它可以比 HashMap 更方便地表示复杂类型, 并能实现更多功能
例子如下

类相当于一个蓝图
生成出来的产品就叫实例
不同的实例, 属性的值不同

public 修饰符就是公共修饰符，访问级别 : 无处不在，可以从类内部，类外部，包内部和包外部访问它。
class 是用来声明 类 的特殊语法
Student 是类名, 一般首字母大写(驼峰命名法)
先看如何使用再看定义方法, 这是一个创建类的标准方法

******

```Java
private static void log(String format, Object... args) {
    System.out.println(String.format(format, args));
}
```

```Java
public class Student {
    // 定义类的变量
    public String name;
    public double height;
    // 定义一个函数, 类的函数叫 方法 (函数和方法的区别, 单纯只是名字不同)
    // 这个函数和类名同名, 被称作构造函数
    public Student(String name, double height) {
        System.out.println("程序运行的时候会运行这里");
        // 用 this.变量名 来创造一个只有类的实例才能访问的变量
        // 这句话之后会解释
        this.name = name;
        this.height = height;
    }
}
```
1. 
// 对 类 调用得到一个「类的实例」
// 赋值给变量 s1
// 这时候 s1 引用的是一个 Student 类型(也就是对象 Student 的实例)
// 也称之为 对象
Student s1 = new Student("usopp", 169.0);

2. 
// 可以通过 . 语法访问对象的属性(也就是类开始时用创造的变量)
// 类的变量叫做 属性(单纯只是名字不同)
log("class, s1 %s %f", s1.name, s1.height);
// 输出如下
// class, s1 usopp 169.0

3. 
// 可以改变 s1 的属性值
s1.name = "xiaousopp";
s1.height = 1.69;
log("class, s1 属性 %s %f", s1.name, s1.height);
// 输出如下
// class, s1 属性 xiaousopp 1.690000

4. 
// 可以创建多个互相独立的实例
// 下面的例子可以看到, s2 和 s3 是互相独立的
Student s2 = new Student("usopp II", 169);
Student s3 = new Student("三代瓜", 169);
log("%s

5. 
// static
不加 static 修饰的方法, 只能在实例上使用
加 static 修饰的方法, 能直接用 类名.方法名() 这种形式调用
加 static 修饰的方法, 内部不能用 this 关键字

6. 
// 可以给类增加一些方法(函数)

```Java
public class StudentInfo {
    // 定义类的变量
    public String name;
    public double height;
    public StudentInfo(String name, double height) {
        this.name = name;
        this.height = height;
    }
    public void show() {
        // 这里可以使用 log
        System.out.println(String.format("student info %s %f", this.name, this.height));
    }
    public void update(String name, double height) {
        this.name = name;
        this.height = height;
    }
}
```

// 初始化
StudentInfo info = new StudentInfo("usopp", 123);

// 调用 info 的 show 方法
// 注意, show 的第一个参数 self 是不用传递的
// Java 自动帮你传递第一个参数
// 下面这句实际上相当于 StudentInfo.show(info)
// 这是一个 Java 提供的方便书写的语法(这种我们称之为 语法糖)
StudentInfo info = new StudentInfo("usopp", 123);
info.show();

// 调用 info 的 update 方法
// 也是不用传递 self 的
info.update("xiao", 169.98);
info.show();

// ==
// 封装, 上面 show 和 update 就是封装的例子
// 意思是说把一些操作做好, 对外部来说只需要简单调用即可
//


// =
// 继承
// 继承是说, 父类的东西你可以直接拿来用
```Java
public class Phone {
    public double price;
    public void off() {
        System.out.println("关机, 手机");
    }
    public void on() {
        System.out.println("开机, 手机");
    }
}
```

// 注意, AnZhuo 类继承自 Phone 类, 写法如下
// 类如果没有写 extents, 则默认继承 object 类

```Java
public class AnZhuo extends Phone{
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
    public void on() {
        System.out.println("安卓, 开机");
    }
}
```

// 初始化并调用
Phone p = new Phone();
p.on();
p.off();


AnZhuo a =  new AnZhuo();

// 因为 AnZhuo 实现了自己的 on 所以这里调用的是自己的
a.on()

// 下面的这句 a.off() 能调用成功, 虽然 AnZhuo 类没有实现 off() 方法
// 但是 Java 会自动在父类中查找 off() 方法
// off 是继承自父类的方法, 所以被调用了
a.off()


// =
// 类主要的优势就是 Hashmap 的替代品 和 可封装方法
// 其他上课要讲的杂项


// 多态
不同的对象, 调用同一个方法, 行为不同
1. 继承
确保两个对象用同样的方法
2. 重写
确保同一个方法, 不同对象的行为不同
@Override
如果加上的话, 会去检查父类是否有同名方法, 如果没有就报错了
让程序更加严谨


// compare 可以接受 Phone 类型的参数和它的子类型, 比如 Anzhuo
```Java
public static void compare(Phone a, Phone b) {
    a.on();
    b.on();
}

Anzhuo an1 = new Anzhuo();
Anzhuo an2 = new Anzhuo();
compare(an1, an2);

```

// 正确
Phone a = new Anzhuo();
// 错误
Anzhuo an1 = new Phone();

// 加上类型转换
Anzhuo an1 = (Anzhuo) new Phone();

函数签名
方法名, 参数列表(包括参数类型), 返回值(包括类型)

# 方法重载

// 一个类中定义了多个函数 / 方法, 函数参数数量不同 / 类型不同等, 就称作方法重载

// 见下面的例子

```Java
// 我们实现了 3 个 add 函数, 但是函数参数的类型和返回值类型都不同
public static int add(int a, int b) {
    // 返回两个整型数的和
    return a + b;
}
public static double add(double a, double b) {
    // 返回两个浮点数的和
    return a + b;
}
public static String add(String a, String b) {
    // 返回字符串的拼接
    return a + b;
}

public static void main(String[] args) {
    // 调用的时候三个函数参数分别是相对应的类型
    log("重载 %d %f %s", add(1, 2), add(0.1, 0.2), add("name: ", "gua"));
}
```


# args (多参数)

// 在函数有多个参数的时候, 或者接受无限参数的时候
// Java 提供便捷的处理方法, 就是 args 多参数

public static double addAll(double a, double b, double c, double d, double e, double f, double g) {
    // 函数接受 7 个 int 参数并且返回 它们的和
    return a + b + c + d + e + f + g;
}


// 调用方式如下
double sum = addAll(1, 2, 3, 4, 5, 6, 7);
log("sum is %f", sum);

// 参数过多, 调用起来很麻烦
// 简便的方式是通过一个 array 装参数然后传给函数
// Java 默认支持多参数传递
// 当函数声明的函数参数有 类型... 的时候
// 传入的参数会被解开分别传递给函数

```Java
public static double addAll2(double... args) {
    double sum = 0;
    for (int i = 0; i < args.length; i++) {
        sum += args[i];
    }
    return sum;
}
double[] a = {1, 2, 3, 4, 5, 6, 7};
log("sum 2 is %f", addAll2(a));
```Java

// 在这个例子中, addAll2 接受 1 个参数 args
// 参数 double... args 就是把 args 拆成独立的参数病分别传给 addAll2

// 输出如下
// sum is 28.0
// sum 2 is 28.0

// arg 还可以用作定义函数时的参数表示
// 方便函数的处理
// 请注意, args 中的 args 可以是任意变量名, 我们只是因为习惯所以使用 args 这个名字
```Java
public static double addAll2(double... args) {
    double sum = 0;
    for (int i = 0; i < args.length; i++) {
        sum += args[i];
    }
    return sum;
}
```

// 然后这样调用, 无论你传多少参数都会被视为是一个数组
log("sum 2 is %f", addAll2(1, 2, 3, 4, 5, 6, 7));
log('add numbers', add_numbers(1, 2, 3, 4, 5))

// 或者继续刚才那样调用也是可以的
double[] a = {1, 2, 3, 4, 5, 6, 7};
log("sum 2 is %f", addAll2(a));


# 泛型

泛型的用途
- 简化重载方法, 少写很多代码
- 保证类型安全, 避免类型错误


1. 泛型方法
// 这是一个泛型方法
// T 叫做类型参数
public static <T> boolean compare(T a, T b) {
   return a.equals(b);
}

使用方法如下
compare(s1, s1);

没有泛型之前, compare 这么写

public static boolean compare(Object a, Object b) {
    return a.equals(b);
}


public static Object compare(Object a, Object b) {
    return a;
}
// 如果返回一个 Object, 需要做类型转换
Integer r = (Integer) compare(a1, a2);

2. 泛型类
// 这是一个泛型类
class MyArrayList <T>{
    T[] data;

    MyArrayList(T[] data) {
        this.data = data;
    }
}

调用方法如下
String[] data = {"hello", "world"};
MyArrayList<String> a = new MyArrayList<String>(data);

