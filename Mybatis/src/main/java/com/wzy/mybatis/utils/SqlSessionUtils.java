package com.wzy.mybatis.utils;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * ClassName: SqlSessionUtils
 * Package: com.atguigu.mybatis.utils
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/6 - 19:45
 * @Version: v1.0
 */
public class SqlSessionUtils {
    public static SqlSession getSqlSession()  {
        SqlSession sqlSession = null ;
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            sqlSession = sqlSessionFactory.openSession(true) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSession ;

    }

    public static SqlSession getFireWallSqlSession(){
        Reader reader;
        //获取配置的资源文件
        try {
            reader = Resources.getResourceAsReader("databaseConfigTest.xml");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("找不到databaseConfig.xml文件");
        }
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
        //sqlSession能够执行配置文件中的SQL语句
        SqlSession sqlSession = factory.openSession(true);
        return sqlSession;
    }
}
