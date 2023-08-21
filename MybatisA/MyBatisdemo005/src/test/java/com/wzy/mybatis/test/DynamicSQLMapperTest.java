package com.wzy.mybatis.test;

import com.sun.corba.se.spi.orb.ParserImplBase;
import com.wzy.mybatis.mapper.DynamicSQLMapper;
import com.wzy.mybatis.pojo.Emp;
import com.wzy.mybatis.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * ClassName: DynamicSQLMapperTest
 * Package: com.wzy.mybatis.test
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/20 - 20:47
 * @Version: v1.0
 */
public class DynamicSQLMapperTest {
    /**
     * 动态SQL
     * 1.if：根据标签中的test属性所对应的表达式，来决定标签中的内容是否需要拼接到sql中
     *2. where:
     * 当where标签中有内容时，会自动生成where关键字，并且将内容“前”多余的and或or去掉，
     * 需要注意的是，where标签不能将其内容后面的and或or去掉，只能去掉前面多余的and或or
     * 当where标签中没有内容时，此时where标签没有任何效果
     * 3.trim标签
     * 若标签中有内容：
     * prefix|suffix: 将trim标签中内容前面或后面添加指定内容
     * suffixOverrides|prefixOverrides:将trim标签中内容前面或后面去掉指定内容
     *若标签中没有内容，trim标签也没有任何效果
     * 4.choose,when,otherwise ,相当于java中的if...else if....else
     *choose相当于是一个父标签，里面来放when,otherwise。
     * when表示if else if （最多只有一个when可以执行
     * otherwise表示else（只能有一个，当when全部失效时执行
     *5.foreach
     * collection:用来设置需要循环的数组或集合
     *item:用来表示数组或集合中每一个数据
     *separator:循环体之间的分隔符
     *open：foreach标签所循环的所有内容的开始符
     *close:foreach标签所循环的所有内容的结束符
     * 6.sql标签
     * 声明：<sql id="empColumns">eid,emp_name,age,sex,email</sql> 设置sql片段
     * 引用：<include refid="empColumns"></include> 引用sql片段
     *
     */

    @Test
    public void testInsertMoreByList(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
        Emp emp1 = new Emp(null,"a1",23,"男","123@qq.com") ;
        Emp emp2 = new Emp(null,"a2",23,"男","123@qq.com") ;
        Emp emp3 = new Emp(null,"a3",23,"男","123@qq.com") ;
        Emp emp4 = new Emp(null,"a4",23,"男","123@qq.com") ;
        List<Emp> emps = Arrays.asList(emp1, emp2, emp3, emp4);
        int result = mapper.insertMoreByList(emps);
//        for(Emp emp : list)
        System.out.println(result);
    }


    @Test
    public void testDeleteMoreByArray(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
        int result = mapper.deleteMoreByArray(new Integer[]{6,7,8});
//        for(Emp emp : list)
        System.out.println(result);
    }
    @Test
    public void testGetEmpByChoose(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
        List<Emp> list = mapper.getEmpByChoose(new Emp(null, "张三", 21, "男", "123@qq.com"));
//        for(Emp emp : list)
        System.out.println(list);
    }

    @Test
    public void testGetEmpByString(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
        List<Emp> list = mapper.getEmpByCondition(new Emp(null, "", 21, "", ""));
//        for(Emp emp : list)
            System.out.println(list);
    }
}
