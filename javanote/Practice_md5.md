# Practice



### 写一个泛型方法 ArrayListCompare, 传入两个 ArrayList, 里面的元素是任意类型,

```
1public static <T> Boolean ArrayCompare(ArrayList<T> l1, ArrayList<T> l2) {23}4
```

遍历比较两个 ArrayList 里面的元素是否相等, 如果都相等返回 true , 不相等返回 false 



```java
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void ensure(boolean condition, String message) {
        if (!condition) {
            System.out.println(message);
        } else {
            System.out.println("测试成功");
        }
    }

    public static <T> T ElementMirror(T e) {
        return e;
    }

    public static <T> Boolean ArrayCompare(ArrayList<T> l1, ArrayList<T> l2) {

        Boolean result = false;

        if(l1.size() != l2.size()){
            result = false;
        }else {
            for (int i = 0; i < l1.size() ; i++) {
                if(!l1.get(i).equals(l2.get(i))){
                    result = false;
                    break;
                }else {
                    result = true;
                }
            }
        }
        return result;
    }


    public static void testElementMirror() {
        String a1 = "hello";
        String r1 = ElementMirror(a1);
        ensure(r1.equals(a1), "testElementMirror 1");


        Integer a2 = 123;
        Integer r2 = ElementMirror(a2);
        ensure(r2.equals(a2), "testElementMirror 2");

    }

    public static void testArrayCompare() {
        ArrayList<String> a1 = new ArrayList<>(Arrays.asList("InterfaceA", "B", "C", "D"));
        ArrayList<String> expect1 = new ArrayList<>(Arrays.asList("InterfaceA", "B", "C", "D"));

        ensure(ArrayCompare(a1, expect1), "test ArrayCompare 1");;

        ArrayList<Integer> a2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ArrayList<Integer> expect2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

        ensure(!ArrayCompare(a2, expect2), "test ArrayCompare 2");;
    }


    public static void main(String[] args) {
//        testElementMirror();
        testArrayCompare();
    }

}
```



### 写一个函数 ArrayListReverse, 可以传入任意类型的 ArrayList

把 ArrayList 里面的元素倒序排列

```java
import java.util.ArrayList;
import java.util.Arrays;

class User {
    public String name;
    public String content;

    public User(String name, String content) {
        this.name = name;
        this.content = content;
    }
}

public class Main {

    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void ensure(boolean condition, String message) {
        if (!condition) {
            System.out.println(message);
        } else {
            System.out.println("测试成功");
        }
    }

    public static <T> T ElementMirror(T e) {
        return e;
    }

    public static <T> Boolean ArrayCompare(ArrayList<T> l1, ArrayList<T> l2) {

        Boolean result = false;

        if(l1.size() != l2.size()){
            result = false;
        }else {
            for (int i = 0; i < l1.size() ; i++) {
                if(!l1.get(i).equals(l2.get(i))){
                    result = false;
                    break;
                }else {
                    result = true;
                }
            }
        }
        return result;
    }

    public static <T> ArrayList<T> ArrayListMake(T[] arrays) {
        ArrayList<T> result = new ArrayList<T>();
        for (int i = 0; i < arrays.length ; i++) {
            result.add(arrays[i]);
        }
        return result;
    }



    public static void testElementMirror() {
        String a1 = "hello";
        String r1 = ElementMirror(a1);
        ensure(r1.equals(a1), "testElementMirror 1");


        Integer a2 = 123;
        Integer r2 = ElementMirror(a2);
        ensure(r2.equals(a2), "testElementMirror 2");

    }

    public static void testArrayCompare() {
        ArrayList<String> a1 = new ArrayList<>(Arrays.asList("InterfaceA", "B", "C", "D"));
        ArrayList<String> expect1 = new ArrayList<>(Arrays.asList("InterfaceA", "B", "C", "D"));

        ensure(ArrayCompare(a1, expect1), "test ArrayCompare 1");;

        ArrayList<Integer> a2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ArrayList<Integer> expect2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

        ensure(!ArrayCompare(a2, expect2), "test ArrayCompare 2");;
    }

    public static  void testArrayListMake() {
        User u1 = new User("test1", "content1");
        User u2 = new User("test2", "content2");
        User[] userArray = {u1, u2};

        ArrayList<User> userList = ArrayListMake(userArray);


        for (int i = 0; i < userList.size(); i++) {
            User e1 = userArray[i];
            User e2 = userList.get(i);
            ensure(e1.name.equals(e2.name), "testArrayListMake");
            ensure(e1.content.equals(e2.content), "testArrayListMake");    }

    }

    public static <T> ArrayList<T> ArrayReverse(ArrayList<T> list) {
        ArrayList<T> result = new ArrayList<T>();
        T temp;
        int len = list.size();
        for (int i = 0; i < len/2 ; i++) {
            temp = list.get(i);
            list.set(i, list.get(len- 1 -i));
            list.set(len - 1 - i,temp);
        }
        result = list;
        return result;

    }

    public static void testArrayReverse() {
        ArrayList<String> e1 = new ArrayList<>();
        e1.add("hello");
        e1.add("world");
        e1.add("ha");
        e1.add("he");
        e1.add("ho");
//        log("-----------(%s)", e1);
        ArrayList<String> e2 = new ArrayList<>();
        e2.add("ho");
        e2.add("he");
        e2.add("ha");
        e2.add("world");
        e2.add("hello");
//        log("-----------(%s)", e2);
        ArrayList<String> r1 = ArrayReverse(e2);
        ensure(r1.equals(e1), "testElementMirror 1");

        ArrayList<Integer> e3 = new ArrayList<>();
        e3.add(1);
        e3.add(3);
        e3.add(5);
        e3.add(7);
        e3.add(9);
//        log("-----------(%s)", e3);
        ArrayList<Integer> e4 = new ArrayList<>();
        e4.add(9);
        e4.add(7);
        e4.add(5);
        e4.add(3);
        e4.add(1);
//        log("-----------(%s)", e4);
        ArrayList<Integer> r3 = ArrayReverse(e4);
//        log("-----------(%s)", r3);
        ensure(r3.equals(e3), "testElementMirror 3");

    }



    public static void main(String[] args) {
//        testElementMirror();
//        testArrayCompare();
//        testArrayListMake();
        testArrayReverse();
    }

}
```



