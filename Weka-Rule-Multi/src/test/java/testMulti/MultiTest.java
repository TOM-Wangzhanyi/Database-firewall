package testMulti;

import multi.InitThreadPool;
import multi.MLAndRuleTask;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * ClassName: MultiTest
 * Package: testMulti
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/17 - 14:23
 * @Version: v1.0
 */
public class MultiTest {
    @Test
    public void multiTest() throws ExecutionException, InterruptedException {
        ExecutorService executor = InitThreadPool.getInstance() ;
        MLAndRuleTask task1 = new MLAndRuleTask("select * from t_user") ;
        MLAndRuleTask task2 = new MLAndRuleTask("select * from t_user") ;
        MLAndRuleTask task3 = new MLAndRuleTask("select * from t_user") ;
        MLAndRuleTask task4 = new MLAndRuleTask("select * from t_user") ;
        MLAndRuleTask task5 = new MLAndRuleTask("select * from t_user") ;
        Future<String> future = executor.submit(task1) ;
        String str = future.get() ;
        System.out.println(str);
        executor.submit(task1) ;
        executor.submit(task2) ;
        executor.submit(task3) ;
        executor.submit(task4) ;
        executor.submit(task5) ;
        executor.shutdown();
    }
}
