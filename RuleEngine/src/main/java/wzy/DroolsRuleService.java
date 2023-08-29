package wzy;

import java.util.List;

/**
 * ClassName: DroolsRuleService
 * Package: wzy
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/28 - 17:39
 * @Version: v1.0
 */
public interface DroolsRuleService {
    /**
     * 从数据库中加载所有的drools规则
     */
    List<DroolsRule> findAll();

    /**
     * 添加drools规则
     */
    void addDroolsRule(DroolsRule droolsRule);

    /**
     * 修改drools 规则
     */
    void updateDroolsRule(DroolsRule droolsRule);

    /**
     * 删除drools规则
     */
    void deleteDroolsRule(Long ruleId, String ruleName);
}

