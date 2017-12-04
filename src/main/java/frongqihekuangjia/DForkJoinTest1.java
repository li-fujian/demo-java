package frongqihekuangjia;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author lfj  2017年7月11日
 * @Description: Fork/Join 框架 
 * 工作窃取算法  双端队列
 *  RecursiveAction：用于没有返回结果的任务。
 *  RecursiveTask ：用于有返回结果的任务。
 */
/**
 * @Description: 计算 1 + 2 + 3 + 4 的结果
 */
public class DForkJoinTest1 extends RecursiveTask<Integer>{
    
    private static final long serialVersionUID = 1L;
    
    private static final int THRESHOLD = 10000; // 阈值。每两个加数相加分为一个任务。
    private int start;
    private int end;
    
    public DForkJoinTest1 (int start, int end) {
        this.start = start;
        this.end = end;
    }

    // 这个任务执行主要的计算。
    @Override
    protected Integer compute() {
        int sum = 0;
        
        // 如果任务足够小就计算任务
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 如果任务大于阈值，就分割成两个子任务计算
            int middle = (start + end) / 2;
            DForkJoinTest1 leftTask = new DForkJoinTest1(start, middle);
            DForkJoinTest1 rightTask = new DForkJoinTest1(middle + 1, end);
            // 执行子任务
            leftTask.fork();
            rightTask.fork();
            // 等待子任务执行完，并得到其结果
            
//            // 处理异常
//            if (leftTask.isCompletedAbnormally()) {
//                System.out.println(leftTask.getException()); // 如果任务被取消了则返回CancellationException, 任务没有完成或者没有抛出异常则返回null
//            }
            
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();
            // 合并子任务
            sum = leftResult + rightResult;
            
            
        }
        return sum;
    }
    
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        // 生成一个计算任务，负责计算 1 + 2 + 3 + 4
        DForkJoinTest1 task = new DForkJoinTest1(1, 100000);
        // 执行一个任务
        Future<Integer> result = forkJoinPool.submit(task);
        try {
            System.out.println(result.get()); // 705082704
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    
//    public static void main(String[] args) {
//        int sum = 0;
//        for (int i = 1; i < 100001; i++) {
//            sum += i;
//        }
//        System.err.println(sum); //705082704
//    }

}