### 已知一个 6 位数字密码的 md5 摘要

```
13C4CF39BAF458C44AF04D3FECCF84BCD
```

需要你逆向破解出原密码

提示: 暴力破解, 遍历所有的可能性, 与已知的 MD5 摘要进行对比



```java
package learn;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class digestEample {
    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static String hexFromBytes(byte[] array) {
        String hex = new BigInteger(1, array).toString(16);
        int zeroLength = array.length * 2 - hex.length();
        for (int i = 0; i < zeroLength; i++) {
            hex = "0" + hex;
        }
        return hex;
    }


    public static String nChar(int n, String m) {
        String zero = m;
        String result = "";
        for (int i = 0; i < n; i++) {
            result += zero;
        }
        return result;
    }

    public static String rjust(String s, int width, String fillchar) {

        String n_string = s;
        int n_length = n_string.length();
        int zero_number = width - n_length;
        String result = "";
        if (zero_number > 0) {
            String buquan_zero = nChar(zero_number, fillchar);
            result = buquan_zero + n_string;
        } else {
            result = n_string;
        }
        return result;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {


        ArrayList<String> stringArrayList = new ArrayList<>();
        String x = "";
        for (int i = 0; i < 1000000; i++) {
            x = String.valueOf(i);
            if (x.length() < 6) {
                x = rjust(x, 6, "0");
//                log("------------x------------(%s)", x);
            }else{
                x = x ;
            }
            stringArrayList.add(x);
        }
        for (int i = 0; i < 1000000; i++) {
            MessageDigest md = MessageDigest.getInstance("md5");
            String message = stringArrayList.get(i);
            md.update(message.getBytes());
//            log("md5 mdupdate %s", md.digest());

            byte[] result = md.digest();
//           log("md5 result %s", result.length);

            String hex = hexFromBytes(result);
//            if (hex.equals("3C4CF39BAF458C44AF04D3FECCF84BCD")) {
                if (hex.equals("3c4cf39baf458c44af04d3feccf84bcd")) {
                log("------------result------------(%s)", message);
                break;
            }
//           log("md5 result hex (%s) length(%s)", hex, hex.length());
        }
    }
}
```

