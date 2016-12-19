package bingfaheduoxiancheng.lock;

import java.util.concurrent.locks.StampedLock;

/**
 * @author lfj 2016年12月19日
 * @Description: 邮戳锁
 */
public class StampedLockDemo {
    private double x, y;
    private final StampedLock sl = new StampedLock();

    void move(double deltaX, double deltaY) { // an exclusively locked method
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    double distanceFromOrigin() { // A read-only method
        long stamp = sl.tryOptimisticRead();
        double currentX = x, currentY = y;
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}

/* 为了提高读写操作下的性能，提出了锁分离。
 * 一般使用读写锁
 * 邮戳锁是对读写锁的改进
 * 邮戳锁认为  读不应该阻塞写。  当读写互斥的时候，读应该重读，而不是不让写线程写。 偏向于写线程的改进。
 * 解决：读多写少时，读写锁会产生写线程饥饿现象
 * 
 * 
 */