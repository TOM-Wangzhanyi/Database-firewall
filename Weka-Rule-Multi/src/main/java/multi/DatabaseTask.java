package multi;

import entity.Result;
import org.apache.ibatis.plugin.Invocation;
import rule.DroolsRuleServiceImpl;
import weka.WekaSingelton;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.concurrent.Callable;

/**
 * ClassName: DatabaseTask
 * Package: multi
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/10/6 - 11:35
 * @Version: v1.0
 */
public class DatabaseTask implements Callable<String> {
    private Invocation invocation ;

    @Override
    public String call() throws Exception {
        invocation.proceed() ;
        System.out.println("成功异步执行了");
        return null;
    }

    public DatabaseTask(Invocation invocation){
        this.invocation = invocation ;
    }

}
