package com.wzy.mybatis.mapper;

import com.wzy.mybatis.pojo.TestIntercepter;

/**
 * ClassName: TestMapper
 * Package: com.wzy.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/6/1 - 17:53
 * @Version: v1.0
 */
public interface TestMapper {
    int testInsertIntercept(TestIntercepter testIntercepter);
}
