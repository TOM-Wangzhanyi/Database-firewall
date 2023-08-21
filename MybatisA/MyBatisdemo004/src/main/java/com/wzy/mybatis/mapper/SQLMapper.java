package com.wzy.mybatis.mapper;

import com.wzy.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: SQLMapper
 * Package: com.atguigu.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/8 - 15:40
 * @Version: v1.0
 */
public interface SQLMapper {
    /**
     * 根据用户名模糊查询用户名
     */
    List<User> getUserByLike(@Param("username") String username) ;
    /**
     * 批量删除
     */
   Integer deleteMore(@Param("ids") String ids) ;
    /**
     * 查询指定表中的数据
     */
    List<User> getUserByTableName(@Param("tableName") String tableNmae) ;
}
