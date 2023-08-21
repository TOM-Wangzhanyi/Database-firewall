package com.wzy.mybatis.mapper;

import com.wzy.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: UserMapper
 * Package: com.atguigu.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/3 - 17:06
 * @Version: v1.0
 */
public interface UserMapper {
    /**
     * 添加用户信息
     */
    int insertUser();
    /**
     * 修改用户信息
     */
    void updateUser();
    /**
     * 删除用户信息
     */
    void deleteUser();
    /**
     * 根据id查询用户信息
     */

    /**
     * 查询所有用户信息
     */
    List<User> getAllUser() ;


}
