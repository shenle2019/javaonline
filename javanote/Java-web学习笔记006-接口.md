# Java-web学习笔记006

## Java接口

一个类只能继承一个类, 只能拥有这个类里面的方法
如果想要指定某个类拥有多个特定的方法
就要用到接口

- 接口的定义如下
// interface 和 class 一样, 是一个关键字
interface PowerOn {
    void on();
}


// Phone 实现了 PowerOn 接口, 用 implements
class Phone implements PowerOn {
    // 实现 PowerOn 接口, 必须在类里面把 on 的具体函数定义写出来
    @Override
    public void on() {
        System.out.println("开机");
    }
}


- 一个类可以实现多个接口

interface PowerOff{
    void off();
}

// Phone 类实现了 PowerOn 和 PowerOff 两个接口
// 在 Phone 类里面, 就要写出 on 和 off 函数的具体定义和内容
class Phone implements PowerOn, PowerOff {
    @Override
    public void on() {
        System.out.println("开机");
    }


    @Override
    public void off() {
        System.out.println("关机");
    }
}


// 如果 Phone 实现了一个 PowerOn 接口
// 那么就能用 PowerOn 类型的变量来接住 Phone 类型的变量

PowerOn p1 = new Phone();

// 但是接口不能用 new 来初始化一个 PowerOn 的实例
// 如下写法是错的
PowerOn p1 = new PowerOn();

// 因为单纯的一个接口, 是没有函数的具体实现的, 是不完整的



- 面向接口编程

// 假设有一个乘客类
// 乘客在飞机起飞的时候要把所有能关闭的设备都关闭

1. 普通的写法
在 Passenger 类里面给每个设备写一个 takeoff

class Anzhuo extends Phone {
    @Override
    public void off() {
        System.out.println("Anzhuo 开机");

    }
}

class iPhone extends Phone {
    @Override
    public void off() {
        System.out.println("iPhone 开机");
    }
}

class Passenger {
    public void takeoffAnzhuo(Anzhuo equipment) {
        equipment.off();
    }

    public void takeoffiPhone(iPhone equipment) {
        equipment.off();
    }
}

// 使用时
Passenger s1 = new Passenger();
Anzhuo anzhuo = new Anzhuo();
iPhone iPhone = new iPhone();
s1.takeoffAnzhuo(anzhuo);
s1.takeoffiPhone(iPhone);


2. 面向接口的解法
// takeoff 方法接受一个 PowerOff 类型的参数
// 所有实现了 PowerOff 接口的类型都能往里面传
class Passenger {
    public void takeoff(PowerOff equipment) {
        equipment.off();
    }
}

Passenger s1 = new Passenger();
Anzhuo anzhuo = new Anzhuo();
iPhone iPhone = new iPhone();
s1.takeoff(anzhuo);
s1.takeoff(iPhone);

依赖倒置本质就是面向接口编程
以前我听你, 我依赖你
现在你听我, 你依赖我

面向接口编程就是你发布接口规范
人家按照你的规范来写


3. 更简洁的面向接口写法

每次有一个新设备, 就要新建一个类, 再写具体的方法太麻烦了
我们可以用两种方式来省略建新的类, 直接写我们想实现的方法

/*
1. 匿名类
传参数的时候, new 一个类, 在类里面写好想实现的函数
注意, 这里并不是 new 了一个 PowerOff 类, PowerOff 是一个接口, 不能 new
而是 new 里一个新的类的实例, 但是省略了类的名字, 只剩下了接口的名字

*/
s1.takeoff(new PowerOff() {
    @Override
    public void off() {
        System.out.println("匿名类 关闭所有设备");
    }
});

/*

2. lambda 表达式
直接把实现好的方法传进去

() -> {
    System.out.println("lambda 关闭所有设备");
}

上面就是实现好的方法

Java 会自己创建一个实现了 PowerOff 接口的类, 把实现好的方法给该类
然后 new 一个该类的实例, 传给 takeoff 方法
*/
s1.takeoff(() -> {
    System.out.println("lambda 关闭所有设备");
});

## MVC概念

MVC
Model, View, Controller
- Model：数据模型, Service 和 Model
- View： 视图, HTML 页面
- Controller: 控制器, Server 和 Route


## Server做的事情：
1. 接受请求
    在 Request 类当中, 解析字符串的请求
    得到参数, 如果是 GET 请求, 解析 url 里面的参数, 放到 query 字典中
    如果是 POST 请求, 解析 http 请求 body 中的参数, 放到 form 字典当中

2. 分发请求
    根据 http 请求的 path 参数, 决定哪个路由函数来处理请求
    留言板功能中, routeMessage 路由函数来处理请求

3. 处理请求
    在路由函数里面, 根据请求的 method 来进行处理
        如果是 POST 请求, 去 request 的 form 属性里面, 拿到参数字典
        如果是 GET 请求, 去 request 的 query 属性出门, 拿到参数字典

    拿到参数字典之后
        如果没有参数, 说明只是一个请求页面的请求, 直接读取页面, 返回
        如果有参数, 说明是一个添加数据的请求, 让 Model 层来处理请求

     Model 层是怎么处理请求的
        根据 http 请求的参数, new 一个数据实例
        把数据实例存到存储中(内存或者txt文件)

4. 返回响应
    在路由函数里面
    读取 html 页面
    在 html 页面中, 用字符串替换的方式, 插入留言数据
    拼接 http 的 header 和 html 页面
    返回二进制的响应




