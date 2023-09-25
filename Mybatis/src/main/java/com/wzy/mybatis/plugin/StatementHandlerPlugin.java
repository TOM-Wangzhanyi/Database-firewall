package com.wzy.mybatis.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

/**
 * ClassName: StatementHandlerPlugin
 * Package: com.wzy.mybatis.plugin
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/30 - 14:52
 * @Version: v1.0
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
})
public class StatementHandlerPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof StatementHandler) {
            System.out.println("StatementHandler");
            checkHasDeletedAtField(invocation);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 检查查询是否需要添加删除标志字段
     *
     * @param invocation 代理对象
     * @throws Throwable 异常
     */
    private void checkHasDeletedAtField(Invocation invocation) throws Throwable {
        System.out.println("checkHasDeletedAtField");
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        //获取到原始sql语句
        String sql = boundSql.getSql();
        System.out.println("这是拦截的sql"+sql);
        }

}
