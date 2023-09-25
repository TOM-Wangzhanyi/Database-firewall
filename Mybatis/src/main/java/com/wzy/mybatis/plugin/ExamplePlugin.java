package com.wzy.mybatis.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.util.Properties;

/**
 * ClassName: ExamplePlugin
 * Package: com.wzy.mybatis.plugin
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/14 - 10:29
 * @Version: v1.0
 */
@Intercepts({@Signature(
        type= Executor.class,
        method = "update",
        args = {MappedStatement.class,Object.class})})
public class ExamplePlugin implements Interceptor {
    private Properties properties = new Properties();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // implement pre-processing if needed
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if(SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
            System.out.println("已经拦截insert方法,不允许执行");
        }
        Object returnObject = invocation.proceed();
        // implement post-processing if needed
        return returnObject;
    }
    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
