package designpattern;

/**
 * @author lifujian  2017年12月5日
 * @Description: 双重校验锁  (double-checked)
 *     
 */
public class Singleton04 {
    private volatile static Singleton04 instance;
    private Singleton04() {
    }
    
    public static Singleton04 getInstance() {
        if (instance == null) {
            synchronized (Singleton04.class) {
                if (instance == null) {
                    instance = new Singleton04();
                }
            }
        }
        return instance;
    }
}
