import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * ClassName: TestMLThreadPool
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/22 - 18:43
 * @Version: v1.0
 */
public class TestMLThreadPool {
    @Test
    public void testMLHtreadPool() throws ExecutionException, InterruptedException {
        long stime = System.currentTimeMillis();
        ExecutorService threadPool = InitThreadPool.getInstance() ;
        Future<String> future = threadPool.submit(new MLUse("SELECT * FROM USER WHERE id = 10")) ;
        String result = future.get() ;
        System.out.println(result);
        long etime = System.currentTimeMillis();
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
    }
}
