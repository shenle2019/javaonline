# Java-web学习笔记005

## Java异常基础知识

```Java
import javax.annotation.processing.FilerException;
import java.io.FileNotFoundException;

// 异常就是代码出错了
// java 里面的异常有两种, 一种叫受检异常, 一种叫未检异常.
// 这两种异常在写代码的时候. 最大的区别就在于
// 已检异常需要在方法的签名里面声明这个异常,
// 未检查异常就不用这么写
public class ExceptionLearn {
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
    // 如果一个方法里抛出了一个受检异常, 就要在方法的签名里写明抛出了哪个异常
    public static void checkedException() throws FileNotFoundException {
        // throw 可以抛出一个异常, 这里的 FilerException 是一个受检异常
        throw new FileNotFoundException("checked");
    }


    public static void callCheckedException() throws FileNotFoundException {
        // try catch会 处理方法中抛出的错误, 也就是异常
        try {
            checkedException();
        } catch (FileNotFoundException e) {
            // 打印异常的信息
            e.printStackTrace();
            // 处理完异常再次抛出这个异常
            throw e;

        } finally {
            // finally 里面的语句, 无论是否发生异常, 都会被执行
            System.out.printf("finally end");
        }
    }

    public static void uncheckedException() {
        // RuntimeException 是一个未检异常
        throw new RuntimeException("unchecked");
    }

    static public void main(String[] args) {
        try {
           // 首先要执行的代码
           log("try test");
        } catch (Exception e) {
           // 这里是出错后会被调用的部分
           log("这里是出错后会被调用的部分");
        } finally {
           // 不管有没有错, 都会被执行的代码
           log("finally end");
        }
        int a = 1;
    }
}
```

## java异常处理机制

### 异常描述
异常机制已经成为判断一门变成语言是否成熟的标准，除了传统的像C语言没有提供异常机制之外，目前主流的编程语言如Java、C#、Ruby、Python等都提供了成熟的异常机制。
异常机制可以使得程序中的异常处理与正常的业务代码分离，保证程序代码更加优雅，并提高程序的健壮性。
​对于一个程序设计人员，需要尽可能的预知所有可能发生的情况，尽可能的保证程序在所有糟糕的情况下都可以运行。

对于一个程序设计人员，需要尽可能的预知所有可能发生的情况，尽可能的保证程序在所有糟糕的情况下都可以运行。

if，else的错误处理机制的缺点？
- 无法穷举所有的异常情况。
- 使得业务实现代码与错误处理代码混杂。


什么是java的异常处理机制？
当运行java程序的时候出现异常情况（意外情形），系统会自动生成一个Exception对象来通知程序，从而实现将“业务实现代码”与“异常处理代码”分离，提供更好的可读性。

Java提供了try(尝试)、catch(捕捉)、finally(最终)这三个关键字来处理异常。在处理各种异常时，需要用到对应的异常类，指的是由程序抛出的对象所属的类。

### 异常处理的使用

由于finally块是可以省略的，异常处理格式可以分为三类：try{ }——catch{ }、try{ }——catch{ }——finally{ }、try{ }——finally{ }。
注意
- 不论程序代码是否存在try代码块中，甚至在catch块中，只要在执行这块代码的时候出现了异常，系统总会自动生成一个异常对象。
- 捕获异常指的是抛出的异常对象是catch块后异常类及其子类的实例，底层是使用instanceof关键字来进行判断的。
- try块中的变量只在代码块中有效。


```Java
public class DealException
{
    public static void main(String args[])
    {
        try
        //要检查的程序语句
        {
            int a[] = new int[5];
            a[10] = 7;//出现异常
        }
        catch(ArrayIndexOutOfBoundsException ex)
        //异常发生时的处理语句
        {
            System.out.println("超出数组范围！");
        }
        finally
        //这个代码块一定会被执行
        {
            System.out.println("*****");
        }
        System.out.println("异常处理结束！");
    }
}
```

可以看出，在异常捕捉的过程中要进行两个判断，第一是try程序块是否有异常产生，第二是产生的异常是否和catch()括号内想要捕捉的异常相同。
那么，如果出现的异常和catch()内想要捕捉的异常不相同时怎么办呢？事实上我们可以在一个try语句后跟上多个异常处理catch语句，来处理多种不同类型的异常。

