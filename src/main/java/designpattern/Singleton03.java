package designpattern;

/**
 * @author lifujian  2017年12月5日
 * @Description: 饿汉式，线程安全  (所谓饿，就是 类加载时就初始化)
 *     优点：没有加锁，执行效率会提高。          缺点：类加载时就初始化，浪费内存。
 */
public class Singleton03 {
    private static Singleton03 instance = new Singleton03();
    private Singleton03() {
    }
    
    public static Singleton03 getInstance() {
        return instance;
    }
}
