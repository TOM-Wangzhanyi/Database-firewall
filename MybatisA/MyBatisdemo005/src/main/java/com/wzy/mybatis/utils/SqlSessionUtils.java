package com.wzy.mybatis.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

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
}
