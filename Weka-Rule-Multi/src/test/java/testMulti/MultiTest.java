package testMulti;

import multi.InitThreadPool;
import multi.MLAndRule;
import multi.SimpleUse;
import org.junit.jupiter.api.Test;
import weka.core.stemmers.Stemmer;

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
        MLAndRule task1 = new MLAndRule("select * from t_user") ;
        MLAndRule task2 = new MLAndRule("select * from t_user") ;
        MLAndRule task3 = new MLAndRule("select * from t_user") ;
        MLAndRule task4 = new MLAndRule("select * from t_user") ;
        MLAndRule task5 = new MLAndRule("select * from t_user") ;
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
