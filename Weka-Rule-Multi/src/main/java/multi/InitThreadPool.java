package multi;

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

//单例模式中的饿汉模式去初始化线程池
public class InitThreadPool {
        private static ExecutorService instance = new ThreadPoolExecutor(30,50,1L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(80), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy()) ;

        private InitThreadPool(){} ;

        public static ExecutorService getInstance(){
            return instance ;
        }

}
