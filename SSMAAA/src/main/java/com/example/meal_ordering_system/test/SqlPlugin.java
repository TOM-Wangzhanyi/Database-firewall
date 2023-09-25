package com.example.meal_ordering_system.test;

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
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

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

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object ret = null;
        try {
            Object parameterObject = boundSql.getParameterObject();
     //       Field[] declaredFields = parameterObject.getClass().getDeclaredFields();
            ret = invocation.proceed();

        } finally {
            try {
                String sql = getSql(configuration, boundSql);
                sql += "\r\n";
                writeToTXT(sql);
                System.out.println("写文件这步执行类\n");

                //打印文件
                System.out.println("执行的sql: " + sql);
            } catch (NullPointerException e) {
                System.out.println("这个非法sql语句，已经把他拦下来了，不可以执行\n");
            }
        }
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
}
