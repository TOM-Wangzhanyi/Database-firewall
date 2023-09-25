package com.wzy.mybatis.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * ClassName: ParameterHandlerPlugin
 * Package: com.wzy.mybatis.plugin
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/29 - 21:34
 * @Version: v1.0
 */

@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
})
public class ParameterHandlerPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 参数代理
        if (invocation.getTarget() instanceof ParameterHandler) {
            System.out.println("ParameterHandler");
            // 自动添加操作员信息
            autoAddOperatorInfo(invocation);
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
     * 自动添加操作员信息
     *
     * @param invocation 代理对象
     * @throws Throwable 异常
     */
    private void autoAddOperatorInfo(Invocation invocation) throws Throwable {
        System.out.println("autoInsertCreatorInfo");
        // 获取代理的参数对象ParameterHandler
        ParameterHandler ph = (ParameterHandler) invocation.getTarget();
        // 通过MetaObject获取ParameterHandler的反射内容
        MetaObject metaObject = MetaObject.forObject(ph,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory());
        // 通过MetaObject反射的内容获取MappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
        // 当sql类型为INSERT或UPDATE时，自动插入操作员信息
        if (mappedStatement.getSqlCommandType() == SqlCommandType.INSERT ||
                mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE) {

            // 获取参数对象
            Object obj = ph.getParameterObject();
            if (null != obj) {
                // 通过反射获取参数对象的属性
                Field[] fields = obj.getClass().getDeclaredFields();
                // 遍历参数对象的属性
                for (Field f : fields) {
                    // 如果sql是INSERT,且存在createdAt属性
                    if ("createdAt".equals(f.getName()) && mappedStatement.getSqlCommandType() == SqlCommandType.INSERT) {
                        // 设置允许访问反射属性
                        f.setAccessible(true);
                        // 如果没有设置createdAt属性，则自动为createdAt属性添加当前的时间
                        if (null == f.get(obj)) {
                            // 设置createdAt属性为当前时间
                            f.set(obj, LocalDateTime.now());
                            System.out.println("没有添加创建时间，系统已经自动添加啦！");
                        }
                    }
                    // 如果sql是INSERT,且存在createdBy属性
                    if ("createdBy".equals(f.getName()) && mappedStatement.getSqlCommandType() == SqlCommandType.INSERT) {
                        // 设置允许访问反射属性
                        f.setAccessible(true);
                        // 如果没有设置createdBy属性，则自动为createdBy属性添加当前登录的人员
                        if (null == f.get(obj)) {
                            // 设置createdBy属性为当前登录的人员
                            f.set(obj, "王展翼");
                            System.out.println("没有添加创建人员，系统已经自动添加啦！");
                            //System.out.println("createdBy");
                        }
                    }
                    // sql为INSERT或UPDATE时均需要设置updatedAt属性
                    if ("updatedAt".equals(f.getName())) {
                        f.setAccessible(true);
                        if (null == f.get(obj)) {
                            f.set(obj, LocalDateTime.now());
                        }
                    }
                    // sql为INSERT或UPDATE时均需要设置updatedBy属性
                    if ("updatedBy".equals(f.getName())) {
                        f.setAccessible(true);
                        if (null == f.get(obj)) {
                            f.set(obj, 0);
                        }
                    }
                }

                // 通过反射获取ParameterHandler的parameterObject属性
                Field parameterObject = ph.getClass().getDeclaredField("parameterObject");
                // 设置允许访问parameterObject属性
                parameterObject.setAccessible(true);
                // 将上面设置的新参数对象设置到ParameterHandler的parameterObject属性
                parameterObject.set(ph, obj);
            }
        }
    }
}