package com.wzy.mybatis.mapper;

import com.wzy.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ClassName: ParameterMapper
 * Package: com.atguigu.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/6 - 19:35
 * @Version: v1.0
 */
public interface ParameterMapper {

    /**
     * 还是验证登录（参数为map集合）
     */
    User checkLoginByMap(Map<String , Object> map);

    /**
     * 验证登录（使用@param注解）
     */
    User checkLoginByParam(@Param("username") String username , @Param("password") String password);
    /**
     * 添加用户信息
     *
     */
    int insertUser(User user);

    /**
     * 验证登录
     *
     */
    User checkLogin(String username,String password) ;

    /**
     * 根据用户名查询用户信息
     */
    User getUserByUserName(String username) ;

    /**
     * 查询所有的员工信息
     */
    List<User> getAllUser() ;

}
