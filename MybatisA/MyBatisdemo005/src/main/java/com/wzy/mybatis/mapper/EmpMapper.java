package com.wzy.mybatis.mapper;

import com.wzy.mybatis.pojo.Dept;
import com.wzy.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: EmpMapper
 * Package: com.wzy.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/19 - 16:14
 * @Version: v1.0
 */
public interface EmpMapper {

    /**
     * 查询所有的员工信息
     */
List<Emp> getAllEmp() ;

/**
 * 查询员工以及员工所对应的部门信息
 */
Emp getEmpAndDept(@Param("eid") Integer eid) ;

    /**
     * 通过分步查询查询员工以及员工所对应的部门信息
     * 分步查询的第一步；查询员工信息
     */
    Emp getEmpAndDeptByStepOne(@Param("eid") Integer eid);

    /**
     * 通过分部查询查询部门中以及部门中所有员工的信息
     * 分步查询第二步：根据did查询员工信息
     */
    List<Emp> getDeptAndEmpByStepTwo(@Param("did") Integer did) ;
}
