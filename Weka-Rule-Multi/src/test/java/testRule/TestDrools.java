package testRule;

import rule.DroolsRule;
import rule.DroolsRuleServiceImpl;
/**
 * ClassName: TestDrools
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/29 - 13:30
 * @Version: v1.0
 */
public class TestDrools {
    public static void main(String[] args) {
        DroolsRule drl = new DroolsRule() ;
        String rule = "rule \"Prevent SQL Injection\"\n" +
                "when\n" +
                "    $s : String( this matches \".*[;\\\\-\\\\'].*\" )\n" +
                "then\n" +
                "    System.out.println(\"SQL\" + \"Injection\"+\"Warnings!!!这是第一条规则\\n\");\n" +
                "end" ;


        drl.setRuleContent(rule);
        drl.setRuleId(1L);
        drl.setKieBaseName("8");
        drl.setKiePackageName("firstPackage");
        DroolsRuleServiceImpl droolsRuleServiceimpl = (DroolsRuleServiceImpl) DroolsRuleServiceImpl.getInstance();
        droolsRuleServiceimpl.addDroolsRule(drl);

        droolsRuleServiceimpl.getDroolsManager().destroy();


        DroolsRule drl2 = new DroolsRule() ;
        String rule2 = "rule \"Prevent SQL Injection2\"\n" +
                "when\n" +
                "    $s : String( this matches \".*[;\\\\-\\\\'].*\" )\n" +
                "then\n" +
                "    System.out.println(\"SQL\" + \"Injection\"+\"Warnings!!!这是第二条规则\\n\");\n" +
                "end" ;
        drl2.setRuleContent(rule2);
        drl2.setRuleId(8L);
        drl2.setKieBaseName("8");
        drl2.setKiePackageName("firstPackage8");
        droolsRuleServiceimpl.addDroolsRule(drl2);


        DroolsRule drl3 = new DroolsRule() ;
        String rule3 = "rule \"Prevent SQL Injection3\"\n" +
                "when\n" +
                "    $s : String( this matches \".*[;\\\\-\\\\'].*\" )\n" +
                "then\n" +
                "    System.out.println(\"SQL\" + \"Injection\"+\"Warnings!!!这是第三条规则\\n\");\n" +
                "end" ;
        drl3.setRuleContent(rule3);
        drl3.setRuleId(9L);
        drl3.setKieBaseName("8");
        drl3.setKiePackageName("firstPackage8");
        droolsRuleServiceimpl.addDroolsRule(drl3);

        droolsRuleServiceimpl.getDroolsManager().fireRule("8","select from rules-");

    }

    //相同kiebase，里面可以包含不同的package，存储不同的rule
    //想要实现kiebase的隔离，注意这个包的名字是不可以重复的，不然会失去隔离效果


    //全部更新，关键kiebase要设为空，不然会导致找包的时候报空指针
}

