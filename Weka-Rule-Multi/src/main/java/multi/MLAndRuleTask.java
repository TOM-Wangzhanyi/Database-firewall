package multi;

import entity.Result;
import rule.DroolsRuleServiceImpl;
import weka.WekaSingelton;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.concurrent.Callable;

/**
 * ClassName: MLAndRule
 * Package: multi
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/17 - 14:48
 * @Version: v1.0
 */
public class MLAndRuleTask implements Callable<String> {
    private String sql;

    @Override
    public String call() throws Exception {
        DroolsRuleServiceImpl droolsRuleServiceimpl = (DroolsRuleServiceImpl) DroolsRuleServiceImpl.getInstance();
        //  droolsRuleServiceimpl.getDroolsManager().fireRule("6","select from rules-");
        Result listResult = droolsRuleServiceimpl.getDroolsManager().fireRule("6", sql);
        if (listResult.getResult() == 100)
            System.out.println("并没有匹配到白名单");
        if (listResult.getResult() == 10)
            System.out.println("匹配到了白名单并且停止匹配");
        FilteredClassifier fc = WekaSingelton.getFcInstance();
        Instances demo = WekaSingelton.getDemoInstance();
        demo.setClassIndex(1);
        Instance instance = new DenseInstance(2);
        //借助trainData2的格式
        instance.setDataset(demo);
        instance.setValue(0, sql);
        instance.setValue(1, "1");   //没有这个会报错
        //  System.out.println("原始数据" + instance);
        demo.add(1, instance);
        // System.out.println(trainData.instance(1));
        demo.setClassIndex(1);
        double result = fc.classifyInstance(demo.instance(1));
        if (result < 0.5)
            System.out.println("这个是一个安全的Sql语句");
        if (result > 0.5)
            System.out.println("这是一个危险的Sql注入语句");
        return "结果看consol输出";
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public MLAndRuleTask(String sql) {
        this.sql = sql;
    }

}
