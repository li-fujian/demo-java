package designpattern;

/**
 * @author lifujian  2017年12月5日
 * @Description: 懒汉式，线程安全  (所谓懒，就是需要用时才实例化)
 */
public class Singleton02 {
    private static Singleton02 instance;
    private Singleton02() {
        
    }
    
    // 此处进行线程同步 在高并发下会有很大的性能问题
    public static synchronized Singleton02 getInstance() {
        if (instance == null) {
            instance = new Singleton02();
        }
        return instance;
    }
}
