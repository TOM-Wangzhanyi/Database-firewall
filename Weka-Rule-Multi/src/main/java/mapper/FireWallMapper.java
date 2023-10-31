package mapper;

import entity.ModelResult;
import rule.DroolsRule;

import java.util.List;

/**
 * ClassName: SelectMapper
 * Package: com.atguigu.mybatis.mapper
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/7 - 16:51
 * @Version: v1.0
 */
public interface FireWallMapper {


    List<DroolsRule> getAllRule() ;

    int InsertResult(ModelResult modelResult);
}
