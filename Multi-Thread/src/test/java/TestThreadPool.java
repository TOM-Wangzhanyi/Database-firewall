import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * ClassName: TestThreadPool
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/22 - 18:28
 * @Version: v1.0
 */
public class TestThreadPool {

    @Test
    public void testThreadPoolOne() throws ExecutionException, InterruptedException {
        long stime = System.currentTimeMillis();
        ExecutorService threadPool = InitThreadPool.getInstance() ;
        for(int i = 0 ; i < 18 ; i++){
            Future<String> future = threadPool.submit(new SimpleUse("hi" + i)) ;
            String str = future.get() ;
            System.out.println(str);
        }
        long etime = System.currentTimeMillis();
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
    }
    @Test
    public void testwithoutThreadPoll(){
        long stime = System.currentTimeMillis();
        for(int i = 0 ; i < 18 ; i++){
            System.out.println(Thread.currentThread().getName() + "   执行callable的call方法" );
        }
        long etime = System.currentTimeMillis();
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
    }
}
