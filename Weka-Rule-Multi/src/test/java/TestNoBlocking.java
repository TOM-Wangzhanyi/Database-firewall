import com.sun.corba.se.impl.orbutil.closure.Future;
import entity.Result;
import multi.InitThreadPool;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
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
            System.out.println(droolsRuleServiceimpl.findAll()+"所有的规则");
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
            System.out.println("weka的结果"+f1);
            System.out.println("rule的结果"+f2);
            if(f1 == 1 && f2 == 1){
                System.out.println("这是注入语句，未匹配白名单且weka认为是注入语句");
                return 1 ;
            }else if(f1 == 1 && f2 == 0){
                System.out.println("这是安全语句，匹配白名单，但是机器学习认为是注入语句");
                return 0 ;
            }else if(f1 == 0 && f2 == 1){
                System.out.println("这是安全语句，未匹配白名单，但是机器学习认为是安全语句");
                return 0 ;
            }else if(f1 == 0 && f2 == 0){
                System.out.println("这是安全语句，匹配白名单，机器学习认为是安全语句");
                return 0 ;
            }
            return 2 ;
        }, executor).whenComplete((v, t) -> {
            thread.interrupt();
        });

    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {
        Thread thread = Thread.currentThread() ;
        String sql = "SELECT * FROM users WHERE id='1' and left((select database()),1)='s'-- ' LIMIT 0,1";
        test1(thread,sql);
        try {
            thread.sleep(3000);
            System.out.println("睡醒了"); // 是不会执行的
        }catch (InterruptedException e){
            int result = future3.get() ;
            if(result == 0){
                System.out.println("被打断了，最终两个模块的判断结果是： 安全的SQL语句");
            }else if(result == 1){
                System.out.println("被打断了，最终两个模块的判断结果是： SQL注入语句");
            }
        }

        //下面这部分是测试redis动态添加规则用的


        thread.sleep(5000) ;

        test1(thread,sql);
        try {
            thread.sleep(3000);
            System.out.println("睡醒了"); // 是不会执行的
        }catch (InterruptedException e){
            int result = future3.get() ;
            if(result == 0){
                System.out.println("被打断了，最终两个模块的判断结果是： 安全的SQL语句");
            }else if(result == 1){
                System.out.println("被打断了，最终两个模块的判断结果是： SQL注入语句");
            }
        }


    }

    //这个方法就是最终方法，先查询缓存，然后根据结果去调用，如果没有缓存，就在线程池中选取两个线程，分配任务，再根据结果决定是否放行。
    //这个方法最终整合进intercept类中，这个方法，在调用drools的时候开启的redis订阅线程。还剩下给rules进行加锁，以及weka的模型进行加锁
    @Test
    public void TestALL() throws ExecutionException, InterruptedException {
        Thread thread = Thread.currentThread() ;
        String sql = "SELECT * FROM users WHERE id='1' and left((select database()),1)='s'-- ' LIMIT 0,1";


        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String ans = jedis.get("SQLInjection") ;
//要用equals方法，==是没有用的
        if(ans.equals("1")){
            System.out.println("缓存显示这个是注入语句");
        }else if(ans.equals("0")){
            System.out.println("缓存显示这个是安全语句");
        }else if(ans == null){
            test1(thread,sql);
            try {
                thread.sleep(3000);
                System.out.println("睡醒了"); // 是不会执行的
            }catch (InterruptedException e){
                int result = future3.get() ;
                if(result == 0){
                    System.out.println("被打断了，最终两个模块的判断结果是： 安全的SQL语句");
                }else if(result == 1){
                    System.out.println("被打断了，最终两个模块的判断结果是： SQL注入语句");
                }
            }
        }else{
            System.out.println("没有匹配到");
            System.out.println(ans);
        }
    }
}
