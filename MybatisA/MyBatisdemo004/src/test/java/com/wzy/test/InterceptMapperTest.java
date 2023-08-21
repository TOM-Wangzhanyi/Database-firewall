package com.wzy.test;

import com.wzy.mybatis.mapper.InterceptMapper;
import com.wzy.mybatis.pojo.TestIntercepter;
import com.wzy.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.jdbc.Null;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 * ClassName: InterceptMapperTest
 * Package: com.wzy.test
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/29 - 21:47
 * @Version: v1.0
 */
public class InterceptMapperTest {
    @Test
    public void testInsertUser()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        InterceptMapper mapper = (InterceptMapper) sqlSession.getMapper(InterceptMapper.class);
        int result = mapper.testInsertIntercept(new TestIntercepter(null,"创新实践演示","男", null,null));
        System.out.println(result);
    }
}
//记录一下，现在是引入了插件parameterHandlerPlugin，可以添加createdAt和createBy的值，
// 我现在新写了一个类，然后将测试，发现可以执行插件的逻辑，但是这个daytime的时间输不进去
//问题就在这个时间的处理上

//现在改为LocalDateTime发现可以，然后也实验成功了，现在的问题是如何使得daytime为空呢
