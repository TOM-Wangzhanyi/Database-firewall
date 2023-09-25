package multi;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * ClassName: Final
 * Package: multi
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/17 - 15:04
 * @Version: v1.0
 */
public class FinalAll {
    public String finalAll(String sql) throws ExecutionException, InterruptedException {
        ExecutorService executor = InitThreadPool.getInstance();
        MLAndRuleTask task1 = new MLAndRuleTask(sql);
        Future<String> future = executor.submit(task1);
        String str = future.get();
        executor.shutdown();
        return str;
    }
}
