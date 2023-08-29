import org.junit.jupiter.api.Test;
import wzy.DroolsRule;
import wzy.DroolsRuleServiceImpl;

/**
 * ClassName: TestSingleDroolsManager
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/29 - 13:31
 * @Version: v1.0
 */
public class TestSingleDroolsManager {
    @Test
    public void useSingleDroolsManger(){

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

    @Test

    //调用单例的DroolsRuleServiceImpl,里面单例实现了DroolsManager,init相当于初始化了manager的判断逻辑
    public void testStatic(){
        DroolsRuleServiceImpl droolsRuleServiceimpl = (DroolsRuleServiceImpl) DroolsRuleServiceImpl.getInstance();
        droolsRuleServiceimpl.init();
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
        droolsRuleServiceimpl.deleteDroolsRule(11L,"Prevent SQL Injection4");
        droolsRuleServiceimpl.getDroolsManager().fireRule("6","select from rules-");
    }

    @Test
    public void staticBlock(){
        DroolsRuleServiceImpl droolsRuleServiceimpl = (DroolsRuleServiceImpl) DroolsRuleServiceImpl.getInstance();
        droolsRuleServiceimpl.getDroolsManager().fireRule("6","select from rules-");
    }
}

