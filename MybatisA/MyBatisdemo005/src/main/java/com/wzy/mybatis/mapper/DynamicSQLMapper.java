package com.wzy.mybatis.mapper;

import com.wzy.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: DynamicSQLMapper
 * Package: com.wzy.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/20 - 20:37
 * @Version: v1.0
 */
public interface DynamicSQLMapper {
    /**
     * 实现多条件查询
     */
    List<Emp> getEmpByCondition(Emp emp);

    /**
     * 用来测试choose，when，otherwise这三个标签
     */
    List<Emp> getEmpByChoose(Emp emp) ;
    /**
     * 通过数组实现批量删除
     */
    int deleteMoreByArray(@Param("eids") Integer[] eids);

    /**
     * 通过list集合实现批量添加的功能
     */
    int insertMoreByList(@Param("emps") List<Emp> emps);
}
