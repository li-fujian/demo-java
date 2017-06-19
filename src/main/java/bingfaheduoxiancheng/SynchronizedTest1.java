package bingfaheduoxiancheng;


/**
 * @author lfj  2017年6月19日
 * @Description: 
 */
public class SynchronizedTest1 {
    public static void main(String[] args) {
        Sync1 sync1 = new Sync1();
        for (int i = 0; i < 3; i++) {
            Thread MyThread = new MyThread1(sync1);
            MyThread.start();
        }
    }
}

class Sync1 {
    public synchronized void test() {
        System.out.println("开始。。。");
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("结束。。。");
    }
}

class MyThread1 extends Thread {
    private Sync1 sync1;
    public MyThread1(Sync1 sync1) {
        this.sync1 = sync1;
    }
    public void run() {
        sync1.test();
    }
}

// 现象：
/*
开始。。。
结束。。。
开始。。。
结束。。。
开始。。。
结束。。。
*/
// 分析：起了三个线程，但三个线程都使用的同一个 'Sync'对象, synchronized 锁的是对象，而非代码，所以三个线程会串行打印。