```Java
public class DealException
{
    public static void main(String args[])
    {
        try
        //要检查的程序语句
        {
            int a[] = new int[5];
            a[0] = 3;
            a[1] = 1;
            //a[1] = 0;//除数为0异常
            //a[10] = 7;//数组下标越界异常
            int result = a[0]/a[1];
            System.out.println(result);
        }
        catch(ArrayIndexOutOfBoundsException ex)
        //异常发生时的处理语句
        {
            System.out.println("数组越界异常");
            ex.printStackTrace();//显示异常的堆栈跟踪信息
        }
        catch(ArithmeticException ex)
        {
            System.out.println("算术运算异常");
            ex.printStackTrace();
        }
        finally
        //这个代码块一定会被执行
        {
            System.out.println("finally语句不论是否有异常都会被执行。");
        }
        System.out.println("异常处理结束！");
    }
}
```
上述例子中ex.printStackTrace();就是对异常类对象ex的使用，输出了详细的异常堆栈跟踪信息，包括异常的类型，异常发生在哪个包、哪个类、哪个方法以及异常发生的行号。

注意
- 捕获多种类型的异常的时候，多种异常类型之间也可以用“|”隔开。
- 捕获多种异常类型的时候，异常变量有隐式的final修饰，因此程序不能对异常变量重新赋值。
```Java
public class Main {

    public static void main(String[] args) {

        try{
            int a = Integer.parseInt(args[0]);
            int b = Integer.parseInt(args[1]);
            int c = a/b;
            System.out.println("您输入的两个数的相除结果是：" + c);
        }catch (IndexOutOfBoundsException|NumberFormatException|ArithmeticException ie){
            System.out.println("程序发生了数组越界、数字格式异常、算数异常之一");
            //捕获异常的时候，异常变量被final修饰不能重新赋值
            //ie = new ArithmeticException();
        }catch (Exception e){
            System.out.println("未知异常");
            e = new RuntimeException("test");
        }
    }
}

//程序发生了数组越界、数字格式异常、算数异常之一
//Process finished with exit code 0

```

异常处理语法结构中只有try块是必须的，没有try块，就不会有catch块和finally块，try块后面可以跟catch块和finally块的其中一个也可以同时跟两个
可以有多个catch块出现，但是父类异常类型的catch块要出现在子类异常catch块的后面，
多个catch块必须位于try块之后，finally块必须位于所有的catch块之后。

有些时候，程序在try块中打开了一些物理资源（例如数据库连接、网络连接和磁盘文件等），这些物理资源都必须显式回收。
​为了保证一定能回收try块中打开的物理资源，异常处理提供了finally块。
不管try块中代码是否出现异常，也不管哪一个catch块被执行，甚至在try块或者catch块中执行了return语句，finally块总会被执行。


- 如果在异常处理代码中使用System.exit(1)语句来退出虚拟机，那么finally块将失去被执行的机会。
- 除非在try块，catch块中调用了退出虚拟机的方法，否则不管在try块、catch块中执行怎样的代码，出现在怎样的情况，异常处理的finally块总会被执行。
- 一旦在finally块中执行了return和throw语句，将会导致try块、catch块中的return、throw语句失效。
- 如果在finally块里也使用了return和throw等导致方法终止的语句，finally块已经终止了方法，系统不会再跳回try块、catch块中执行任何代码。

### throws关键字

throws声明的方法表示该方法不处理异常，而由系统自动将所捕获的异常信息抛给上级调用方法。

```Java

public class throwsDemo
{
    public static void main(String[] args)
    {
        int[] a = new int[5];
        try
        {
            setZero(a,10);
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            System.out.println("数组越界错误！");
            System.out.println("异常："+ex);
        }
        System.out.println("main()方法结束。");
    }
    private static void setZero(int[] a,int index) throws ArrayIndexOutOfBoundsException
    {
        a[index] = 0;
    }
}

```
throws关键字抛出异常，“ArrayIndexOutOfBoundsException”表明setZero()方法可能存在的异常类型，一旦方法出现异常，setZero()方法自己并不处理，而是将异常提交给它的上级调用者main()方法。

使用throws声明抛出异常的思路是，当前方法不知道如何处理这种类型的异常，该异常应该由上一级调用者处理，如果main方法也不知道如何处理这种异常，也可以使用throws来抛出异常，该异常将会被JVM处理。
​JVM对异常的处理方法是，打印异常的跟踪栈信息，并中止程序运行。
异常跟踪栈信息：逐层定位异常代码所在地点。
```Java
    public static void main(String[] args) throws FileNotFoundException {

        test();

    }
    public static void test() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("a.txt");
    }
```

注意

- 一旦使用了throws来抛出异常，程序就可以不使用 try…catch代码块来捕获异常了。
- 使用throws抛出异常时候有一个限制，子类方法抛出的异常类型应该是父类方法声明抛出异常类型的子类或相同，子类方法抛出的异常不允许比父类方法抛出的异常多。
- 对于Checked异常，java要求必须显式捕获处理该异常（try…catch），或者显示声明抛出异常（throws）。

