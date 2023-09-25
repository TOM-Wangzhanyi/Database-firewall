package com.wzy.mybatis.mapper;

import com.wzy.mybatis.pojo.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ClassName: SelectMapper
 * Package: com.atguigu.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/7 - 16:51
 * @Version: v1.0
 */
public interface SelectMapper {
    /**
     * 根据id查询用户信息
     */
    User getUserById(@Param("id") Integer id) ;
    /**
     * 查询所有的用户信息
     */
    List<User> getAllUser() ;
    /**
     * 查询用户表的总记录数
     */
    Integer getCount() ;
    /**
     * 根据id查询用户信息为一个map集合
     */
    @MapKey("id")
    Map<String , Object> getUserByIdToMap(@Param("id") Integer id);
    /**
     * 根据email查询用户信息为一个map集合 发现就是查这个eamil返回地址
     */
    Map<String , Object> getUserByEmailToMap(@Param("email") String email);

    /**
     * 查询多个用户信息为map集合
     */
    @MapKey("id")
    Map<String , Object> getAllUserToMap();
}
