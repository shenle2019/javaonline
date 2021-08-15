# Java-web学习笔记002

## array（数组）

```Java
private static void log(String format, Object... args) {
    System.out.println(String.format(format, args));
}
```

- 创建数组
// 使用 {} 符号, 每个元素用逗号分隔
int a[] = {3, 5, 8, 1};
// 现在 a 是一个 array, 拥有 4 个元素, 每个元素都是数字类型

// 创建一个空的数组, 可以容纳 100 个元素
int a2[] = new int[100];

- 获取长度
可以用 .length 的方式得到 array 的长度
log("求 array 长度 %d", a.length)
// 使用 .length 可以求出数组的长度（数组中元素的个数）
// 值可以用变量接住
int length = a.length;
log("数字长度为 %d", length)

- 访问元素
// 对于数组中的每个元素, 可以通过下标（就是元素在数组中的序号, 从 0 开始）访问
// 下标访问语法是 [] 中括号
log("用下标访问 array 中的元素");
log("a[0] %d", a[0]);
log("a[1] %d", a[1]);
log("a[2] %d", a[2]);
log("a[3] %d", a[3]);
// 因为一共只有 4 个元素, 所以访问不存在的下标会报错
// log("报错行", a[4]);

/ 手动访问元素当然是低效的
// 可以用循环来访问元素, 这个过程叫 遍历

log("循环访问 array 所有元素");
for (int i = 0; i < a.length; i++) {
    log("element %d", a[i]);
}

// 上面的循环等价于下面的 while
int i = 0;
while (i < a.length) {
    log("while 循环的 element %d", a[i]);
    i += 1;
}

// java 的 array 在初始化后, 数组的长度就不能修改了
// 所以 array 并不能简单地添加、删除元素

//小练习, 得到列表中最小的元素
```Java
int[] a1 = {3, 9, 2, -10, 8};
int min = a1[0];
for (int i = 0; i < a1.length; i++) {
    int n = a1[i];
    if (n < min) {
        min = n;
    }
}
log("最小值是 %d", min);
```



## 字符串
// 字符串的操作
// 字符串可以判断相等、可以相加
// 例子
log("判断相等或者包含");
log("结果 1 %b", "good" == "good");
log("结果 2 %b", "good" == "bad");
log("结果 3 %b", "good" != "bad");

// 拼接得到一个新字符串
log("拼接结果 1 %s", "very" + "good");
log("拼接结果 2 %s", "very " + "good");

// 得到一个你想要的字符串有多种方式
// 但是现在有现代的方式, 模板字符串
String name = "usopp";
String hello = String.format("%s, 你好", name);
log("%s", hello);
// 简单说来, 就是 %s 会被变量 user 替换形成新字符串


// 字符串相当于一个 array, 可以用下标访问
// 看例子, 看结果
// 下面字符串 s 的长度是 7, 但是下标是从 0 开始的, 所以最大下标是 6
String s = 'iamgood';
String s = "iamgood";
log("s[0] %s", s.charAt(0));
log("s[1] %s", s.charAt(1));
log("s[2] %s", s.charAt(2));
// ...
log("s[6] %s", s.charAt(6));
//
// 当然也就可以和 array 一样用循环遍历了


// 字符串不能使用下标来赋值
// 只能拼接起来生成一个新的字符串
String name = 'usopp'

// name.charAt(0) 是 'u'


// 字符串可以用 .substring 函数切片（取子字符串）
// 语法如下
// s.substring(开始下标, 结束下标)
log("slice 0 3 %s", s.substring(0, 3));  // 'iam'
log("slice 1 3 %s", s.substring(1, 3));  // 'am'

// 可以省略结束下标参数, 表示取到底
log("slice 0 %s", s.substring(3));      // 'good'


## break 语句
```Java
// break 语句可以终止循环
private static void breakStatement() {
    int i = 0;
    while (i < 10) {
        log("while 中的 break 地址");
        // break 语句执行后, 循环就结束了
        break;
        // 因此 i += 1 这一句是不会被执行的
        // i += 1;
    }
    log("break 结束的 i 值 %d", i);
}
```
// 运行
breakStatement();
// 输出如下
// while 中的 break 语句
// break 结束的 i 值 0
//
// 可以看到
// 循环执行了一次就结束了
// i 仍然是 0, 说明 break 后的语句的确没有被执行


