package com.wzy.mybatis.utils;

import java.util.concurrent.*;

/**
 * ClassName: ThreadPool
 * Package: com.wzy.mybatis.utils
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/10/6 - 11:42
 * @Version: v1.0
 */
public class ThreadPool {
    private static ExecutorService instance = new ThreadPoolExecutor(30,50,1L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(80), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy()) ;

    private ThreadPool(){} ;

    public static ExecutorService getInstance(){
        return instance ;
    }
}
