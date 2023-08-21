package com.wzy.test;

import com.wzy.mybatis.mapper.ParameterMapper;
import com.wzy.mybatis.pojo.User;
import com.wzy.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ParameterMapeerTest
 * Package: com.atguigu.mybatis.test
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/6 - 19:42
 * @Version: v1.0
 */
public class ParameterMapeerTest {
    /**
     * mybatis获取参数值的两种方式 ${} , #{}
     *第一种情况  mapper接口的参数为单个的字面量类型
     * 可以通过$#{}来获取参数值，但是需要注意${}的单引号问题
     * 2. mapper接口的参数为多个时
     *mybatis会将参数放在一个map集合中，以两种方式进行存储
     * a>以arg0 ,arg1...为键，以参数为值
     * b>以param1 ,param2...为键，以参数为值
     * 因此只需要通过#{}${}以键的方式来访问值即可。
     * 3 .若mapper接口方法的参数有多个时，我们可以手动将这些参数放在map中
     *只需要通过#{}${}以键的方式来访问值即可。 相当于是自己设置了键，不需要mybatis来设置了
     * 4.mapper接口方法的参数是一个实体类类型的参数的时候
     *只需要通过#{}${}以属性的方式访问值，这里的属性必须要在User类中有getset方法。会根据#${}中的String来对相关变量进行访问
     * 5.第二种的变形，用@param注解来设置mybatis创建的map的键值的名字，也可以param1,param2访问
     *
     *
     */

    @Test
    public void testCheckLoginByParam()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        User user = mapper.checkLoginByParam("里斯","123456");
        System.out.println(user);
    }

    @Test
    public void testInsertUser()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
       int result = mapper.insertUser(new User(null,"六六","123456",23,"女","123@qq.com"));
        System.out.println(result);
    }

    @Test
    public void testCheckLoginByMap()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        Map<String,Object> map = new HashMap<String , Object>() ;
        map.put("username","张三");
        map.put("password","123456");
        User user = mapper.checkLoginByMap(map);
        System.out.println(user);
    }

    @Test
    public void testCheckLogin()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        User user = mapper.checkLogin("张三","123456");
        System.out.println(user);
    }

    @Test
    public void testGetUserByUsername()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        User user = mapper.getUserByUserName("张三") ;
        System.out.println(user);
    }

    @Test
    public void testgetAllUser()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        List<User> list = mapper.getAllUser();
        for(User user : list)
        {
            System.out.println(user);
        }
    }
    @Test
    public void  testJDBC() throws Exception {
        String username = "admin" ;
        Class.forName("");
        Connection connection = DriverManager.getConnection("", "", "");
        //connection.prepareStatement("select * from t_user where username = '"+username+"'")

    }

}
