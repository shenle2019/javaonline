import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    private static void arrayDemo() {
        int a[] = {1, 2, 3, 4, 8};
        int a2[] = new int[100];

        log("求 array 长度 %d", a.length);
        log("求 a2 array 长度 %d", a2.length);

        log("用下标访问 array 中的元素");
        log("a[0] %d", a[0]);
        log("a[1] %d", a[1]);
        log("a[2] %d", a[2]);
        log("a[3] %d", a[3]);

        // int i = 0;
        // while(i < a.length) {
        //     log("a[i] %s", a[i]);
        //     i = i + 1;
        // }

        // 修改第一个元素为 20
        a[0] = 20;
        log("循环访问 array 所有元素");
        for (int i = 0; i < a.length; i++) {
            log("element %d", a[i]);
        }


        int min = a[0];
        for (int i = 0; i < a.length; i++) {
            int e = a[i];
            log("e %d", a[i]);
            if (e < min) {
                min = e;
            }
        }
        log("array 最小元素 %s", min);

        int total = 0;
        for (int i = 0; i < a.length; i++) {
            int e = a[i];
            total = total + e;
        }

        log("sum %s", total);

        int average = total / a.length;
    }

    private static String substring(String data, int begin, int end) {
        String r = "";
        for (int i = begin; i < end + 1; i++) {
            char c = data.charAt(i);
            r = r + c;
        }
        return r;
    }


    public static void StringDemo() {
        String s = "hello world";
        log("判断相等或者包含");
        log("结果 1 %b", "good" == "good");
        log("结果 2 %b", "good" == "bad");
        log("结果 3 %b", "good" != "bad");
        log("结果 2 %b", "good".equals("bad"));

        String s1 = "good";
        log("good == bad %s", s1.equals("bad"));

        log("拼接结果 1 %s", "very" + " good");

        /*
        hello
        world
        "world"
        * */
        String name = "usopp";
        String hello = String.format("%s, 你好", name);
        log("%s", hello);

        // \n 叫做换行符号
        String duohang = "hello\n\"world\"";
        log("%s", duohang);
        log("切片 %s", duohang.substring(4));
        log("切片 %s", substring(duohang, 0, 4));

    }

    private static int sum(int[] array) {
        // 先设置一个变量用来存数组的和
        int s = 0;
        for (int i = 0; i < array.length; i++) {
            // 用变量 n 保存元素的值
            int n = array[i];
            s += n;
        }
        // 循环结束, 现在 s 里面存的是数组中所有元素的和了
        return s;
    }

    public static void ensure(boolean condition, String message) {
        if (!condition) {
            System.out.println(message);
        } else {
            System.out.println("测试成功");
        }
    }

    private static int product(int[] array) {
        // 先设置一个变量用来存数组的和
        int s = 1;
        for (int i = 0; i < array.length; i++) {
            // 用变量 n 保存元素的值
            int n = array[i];
            s = s * n;
            // 等于 s += n
        }
        // 1 * 1 * 2 * 3 * 4
        // 循环结束, 现在 s 里面存的是数组中所有元素的和了
        return s;
    }

    private static void testProduct() {
        log("*** test product start");

        int[] a = {8, 2, 3, 4};
        ensure(product(a) == 192, "test product 1");

        int[] a2 = {2, 3, 4, 5};
        ensure(product(a2) == 120, "test product 2");

        int[] a3 = {1, 3, 4, 5};
        ensure(product(a3) == 60, "test product 3");

        log("*** test product end");
    }

    private static int abs(int n) {
        int r = n;
        if (n < 0) {
            r = -n;
        }
        return r;

    }

    public static void testAbs() {
        log("*** test abs start");
        int a1 = 0;
        int r1 = 0;
        ensure(abs(a1) == r1, "testAbs 1");

        int a2 = -11;
        int r2 = 11;
        ensure(abs(a2) == r2, "testAbs 2");

        int a3 = 33;
        int r3 = 33;
        ensure(abs(a3) == r3, "testAbs 3");

        log("*** test abs end");
    }

    private static double average(int[] array) {
        double s = sum(array);
        // log("sum %s", s);
        // log("length %s", array.length);
        // log("sum / length (%s)", 14 / 4);
        double r = s / array.length;
        return r;
    }

    private static boolean doubleEqual(double a, double b) {
        double delta = 0.0001;
        return Math.abs(a - b) < delta;
    }

    private static void testAverage() {
        int a1[] = {1, 2, 5, 6};
        double r1 = 3.5;
        // log("test 1 %s", average(a1));
        ensure(average(a1) == r1, "test average 1");

        int a2[] = {0, 2, 6};
        double r2 = 8.0 / 3.0;
        ensure(doubleEqual(average(a2), r2), "test average 2");
    }

    private static int min(int[] array) {
        int minElement = array[0];
        for (int i = 0; i < array.length; i++) {
            int e = array[i];

            if (e < minElement) {
                minElement = e;
            }
        }

        return minElement;
    }

    private static void testMin() {
        log("*** test min stqrt");
        int a1[] = {2, 3, 1, 5};
        int expectedR1 = 1;
        int r1 = min(a1);
        ensure(r1 == expectedR1, "testMin 1");

        int a2[] = {2, 3, 19, 5};
        int expectedR2 = 2;
        int r2 = min(a2);
        ensure(r2 == expectedR2, "testMin 2");

        log("*** test min end");
    }

    private static void breakStatement() {
        int i = 0;
        while (i < 10) {
            log("while 中的 break 地址");
            // break 语句执行后, 循环就结束了
            for (int j = 0; j < 4; j++) {
                log("内部循环");
                break;
            }
            // 因此 i += 1 这一句是不会被执行的
            i += 1;
        }
        log("break 结束的 i 值 %d", i);
    }

    private static void continueStatement() {
        int i = 0;
        while(i < 10) {
            // 一定要记住改变循环条件，否则会无限循环
            i += 1;
            // 如果 i 是偶数，则 continue 跳过这次循环
            if (i % 2 == 0) {
                continue;
            }
            // continue;
            log("while 中的 continue 语句 %d", i);
        }
        log("continue 循环结束 %s", i);
    }

    public static void demoArrayList() {
    // 新建一个 ArrayList, 里面存放的是 String 类型的元素
        int a5[] = {1, 2, 3};
        log("int array a5 (%s)", a5);
        ArrayList<String> a = new ArrayList<>();
        // int Integer(增强版的 int)
        ArrayList<Integer> a2 = new ArrayList<>();
        // double Double
        ArrayList<Double> a3 = new ArrayList<>();

        log("new ArrayList length %s", a.size());
        // 往里面添加元素
        a.add("hello");
        a.add("world");
        // 打印数组
        log("打印数组 %s \n", a);

        // 取元素, 参数为元素所在位置的索引
        // a[0]
        String s1 = a.get(0);
        String s2 = a.get(1);
        System.out.printf("打印下标为 0 的元素 %s \n", s1);
        System.out.printf("打印下标为 1 的元素 %s \n", s2);

        // 查看数组的长度
        int length = a.size();
        System.out.printf("数组长度 %s \n", length);


        // a[0] = "hello2";
        // 修改元素
        a.set(0, "hello2");
        System.out.printf("打印修改后的数组 %s \n", a);

        // 根据下标来删除元素
        a.remove(0);
        // 根据元素自己来删除元素
        a.remove(s2);
        System.out.printf("打印删除后的数组 %s \n", a);
    }

    private static ArrayList<Integer> test2(ArrayList<Integer> array) {
        ArrayList<Integer> r = new ArrayList<>();
        r.add(123);
        return r;
    }

    private static void demoHashMap() {
        HashMap<String, String> taoer = new HashMap<>();
        taoer.put("name", "usopp");
        taoer.put("height", "169");

        String name = taoer.get("name");
        log("taoer 的 name 是 %s", name);

        log("age is %s", taoer.get("age"));

        log("hashmap %s", taoer);
        taoer.put("name", "usopp2");
        log("hashmap %s", taoer);

        taoer.remove("name");
        log("hashmap after remove %s", taoer);

        taoer.put("分数", "100");
        taoer.get("分数");
    }

    private static int apply(String op, int a, int b) {
        if (op == "+") {
            return a + b;
        } else if (op == "-") {
            return a - b;
        } else if (op == "*") {
            return a * b;
        } else if (op == "/") {
            return a + b ;
        } else {
            return 0;
        }
    }

    public static void ensureIntEqual(int a, int b, String message) {
        if (a != b) {
            String m = String.format("%s, (%s) 不等于 (%s)", message, a, b);
            System.out.println(m);
        } else {
            System.out.println("测试成功");
        }
    }

    private static void testApply() {

        String op1 = "+";
        int a = 4;
        int b = 2;
        int e1 = 7;
        int r1 = apply(op1, a, b);
        ensureIntEqual(r1, e1, "test apply 1");

        String op2 = "-";
        int e2 = 2;
        int r2 = apply(op2, a, b);
        ensureIntEqual(r2, e2, "test apply 2");


        String op3 = "/";
        int e3 = 2;
        int r3 = apply(op3, a, b);
        ensureIntEqual(r3, e3, "test apply 3");

        String op4 = "*";
        int e4 = 8;
        int r4 = apply(op4, a, b);
        ensureIntEqual(r4, e4, "test apply 4");
    }

    private static int applyList(String op, int[] oprands) {
        int s = oprands[0];
        for (int i = 1; i < oprands.length; i++) {
            int e = oprands[i];
            log("s %s", s);
            log("e %s", e);
            s = apply(op, s, e);
        }
        log("result %s", s);
        return s;
    }

    private static void testApplyList() {
        int a[] = {2, 3, 5, 7};
        int r1 = applyList("+", a);
        int e1 = 17;
        ensure(r1 == e1, "test applyList 1");

        int a2[] = {2, 3, 5, 7};
        int r2 = applyList("*", a2);
        int e2 = 210;
        ensure(r2 == e2, "test applyList 2");
    }


    public static void main(String[] args) {
        testProduct();
        testAbs();
        testAverage();
        testMin();
        demoArrayList();
        demoHashMap()
        testApply();
        testApplyList();
    }
}

