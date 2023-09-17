package testWeka;

import entity.Result;
import org.junit.Test;
import rule.DroolsRuleServiceImpl;
import weka.WekaSingelton;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * ClassName: WekaTest
 * Package: testWeka
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/17 - 14:18
 * @Version: v1.0
 */
public class WekaTest {
    @Test
    public void wekaTest() throws Exception {
            String sql = "SELECT wide ( s )  FROM west" ;
            FilteredClassifier fc = WekaSingelton.getFcInstance() ;
            Instances demo = WekaSingelton.getDemoInstance() ;
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
            double result = fc.classifyInstance(demo.instance(1)) ;
            if(result < 0.5)
                System.out.println("这个是一个安全的Sql语句");
            if(result > 0.5)
                System.out.println("这是一个危险的Sql注入语句");
        }
    }
