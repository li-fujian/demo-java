package designpattern;

/**
 * @author lifujian  2017年12月5日
 * @Description: 懒汉式，线程不安全  (所谓懒，就是需要用时才实例化)
 */
public class Singleton01 {
    private static Singleton01 instance;
    private Singleton01() {
        
    }
    
    public static Singleton01 getInstance() {
        if (instance == null) {
            instance = new Singleton01();
        }
        return instance;
    }
}
