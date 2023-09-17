package testRule;

import rule.DroolsRule;
import rule.DroolsRuleServiceImpl;

/**
 * ClassName: TestRemoveRule
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/29 - 13:30
 * @Version: v1.0
 */
public class TestRemoveRule {

    //添加了前两条规则，然后destroy，再添加一条规则，并删除 ，注意我还在删除的方法里面添加了两条输出kiebase对应的package语句以及里面的内容
    public static void main(String[] args) {
        DroolsRuleServiceImpl droolsRuleServiceimpl = (DroolsRuleServiceImpl) DroolsRuleServiceImpl.getInstance();
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

        //   droolsRuleServiceimpl.getDroolsManager().deleteDroolsRule(drl3,"Prevent SQL Injection3");

        droolsRuleServiceimpl.getDroolsManager().fireRule("6","select from rules-");

    }

}
