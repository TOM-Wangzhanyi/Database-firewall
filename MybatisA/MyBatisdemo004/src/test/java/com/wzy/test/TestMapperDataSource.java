package com.wzy.test;

import com.wzy.mybatis.mapper.InterceptMapper;
import com.wzy.mybatis.pojo.TestIntercepter;
import com.wzy.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * ClassName: TestMapperDataSource
 * Package: com.wzy.test
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/6/1 - 17:57
 * @Version: v1.0
 */
public class TestMapperDataSource {
    @Test
    public void testInsertUser()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        InterceptMapper mapper = (InterceptMapper) sqlSession.getMapper(InterceptMapper.class);
        int result = mapper.testInsertIntercept(new TestIntercepter(null,"创新实践演示","男", null,null));
        System.out.println(result);
    }
}
