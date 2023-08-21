package com.wzy.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * ClassName: DataMapper
 * Package: com.wzy.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/4/21 - 21:58
 * @Version: v1.0
 */
public interface DataMapper {
    int insertRules(@Param("ruledata") String ruledata);

    String selectRules(@Param("id") Integer id);
}
