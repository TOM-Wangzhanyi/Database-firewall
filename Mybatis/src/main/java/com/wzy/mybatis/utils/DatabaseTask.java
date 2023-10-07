package com.wzy.mybatis.utils;

import com.wzy.mybatis.plugin.SqlPlugin;
import org.apache.ibatis.plugin.Invocation;

import java.util.concurrent.Callable;

/**
 * ClassName: DatabseTask
 * Package: com.wzy.mybatis.utils
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/10/6 - 11:41
 * @Version: v1.0
 */
    public class DatabaseTask implements Callable<String> {
        private Thread thread;

        @Override
        public String call() throws Exception {
          //  Thread.sleep(100);
            thread.interrupt();
            System.out.println("成功异步执行了");
            return null;
        }

        public DatabaseTask(Thread thread){
            this.thread = thread ;
        }

    }
