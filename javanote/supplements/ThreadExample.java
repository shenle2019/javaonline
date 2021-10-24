import java.io.Writer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Utils {
    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
}


class Bank {
    static Integer money = 0;
    static Lock lock = new ReentrantLock();
    static Lock lock2 = new ReentrantLock();
}

class Deposit implements Runnable{
    public void run() {
        for (int i = 0; i < 10000; i++) {
            // 指令1: 从内存里面读取 money
            // 指令2: 在 CPU 里面把 money + 1
            // 指令3: 把 money 写进内存

            // 我们预想中, 这三步应该同时完成, 并且在做这三步的时候, 没有其他线程能读写内存
            // 这就叫原子操作
            // 在数据库里面, 就叫做事务

            Bank.lock2.lock();
            Utils.log("存钱获取 lock2");
            Bank.lock.lock();
            Utils.log("存钱获取 lock");

            Bank.money = Bank.money + 1;

            Bank.lock.unlock();
            Utils.log("存钱释放 lock");
            Bank.lock2.unlock();
            Utils.log("存钱释放 lock2");


        }
    }
}

class WithDraw implements Runnable{
    public void run() {
        for (int i = 0; i < 10000; i++) {
            // 指令1: 从内存里面读取 money
            // 指令2: 在 CPU 里面把 money - 1
            // 指令3: 把 money 写进内存
            Bank.lock2.lock();
            Utils.log("取钱获取 lock2");
            Bank.lock.lock();
            Utils.log("取钱获取 lock");
            Bank.money = Bank.money - 1;
            Bank.lock.unlock();
            Utils.log("取钱释放 lock");
            Bank.lock2.unlock();
            Utils.log("取钱释放 lock2");
        }
    }
}


public class ThreadExample {
    public static void test() {
        Deposit task1 = new Deposit();
        WithDraw task2 = new WithDraw();


        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        // 让当前线程当 thread1 和 thread2 跑完之后, 才继续往下执行
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Utils.log("Bank money(%s)", Bank.money);
    }

    public static void main(String[] args) {
        test();
    }
}
