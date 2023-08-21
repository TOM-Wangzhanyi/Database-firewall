package com.wzy.mybatis.plugin;

import com.wzy.mybatis.mapper.InterceptMapper;
import com.wzy.mybatis.mapper.TestMapper;
import com.wzy.mybatis.pojo.TestIntercepter;
import com.wzy.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.SqlSession;

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
public class TestNewDataSourcePlugin implements Interceptor {
    private Properties properties = new Properties();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        SqlSession sqlSession = SqlSessionUtils.getFireWallSqlSession();
        TestMapper mapper = (TestMapper) sqlSession.getMapper(TestMapper.class);
        int result = mapper.testInsertIntercept(new TestIntercepter(null,"创新实践演示","男", null,null));
        Object returnObject = invocation.proceed();
        return returnObject;
    }
    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
