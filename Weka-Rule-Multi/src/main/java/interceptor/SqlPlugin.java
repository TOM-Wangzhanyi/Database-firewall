package interceptor;

import entity.ModelResult;
import mapper.FireWallMapper;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;
import rule.DroolsRuleServiceImpl;
import weka.WekaSingelton;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * ClassName: SqlPlugin
 * Package: com.example.meal_ordering_system.test
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/25 - 15:28
 * @Version: v1.0
 */
@Intercepts({
        @Signature(type = Executor.class,method = "query",args={MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class,method = "query",args={MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class,method = "update",args={MappedStatement.class,Object.class})



})
public class SqlPlugin implements Interceptor {
    private Properties properties;

    private CompletableFuture<Integer> future1 = null;
    private CompletableFuture<Integer> future2 = null;
    private CompletableFuture<Integer> future3 = null;

    String sql = null ;

    int result = 0 ;

    @Override
    public Object intercept(Invocation invocation) throws Exception {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object ret = null;
        try {
            Object parameterObject = boundSql.getParameterObject();
            //       Field[] declaredFields = parameterObject.getClass().getDeclaredFields();
            try {
                sql = getSql(configuration, boundSql);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            Thread thread = Thread.currentThread();
            test1(thread, sql);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                result = future3.get();
                if (result == 0) {
           //         System.out.println("最终两个模块的判断结果是： 安全的SQL语句");
                    ret = invocation.proceed();
                } else {
           //         System.out.println("最终两个模块的判断结果是： SQL注入语句");
                    ret = null;
                }
            }
        } finally {
            SqlSession sqlSession = com.wzy.mybatis.utils.SqlSessionUtils.getFireWallSqlSession();
            FireWallMapper mapper = (FireWallMapper) sqlSession.getMapper(FireWallMapper.class);
            String[] dic = new String[2] ;
            dic[0] = "安全语句";
            dic[1] = "注入语句";
            int ans = result ;
            int result = mapper.InsertResult(new ModelResult(null,sql,dic[ans]));
        }


//        } finally {
//            try {
//                sql += "\r\n";
//                if(result == 0){
//              //  writeToTXT(sql + "这是一个SQL安全语句");
//                }else {
//                    writeToTXT(sql + "这是一个SQL注入语句");
//                }
//                System.out.println("写文件这步执行类\n");
//
//                //打印文件
//                System.out.println("执行的sql: " + sql);
//            } catch (NullPointerException e) {
//                System.out.println("这个非法sql语句，已经把他拦下来了，不可以执行\n");
//            }
//        }
        return ret;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private String getSql(Configuration configuration, BoundSql boundSql) throws IllegalAccessException {
        String sql = boundSql.getSql();
        if (sql == null || sql.length() == 0) {
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

        if (!parameterMappings.isEmpty() && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = replaceSql(sql, parameterObject);
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = replaceSql(sql, obj);
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = replaceSql(sql, obj);
                    }
                }
            }
        }
        return sql;
    }

    private String replaceSql(String sql, Object parameterObject) {
        String result;
        if (parameterObject instanceof String) {
            result = "'" + parameterObject.toString() + "'";
        } else if (parameterObject instanceof Date) {
            result = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parameterObject) + "'";
        } else {
            result = parameterObject.toString();
        }
        return sql.replaceFirst("\\?", result);
    }

    private String beautifySql(String sql) {
        return sql.replaceAll("[\\s\n]+", " ");
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

    public  void test1(Thread thread,String sql){

        ExecutorService executor = multi.InitThreadPool.getInstance();
        future1 = CompletableFuture.supplyAsync(() -> {
        //    System.out.println("1:" + Thread.currentThread().getName());
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
      //      System.out.println("2:" + Thread.currentThread().getName());
            DroolsRuleServiceImpl droolsRuleServiceimpl = (DroolsRuleServiceImpl) DroolsRuleServiceImpl.getInstance();
            //  droolsRuleServiceimpl.getDroolsManager().fireRule("6","select from rules-");
            entity.Result listResult = droolsRuleServiceimpl.getDroolsManager().fireRule("6",sql);
            if(listResult.getResult() == 100) {
             //   System.out.println("并没有匹配到白名单");
                return 1 ;
            }
            if(listResult.getResult() == 10) {
               // System.out.println("匹配到了白名单并且停止匹配");
                return 0 ;
            }
            return 2 ;
        }, executor);
        future3 = future1.thenCombineAsync(future2, (f1, f2) -> {
            if (f2 == 1 && f1 == 1) {
          //      System.out.println("判断为SQL注入语句");
                return 1;
            } else {
              //  System.out.println("认为是安全的SQL语句");
                return 0;
            }
        }, executor).whenComplete((v, t) -> {
            thread.interrupt();
        });

    }
}
