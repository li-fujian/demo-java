package designpattern;

/**
 * @author lifujian  2017年12月5日
 * @Description: 静态内部类
 *     
 */
public class Singleton05 {
    private static class SingletonHolder {
        private static final Singleton05 INSTANCE = new Singleton05();
    };
    private Singleton05() {
    }
    
    public static final Singleton05 getInstance() {
        return SingletonHolder.INSTANCE;
    }
}

/*
 * 
 * 建议使用第 3 种饿汉方式
 * 
 *
 */