Checked异常的优势
- Checked异常能在编译的时候提醒程序员代码可能存在的问题，提醒程序员必须注意处理该异常。
- 声明该异常由该方法的调用者来处理，从而可以避免程序员因为粗心而忘记处理该异常的错误。




### throw关键字

throw的作用是手工抛出异常类的实例化对象。

```Java
public class throwDemo
{
    public static void main(String[] args)
    {
        try
        {
            //抛出异常的实例化对象
            throw new ArrayIndexOutOfBoundsException("\n个性化异常信息：\n数组下标越界");
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            System.out.println(ex);
        }
    }
}
```

我们能发现，throw好像属于没事找事，引发运行期异常，并自定义提示信息。事实上，throw通常和throws联合使用，抛出的是程序中已经产生的异常类实例。

```Java
public class ExceptionDemo
 {
     public static void main(String[] args) 
     {
         int[] a = new int[5];
         try
         {
             setZero(a,10);
         }
         catch(ArrayIndexOutOfBoundsException e)
         {
             System.out.println("异常："+e);
         }
         System.out.println("main()方法结束！");
     }
     public static void setZero(int[] a,int index) throws ArrayIndexOutOfBoundsException
     {
         System.out.println("setZero方法开始：");
         try
         {
             a[index] = 0;
         }
         catch(ArrayIndexOutOfBoundsException ex)
         {
             throw ex;
         }
         finally
         {
             System.out.println("setZero方法结束。");
         }
     }
 }
```

输出结果：

setZero方法开始：
setZero方法结束。
异常：java.lang.ArrayIndexOutOfBoundsException:10
main()方法结束！


注意
- 如果throw语句抛出的异常是Checked异常，则该throw语句要么处于try块中，显式捕获异常，要么放在一个带有throws声明抛出的方法中，即把该异常交给该方法的调用者来处理。
- 如果throw语句抛出的异常是RunTime异常，该语句无须放在try块中，也无须放在throws声明抛出的方法中，程序接可以显式捕获并处理异常也可以显式声明抛出异常。


### RuntimeException类

Exception和RuntimeException的区别：
Exception:强制性要求用户必须处理；
RunException:是Exception的子类，由用户选择是否进行处理。

Throwable类下有Error（系统错误，处理不了）+ Exception（强制性要求用户必须处理）
Exception下有RuntimeException（由用户选择是否进行处理）+其他exception（受检异常，如IOexception，必须处理）

RuntimeException（未检异常，由用户选择是否进行处理）：
异常	描述
ArithmeticException	当出现异常的运算条件时，抛出此异常。例如，一个整数"除以零"时，抛出此类的一个实例。
ArrayIndexOutOfBoundsException	用非法索引访问数组时抛出的异常。如果索引为负或大于等于数组大小，则该索引为非法索引。
ArrayStoreException	试图将错误类型的对象存储到一个对象数组时抛出的异常。
ClassCastException	当试图将对象强制转换为不是实例的子类时，抛出该异常。
IllegalArgumentException	抛出的异常表明向方法传递了一个不合法或不正确的参数。
IllegalMonitorStateException	抛出的异常表明某一线程已经试图等待对象的监视器，或者试图通知其他正在等待对象的监视器而本身没有指定监视器的线程。
IllegalStateException	在非法或不适当的时间调用方法时产生的信号。换句话说，即 Java 环境或 Java 应用程序没有处于请求操作所要求的适当状态下。
IllegalThreadStateException	线程没有处于请求操作所要求的适当状态时抛出的异常。
IndexOutOfBoundsException	指示某排序索引（例如对数组、字符串或向量的排序）超出范围时抛出。
NegativeArraySizeException	如果应用程序试图创建大小为负的数组，则抛出该异常。
NullPointerException	当应用程序试图在需要对象的地方使用 null 时，抛出该异常
NumberFormatException	当应用程序试图将字符串转换成一种数值类型，但该字符串不能转换为适当格式时，抛出该异常。
SecurityException	由安全管理器抛出的异常，指示存在安全侵犯。
StringIndexOutOfBoundsException	此异常由 String 方法抛出，指示索引或者为负，或者超出字符串的大小。
UnsupportedOperationException	当不支持请求的操作时，抛出该异常。


