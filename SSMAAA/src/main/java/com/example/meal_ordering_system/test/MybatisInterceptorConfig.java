package com.example.meal_ordering_system.test;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: aaa
 * Package: com.example.meal_ordering_system.test
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/25 - 16:14
 * @Version: v1.0
 */
@Configuration
public class MybatisInterceptorConfig {

    @Bean
    public String myInterceptor(SqlSessionFactory sqlSessionFactory) {

        SelectPlugin plugin = new SelectPlugin();
    //    sqlSessionFactory.getConfiguration().addInterceptor(plugin);
        sqlSessionFactory.getConfiguration().addInterceptor(new SqlPlugin());
      //  sqlSessionFactory.getConfiguration().addInterceptor(new ResultInterceptor());
        return "interceptor";
    }
}
