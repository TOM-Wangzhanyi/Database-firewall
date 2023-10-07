package com.wzy.mybatis.plugin;

import com.wzy.mybatis.pojo.TestIntercepter;
import com.wzy.mybatis.utils.DatabaseTask;
import com.wzy.mybatis.utils.ThreadPool;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.apache.ibatis.executor.Executor;

/**
 * ClassName: SqlPlugin
 * Package: com.wzy.mybatis.plugin
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/30 - 15:32
 * @Version: v1.0
 */
@Intercepts({
        @Signature(type = Executor.class,method = "query",args={MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class,method = "query",args={MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class,method = "update",args={MappedStatement.class,Object.class})



})
public class SqlPlugin implements Interceptor {
    private Properties properties;
    private Object lock = new Object();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement =(MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1] ;
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object ret = null ;

            CompletableFuture<Integer> future1 = null;
            CompletableFuture<Integer> future2 = null;
            CompletableFuture<Integer> future3 = null;

            //获取当前线程，当前线程提交两个异步并行的任务，主线程睡眠，然后由另一个异步并行的任务汇总结果，然后唤醒主线程，
            //主线程获取future3的返回值，根据返回值决定是否执行


            Object parameterObject = boundSql.getParameterObject();
        //    Field[] declaredFields = parameterObject.getClass().getDeclaredFields();
            Thread thread = Thread.currentThread();
         //   ThreadPool.getInstance().submit(new DatabaseTask(thread));
            try {
                //
               // ThreadPool.getInstance().submit(new DatabaseTask(Thread.currentThread())) ;
                ExecutorService executor = ThreadPool.getInstance() ;
                future1 = CompletableFuture.supplyAsync(() -> {
                    System.out.println("1:" + Thread.currentThread().getName());
                    return 123;
                }, executor);
                future2 = CompletableFuture.supplyAsync(() -> {
                    System.out.println("2:" + Thread.currentThread().getName());
                    return 123;
                }, executor);
                future3 = future1.thenCombineAsync(future2,(f1, f2) -> {
                    System.out.println("3:" + Thread.currentThread().getName());
                    System.out.println("第1个任务结果:" + f1);
                    System.out.println("第2个任务结果:" + f2);
                    if(f1 == f2){
                        System.out.println("两个结果是一样的");
                        return 1 ;
                    }else return 0 ;
                },executor).whenComplete((v, t) -> {
                    thread.interrupt();
                });
                System.out.println("最后执行结果:" + future3.get());
                Thread.sleep(6000);
            }catch (InterruptedException e) { // 当异步线程唤醒主线程时，会触发中断异常
                System.out.println("-----------------------------------------------");
                System.out.println();
                if(future3.get() == 1){
                    ret = invocation.proceed();
                }

            }
           // ret = invocation.proceed();
           // thread.wait();
         //

        finally {
            try {
                String sql = getSql(configuration, boundSql);
               // System.out.println("说明这个语句获取失败了");
                sql+="\r\n";
                writeToTXT(sql);
                System.out.println("写文件这步执行了\n");

                //打印文件
                System.out.println("执行的sql: "+ sql);
            }catch(NullPointerException e){
                System.out.println("获取SQL语句或者写入文件失败\n");
            }
        }
        return ret;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private String getSql(Configuration configuration, BoundSql boundSql) throws IllegalAccessException {
        String sql = boundSql.getSql();
        if(sql == null || sql.length() == 0){
            return "";
        }
        sql = beautifySql(sql);
        //将？替换为真正的参数

        Object parameterObject = boundSql.getParameterObject();
        //修改parameterObject里面的值
//        Field[] declaredFields = parameterObject.getClass().getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            if("name".equals(declaredField.getName()))
//            {
//                declaredField.setAccessible(true);
//                declaredField.set(parameterObject,"非法的插入");
//            }
//        }


        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        if(!parameterMappings.isEmpty() && parameterObject!=null)
        {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if(typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())){
                sql = replaceSql(sql,parameterObject);
            }else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for(ParameterMapping parameterMapping : parameterMappings){
                    String propertyName = parameterMapping.getProperty();
                    if(metaObject.hasGetter(propertyName)){
                        Object obj = metaObject.getValue(propertyName);
                        sql = replaceSql(sql,obj);
                    }else if(boundSql.hasAdditionalParameter(propertyName)){
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = replaceSql(sql,obj);
                    }
                }
            }
        }
        return sql ;
    }

    private String replaceSql(String sql, Object parameterObject) {
        String result;
        if (parameterObject instanceof String) {
            result = "'" + parameterObject.toString() + "'";
        } else if (parameterObject instanceof Date) {
            result = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parameterObject) + "'";
        }else{
            result = parameterObject.toString();
        }
        return sql.replaceFirst("\\?",result);
    }
    private String beautifySql(String sql) {
        return sql.replaceAll("[\\s\n]+"," ");
    }


    public void writeToTXT(String str) {
        FileOutputStream o = null;
        String path = "C:\\Users\\wzyxi\\Desktop\\";
        String filename = "SqlLog.txt";
        byte[] buff = new byte[]{};
        try {
            File file = new File(path + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            buff = str.getBytes();
            o = new FileOutputStream(file, true);
            o.write(buff);
            o.flush();
            o.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    }
