package com.wzy.mybatis.mapper;

import com.wzy.mybatis.pojo.Dept;
import com.wzy.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: DeptMapper
 * Package: com.wzy.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/19 - 16:15
 * @Version: v1.0
 */
public interface DeptMapper {

    /**
     * 通过分步查询查询员工以及员工所对应的部门信息
     * 分步查询的第二步；通过did查询员工所对应的信息
     */
    Dept getEmpAndDeptByStepTwo(@Param("did") Integer did) ;

    /**
     * 部门以及部门中所有的员工信息
     */
    Dept getDeptAndEmp(@Param("did") Integer did);

    /**
     * 通过分部查询查询部门中以及部门中所有员工的信息
     * 分步查询第一步：查询部门信息
     */
    Dept getDeptAndEmpByStepOne(@Param("did") Integer did);

    /**
     * 测试netty用String查询数据
     */
    Emp getEmpByString(@Param("str") String str) ;

}
