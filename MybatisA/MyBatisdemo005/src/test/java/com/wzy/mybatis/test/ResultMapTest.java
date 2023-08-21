package com.wzy.mybatis.test;

import com.wzy.mybatis.mapper.DataMapper;
import com.wzy.mybatis.mapper.DeptMapper;
import com.wzy.mybatis.mapper.EmpMapper;
import com.wzy.mybatis.pojo.Dept;
import com.wzy.mybatis.pojo.Emp;
import com.wzy.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * ClassName: ResultMapTest
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/19 - 16:19
 * @Version: v1.0
 */
public class ResultMapTest {
    /**
     * 解决字段名和属性名不一致的情况“（数据库的命名规则与java的不同
     * 1.为字段取别名，来和类的属性名一致
     * 2.设置全局配置文件，将下划线自动映射为驼峰 <setting name="mapUnderscoreToCamelCase" value="true"/>
     * 3.通过ResultMap的方式来设置自定义的映射关系
     * <resultMap id="empResultMap" type="Emp">
     *         <id property="eid" column="eid"></id>
     *         <result property="empName" column="emp_name"></result>
     *         <result property="age" column="age"></result>
     *         <result property="sex" column="sex"></result>
     *         <result property="email" column="email"></result>
     *     </resultMap>
     *
     *     处理多对一的映射关系
     *
     *     1.级联属性赋值
     *     2.association
     *     3.分步查询
     *
     *     处理一对多的关系
     *     1.collection
     *     2.分布查询
     */
    @Test
    public void getAllEmp()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession() ;
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        List<Emp> list = mapper.getAllEmp();
        for(Emp emp : list)
        {
            System.out.println(emp);
        }
    }

    @Test
    public void getEmpAndDept()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession() ;
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp = mapper.getEmpAndDept(1);
        System.out.println(emp);

    }

    @Test
    public void getEmpAndDeptByStep()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession() ;
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp = mapper.getEmpAndDeptByStepOne(1);
        System.out.println(emp.getEmpName());
        System.out.println("+++++++++++++++++++++++++++++");
        System.out.println(emp.getDept());

    }
    @Test
    public void testGetDeptAndEmp()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession() ;
        DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
        Dept dept = mapper.getDeptAndEmp(1);
        System.out.println(dept);

    }

    @Test
    public void testGetDeptAndEmpByStep()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession() ;
        DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
        Dept dept = mapper.getDeptAndEmpByStepOne(1);
        System.out.println(dept.getDeptName());

    }

    @Test
    public void testNettyStringTrans()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession() ;
        DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
        Emp emp = mapper.getEmpByString("y");
        System.out.println(emp);

    }



    @Test
    public void testPutDataRules()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession() ;
        DataMapper mapper = sqlSession.getMapper(DataMapper.class);
        mapper.insertRules("rule \"Prevent SQL Injection\"\n" +
                "when\n" +
                "    $s : String( this matches \".*[;\\\\-\\\\'].*\" )\n" +
                "then\n" +
                "    throw new SecurityException(\"SQL Injection detected: \" + $s);\n" +
                "end");

    }

    @Test
    public void testGetDataRules()
    {
        SqlSession sqlSession = SqlSessionUtils.getSqlSession() ;
        DataMapper mapper = sqlSession.getMapper(DataMapper.class);
        String rule = mapper.selectRules(1);
        System.out.println(rule);

    }

}
