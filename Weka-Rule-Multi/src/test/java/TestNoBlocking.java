import com.sun.corba.se.impl.orbutil.closure.Future;
import entity.Result;
import multi.InitThreadPool;
import org.junit.jupiter.api.Test;
import rule.DroolsRuleServiceImpl;
import weka.WekaSingelton;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * ClassName: TestNoBlocking
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/10/7 - 14:30
 * @Version: v1.0
 */
public class TestNoBlocking {



    private CompletableFuture<Integer> future1 = null;
    private CompletableFuture<Integer> future2 = null;
    private CompletableFuture<Integer> future3 = null;
    public  void test1(Thread thread,String sql){

        ExecutorService executor = InitThreadPool.getInstance();
        future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("1:" + Thread.currentThread().getName());
            FilteredClassifier fc = WekaSingelton.getFcInstance() ;
            Instances demo = WekaSingelton.getDemoInstance() ;
            demo.setClassIndex(1);
            Instance instance = new DenseInstance(2);
            //借助trainData2的格式
            instance.setDataset(demo);
            instance.setValue(0, sql);
            instance.setValue(1, "1");   //没有这个会报错
            //  System.out.println("原始数据" + instance);
            demo.add(1, instance);
            // System.out.println(trainData.instance(1));
            demo.setClassIndex(1);
            double result = 0;
            try {
                result = fc.classifyInstance(demo.instance(1));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(result < 0.5)
                return 0 ;
            if(result > 0.5)
                return 1 ;
            return 2;
        }, executor);
        future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("2:" + Thread.currentThread().getName());
            DroolsRuleServiceImpl droolsRuleServiceimpl = (DroolsRuleServiceImpl) DroolsRuleServiceImpl.getInstance();
            //  droolsRuleServiceimpl.getDroolsManager().fireRule("6","select from rules-");
            Result listResult = droolsRuleServiceimpl.getDroolsManager().fireRule("6",sql);
            if(listResult.getResult() == 100) {
                System.out.println("并没有匹配到白名单");
                return 1 ;
            }
            if(listResult.getResult() == 10) {
                System.out.println("匹配到了白名单并且停止匹配");
                return 0 ;
            }
            return 2 ;
        }, executor);
        future3 = future1.thenCombineAsync(future2, (f1, f2) -> {
            if (f2 == 1 && f1 == 1) {
                System.out.println("判断为SQL注入语句");
                return 1;
            } else {
                System.out.println("认为是安全的SQL语句");
                return 0;
            }
        }, executor).whenComplete((v, t) -> {
            thread.interrupt();
        });

    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {
        Thread thread = Thread.currentThread() ;
        String sql = "SELECT wide ( s )  FROM west" ;
        test1(thread,sql);
        try {
            thread.sleep(3000);
            System.out.println("睡醒了"); // 是不会执行的
        }catch (InterruptedException e){
            int result = future3.get() ;
            if(result == 0){
                System.out.println("最终两个模块的判断结果是： 安全的SQL语句");
            }else if(result == 1){
                System.out.println("最终两个模块的判断结果是： SQL注入语句");
            }
        }
    }
}
