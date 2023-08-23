import weka.core.Instances;

import java.util.concurrent.*;

/**
 * ClassName: InitThreadPool
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/22 - 18:19
 * @Version: v1.0
 */

//单例模式初始化线程池
public class InitThreadPool {
        private static ExecutorService instance = new ThreadPoolExecutor(7,10,1L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(3), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy()) ;

        private InitThreadPool(){} ;

        public static ExecutorService getInstance(){
            return instance ;
        }

}
