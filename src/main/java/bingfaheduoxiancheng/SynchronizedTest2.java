package bingfaheduoxiancheng;

/**
 * @author lfj 2017年6月19日
 * @Description:
 */
public class SynchronizedTest2 {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Thread MyThread = new MyThread2();
            MyThread.start();
        }
    }
}

class Sync2 {
    public void test() {
        synchronized (Sync2.class) {
            System.out.println("开始。。。");
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("结束。。。");
        }

    }
}

class MyThread2 extends Thread {

    public void run() {
        Sync2 sync2 = new Sync2();
        sync2.test();
    }
}

// 现象：
/*
 * 开始。。。 结束。。。 开始。。。 结束。。。 开始。。。 结束。。。
 */
// 分析：这个示例用'synchronized'锁了'Sync2.class' 这个类,实现了全局锁的效果。所以三个线程会串行打印。
//  最后说说static synchronized 方法，static方法可以直接类名加方法名调用，方法中无法使用 this，
//  所以它锁的不是this，而是类的Class对象，所以，static synchronized方法也相当于全局锁，相当于锁住了代码段



