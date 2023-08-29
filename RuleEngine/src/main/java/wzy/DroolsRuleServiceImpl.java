package wzy;

import java.util.*;

/**
 * ClassName: DroolsRuleServiceImpl
 * Package: wzy
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/29 - 13:27
 * @Version: v1.0
 */
public class DroolsRuleServiceImpl implements DroolsRuleService {

    private static DroolsRuleServiceImpl droolsRuleServiceimpl ;

    private DroolsManager droolsManager = DroolsManager.getInstance() ;

    static {
        droolsRuleServiceimpl = new DroolsRuleServiceImpl() ;
        DroolsRule drl = new DroolsRule() ;
        String rule = "package first \n" +
                "\n" +
                "rule \"Prevent SQL Injection\" \n" +
                "when \n" +
                "    $s : String( this matches \".*[;\\\\-\\\\'].*\" ) \n" +
                "then \n" +
                "    System.out.println(\"SQL\" + \"Injection\"+\"Warnings!!!这是第一条规则\\n\"); \n" +
                "end";


        drl.setRuleContent(rule);
        drl.setRuleId(1L);
        drl.setKieBaseName("6");
        drl.setKiePackageName("first");

        droolsRuleServiceimpl.addDroolsRule(drl);

        DroolsRule drl2 = new DroolsRule() ;
        String rule2 = "package second \n" +
                "\n" +
                "rule \"Prevent SQL Injection2\" \n" +
                "when \n" +
                "    $s : String( this matches \".*[;\\\\-\\\\'].*\" ) \n" +
                "then \n" +
                "    System.out.println(\"SQL\" + \"Injection\"+\"Warnings!!!这是第二条规则\\n\"); \n" +
                "end";
        drl2.setRuleContent(rule2);
        drl2.setRuleId(8L);
        drl2.setKieBaseName("6");
        drl2.setKiePackageName("second");
        droolsRuleServiceimpl.addDroolsRule(drl2);


        droolsRuleServiceimpl.getDroolsManager().destroy();
        //      droolsRuleServiceimpl.getDroolsManager().deleteDroolsRule(drl,"Prevent SQL Injection");


        DroolsRule drl3 = new DroolsRule() ;
        String rule3 = "package first \n" +
                "\n" +
                "rule \"Prevent SQL Injection3\" \n" +
                "when \n" +
                "    $s : String( this matches \".*[;\\\\-\\\\'].*\" ) \n" +
                "then \n" +
                "    System.out.println(\"SQL\" + \"Injection\"+\"Warnings!!!这是第三条规则\\n\"); \n" +
                "end";
        drl3.setRuleContent(rule3);
        drl3.setRuleId(9L);
        drl3.setKieBaseName("6");
        drl3.setKiePackageName("first");
        droolsRuleServiceimpl.addDroolsRule(drl3);

        DroolsRule drl4 = new DroolsRule() ;
        String rule4 = "package first \n" +
                "\n" +
                "rule \"Prevent SQL Injection4\" \n" +
                "when \n" +
                "    $s : String( this matches \".*[;\\\\-\\\\'].*\" ) \n" +
                "then \n" +
                "    System.out.println(\"SQL\" + \"Injection\"+\"Warnings!!!这是第四条规则\\n\"); \n" +
                "end";
        drl4.setRuleContent(rule4);
        drl4.setRuleId(11L);
        drl4.setKieBaseName("6");
        drl4.setKiePackageName("first");
        droolsRuleServiceimpl.addDroolsRule(drl4);
    }

    /**
     * 模拟数据库
     */
    private Map<Long, DroolsRule> droolsRuleMap = new HashMap<>(16);

    @Override
    public List<DroolsRule> findAll() {
        return new ArrayList<>(droolsRuleMap.values());
    }

    @Override
    public void addDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setCreatedTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    @Override
    public void updateDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setUpdateTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    @Override
    public void deleteDroolsRule(Long ruleId, String ruleName) {
        DroolsRule droolsRule = droolsRuleMap.get(ruleId);
        if (null != droolsRule) {
            droolsRuleMap.remove(ruleId);
            droolsManager.deleteDroolsRule(droolsRule, ruleName);
        }
    }
    public DroolsManager getDroolsManager(){
        return droolsManager ;
    }

    private DroolsRuleServiceImpl(){

    }
    public static DroolsRuleService getInstance(){
        return droolsRuleServiceimpl ;
    }

    public void init(){
        ;
    }
}

