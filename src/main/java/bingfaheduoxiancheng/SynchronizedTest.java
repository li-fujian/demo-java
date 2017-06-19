package bingfaheduoxiancheng;


/**
 * @author lfj  2017年6月19日
 * @Description: 
 */
public class SynchronizedTest {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Thread MyThread = new MyThread();
            MyThread.start();
        }
    }
}

class Sync {
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

class MyThread extends Thread {
    public void run() {
        Sync sy = new Sync();
        sy.test();
    }
}
/*
现象：
    开始。。。
    开始。。。
    开始。。。
    结束。。。
    结束。。。
    结束。。。
*/
// 分析：起了三个线程，每个线程分别 new 了一个 'Sync'对象, synchronized 锁的是对象，而非代码，所以三个线程互不会影响。


