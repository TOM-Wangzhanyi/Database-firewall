package com.wzy.test;

import com.wzy.mybatis.mapper.SelectMapper;
import com.wzy.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Map;

/**
 * ClassName: SelectMapperTest
 * Package: com.atguigu.mybatis.test
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/7 - 16:55
 * @Version: v1.0
 */

/**
 * Mybatis的各种查询功能
 * 1.如果查询出来的数据只有一条，那么可以用实体类对象或者集合来接收
 * 2.如果是多条数据，那么就一定不能用实体类来接收，会抛异常TooManyResultException
 * 只能用list集合来接收
 *
 * MyBatis中设置了默认的类型别名
 * java.lang.Integer --> int integer
 * int --> _int _integer
 * Map --> map
 * String --> string
 *
 * 3.可以用map来保存，哈希存储是乱序的，当我们查询到结果是没有对应实体类的时候，可以用map
 *比如说多表查询，并且直接输出对象依靠的是toSting方法
 * 但是不知道为什么我这个输出email，输出的是地址.
 *
 * 一条数据转换为map集合来接收，那么多条数据转换为Map的List集合来接收
 * 也可以用@mapkey注解，指定以什么为key，那么一条数据就组成了一个集合放在Object里面
 */
public class SelectMapperTest {

    @Test
    public void testGetAllUserToMap()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        System.out.println(mapper.getAllUserToMap());
    }

    @Test
    public void testGetUserByEmailToMap()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        Map<String, Object> userMap = mapper.getUserByEmailToMap("123@qq.com") ;
        System.out.println(userMap);
    }

    @Test
    public void testGetUserByIdToMap()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        Map<String, Object> userMap = mapper.getUserByIdToMap(7) ;
        System.out.println(userMap);
    }

    @Test
    public void testGetCount()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        System.out.println(mapper.getCount());
    }

    @Test
    public void testGetAllUser()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        System.out.println(mapper.getAllUser());
    }

    @Test
    public void testGetUserById()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
        System.out.println(mapper.getUserById(22));
    }
}