其他exception（受检异常，如IOexception，必须处理）：
异常	描述
ClassNotFoundException	应用程序试图加载类时，找不到相应的类，抛出该异常。
CloneNotSupportedException	当调用 Object 类中的 clone 方法克隆对象，但该对象的类无法实现 Cloneable 接口时，抛出该异常。
IllegalAccessException	拒绝访问一个类的时候，抛出该异常。
InstantiationException	当试图使用 Class 类中的 newInstance 方法创建一个类的实例，而指定的类对象因为是一个接口或是一个抽象类而无法实例化时，抛出该异常。
InterruptedException	一个线程被另一个线程中断，抛出该异常。
NoSuchFieldException	请求的变量不存在
NoSuchMethodException	请求的方法不存在

处理Checked异常的方式：
- 当前方法明确知道如何处理异常，程序应该使用try…catch块来捕获异常然后在对应的catch块中修复异常。
- 当前方法不知道如何处理异常，应该在定义该方法的时候抛出异常。


异常方法：
序号	方法及说明
1	public String getMessage() 返回关于发生的异常的详细信息。这个消息在Throwable 类的构造函数中初始化了。
2	public Throwable getCause() 返回一个Throwable 对象代表异常原因。
3	public String toString() 使用getMessage()的结果返回类的串级名字。
4	public void printStackTrace() 打印toString()结果和栈层次到System.err，即错误输出流。
5	public StackTraceElement [] getStackTrace() 返回一个包含堆栈层次的数组。下标为0的元素代表栈顶，最后一个元素代表方法调用堆栈的栈底。
6	public Throwable fillInStackTrace() 用当前的调用栈层次填充Throwable 对象栈层次，添加到栈层次任何先前信息中。

常用方法
- getMessage()：返回该异常的详细描述字符串。
- printStackTrace()：将该异常的跟踪栈信息输出到标准错误输出。
- printStackTrace(PrintStream s)：将该异常的跟踪栈信息输出到指定输出流。
- getStackTrace()：返回该异常的跟踪栈信息。


### 自定义异常类

自定义异常类继承自Exception类，可以使用父类的大量的方法，也可自己编写方法来处理特定的事件。Java提供用继承的方式运行用户自己编写的异常类。
```Java

class MyException extends Exception
{
    public MyException(String message)
    {
        super(message);
    }
}
public class DefinedException
{
    public static void main(String[] args)
    {
        try
        {
            throw new MyException("\n自定义异常类！");
        }
        catch(MyException e)
        {
            System.out.println(e);
        }
    }
}

```


用户自定义异常都应该继承Exception基类，如果希望自定义Runtime异常，则应该继承RuntimeException基类。
定义异常类的时候通常需要提供两个构造器，**一个是无参构造器，另一个是带一个字符串参数的构造器。**这个字符串将作为这个异常对象的描述信息（也就是异常对象的getMessage（）方法的返回值）。


```Java
class AuctionException extends Exception{
    public AuctionException(){
        
    }
    public AuctionException(String msg){
        super(msg);
    }
}

```

### 异常链

使用异常链原因

- 对于正常用户而言，他们不想看到底层的异常，底层异常对他们没有使用系统没有任何帮助。
- 对于恶意用户而言，底层异常暴露出来会使得系统不安全。

处理方法

​- 程序先捕获原始异常，然后抛出一个新的业务异常，新的业务异常中包含了对用户的提示信息，这种处理方式被称为异常转译。
- 这种捕获一个异常然后接着抛出另一个新的异常，并把原始异常信息保存下来是一种典型的链式处理（23种设计模式之一的责任链模式），也称为“异常链”。

### java的异常跟踪栈

异常对象的printStackTrace()方法用于打印异常对象的跟踪栈信息，根据这个方法的输出结果，开发者可以找到异常的源头，并跟踪到异常一路触发的过程。
```Java
public class Main {

    public static void main(String[] args) throws AuctionException {

        first();

    }
    public static void first() throws AuctionException {
        second();
    }
    public static void second() throws AuctionException {
        third();
    }
    public static void third() throws AuctionException {
        throw new AuctionException("是异常");
    }


}
class AuctionException extends Exception{
    public AuctionException(){

    }
    public AuctionException(String msg){
        super(msg);
    }

    
//   Exception in thread "main" AuctionException: 是异常
//	at Main.third(Main.java:17)
//	at Main.second(Main.java:14)
//	at Main.first(Main.java:11)
//	at Main.main(Main.java:7)
//
//Process finished with exit code 1 
```


### 异常处理规则

[Java - 9个处理异常的最佳准则](https://www.cnblogs.com/kcher90/p/7468512.html)

- 不要过度使用异常
- 不要使用过于庞大的try块
- 避免使用Catch all语句
- 不要忽略捕获到的异常



