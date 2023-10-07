package com.wzy.test;

import com.wzy.mybatis.mapper.SQLMapper;
import com.wzy.mybatis.pojo.User;
import com.wzy.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * ClassName: SQLMapperTest
 * Package: com.atguigu.mybatis.test
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/8 - 15:44
 * @Version: v1.0
 */
public class SQLMapperTest {


    @Test
    public void testGetUserByTableName() throws IOException {
//        String temp ;
//        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
//        SQLMapper mapper = sqlSession.getMapper(SQLMapper.class);
//        //temp = Sever3.recieveStr();
//        System.out.println(mapper.getUserByTableName(temp));
        ;
    }
    @Test
    public void testDeleteMore()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        SQLMapper mapper = sqlSession.getMapper(SQLMapper.class);
        int result = mapper.deleteMore("17,18,19") ;
        System.out.println(result);
    }

    @Test
    public void testGetUserByLike()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        SQLMapper mapper = sqlSession.getMapper(SQLMapper.class);
        List<User> list = mapper.getUserByLike("里");
        System.out.println(list);
    }
}