## continue 语句

```Java
// continue 语句可以跳过单次循环
private static void continueStatement() {
    int i = 0;
    while(i < 10) {
        // 一定要记住改变循环条件，否则会无限循环
        i += 1;
        // 如果 i 是偶数，则 continue 跳过这次循环
        if (i % 2 == 0) {
            continue;
        }
        log("while 中的 continue 语句 %d", i);
    }
}
```

// 运行
continueStatement()
// 输出如下
// while 中的 continue 语句 1
// while 中的 continue 语句 3
// while 中的 continue 语句 5
// while 中的 continue 语句 7
// while 中的 continue 语句 9
//
// 可以看到
// 只有 1 3 5 7 9 被 log 出来了
// 因为 i 是偶数的时候, 循环体中剩下的部分被跳过了


## ArrayList
```Java
public static void demoArrayList() {
    // 新建一个 ArrayList, 里面存放的是 String 类型的元素
    ArrayList<String> a = new ArrayList<>();

    // 往里面添加元素
    a.add("hello");
    a.add("world");
    // 打印数组
    System.out.printf("打印数组 %s \n", a);

    // 取元素, 参数为元素所在位置的索引
    String s1 = a.get(0);
    String s2 = a.get(1);
    System.out.printf("打印下标为 0 的元素 %s \n", s1);
    System.out.printf("打印下标为 1 的元素 %s \n", s2);

    // 查看数组的长度
    int length = a.size();
    System.out.printf("数组长度 %s \n", length);

    // 修改元素
    a.set(0, "hello2");
    System.out.printf("打印修改后的数组 %s \n", a);


    // 根据下标来删除元素
    a.remove(0);
    // 根据元素自己来删除元素
    a.remove(s2);
    System.out.printf("打印删除后的数组 %s \n", a);
}

```

## HashMap (字典)

// HashMap 是一个非常重要的存储数据的类型
// HashMap 和 array 是最重要的两个存储数据的工具（主流语言都有这两个类型，只是名字不一样）
//
// array 通过数字下标来访问元素
// HashMap 通过 key（键）来访问元素
// 详细请看下方资料

// 创建 HashMap

HashMap<String, String> usopper = new HashMap<>();
// 使用 put 来给字典存值
usopper.put("name", "shenle");
usopper.put("height", "169");

// 可见, 字典的创建是花括号 {}
// 字典的内容是成对出现的
// put 的第一个参数是 key（键），几乎所有情况下，都是字符串，这也是它的主要用途
// put 第二个参数是 value（值），可以是任意类型，包括 int, string, boolean, array, 字典 等

// 实际上你可以把 array 看做 key 为数字的字典(有一些编程语言就是这么做的)
// 访问（读取/使用） 字典 中的元素

// 通过 get 语法可以用 key 得到 value
log("通过 key 访问 字典 的元素");
String name = usopper.get("name");
log("usopper 的 name 是 %s", name);

// 访问不存在的 key 会得到 null
// null 在 java 中用于表示不存在、未定义
// usopper 对象中并没有 "age" 这个 key，因此下面的语句会得到 null
log("age is %s", usopper.get("age"));

//
// 创建一个新的对象来使用
HashMap<String, String> shenle = new HashMap<>();
shenle.put("name", "xiaoshenle");
shenle.put("height", "169");

// 增加一个元素
shenle.put("gender", "男");
// 我们这里使用课 3 教过的 + 语法来直接输出字典
log("object 增加" + shenle);
log("object 增加 %s", shenle.get("gender"));

// 修改已有的元素
shenle.put("name", "new shenle");
log("object 修改" + shenle);

// 删除元素, 注意这里使用了内置的 delete 语句
shenle.remove("name");
log("object 删除" + shenle);

// 运行，输出如下（注意，输出不一定能保证 key 的顺序，所以你能看到的输出和我这个不一定一样）
// object 增加{gender=男, name=xiaoshenle, height=169}
// object 增加 男
// object 修改{gender=男, name=new shenle, height=169}
// object 删除{gender=男, height=169}
//
// 注意看，增加了一个新的 key "gender"
// 并且可以访问到它的值
// 同时，可以看到 "name" 被修改了
// 另外，key 还会被 remove 函数删除
