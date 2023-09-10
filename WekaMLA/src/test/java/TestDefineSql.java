import org.junit.Test;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * ClassName: TestDefineSql
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/26 - 16:57
 * @Version: v1.0
 */


//structure.afrr是训练的时候用的数据集    //demo是为了创建instance方便设置的数据集  //MMM是统一向量化后的格式
public class TestDefineSql {
    @Test
    public void testDefineSql() throws Exception {

        String sql = "\\\"1\\\"\\\" where 1051  =  1051 union all select null--\\\"";
        ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("J48.model"));
        J48 model = (J48) ois1.readObject();
        ois1.close();

    //    Instances trainData = ConverterUtils.DataSource.read("structure.arff");
        Instances last = ConverterUtils.DataSource.read("MMM.arff");
        Instances demo = ConverterUtils.DataSource.read("structure.arff");

        StringToWordVector filter = new StringToWordVector();



            Instance instance = new DenseInstance(2);
            //借助trainData2的格式
            instance.setDataset(demo);
            instance.setValue(0, sql);
            instance.setValue(1, "1");   //没有这个会报错
          //  System.out.println("原始数据" + instance);
            demo.add(1, instance);
            // System.out.println(trainData.instance(1));
            demo.setClassIndex(1);
            filter.setInputFormat(demo);
            Instances filteredTemp = Filter.useFilter(demo, filter);
            filteredTemp.setClassIndex(0);
            //   System.out.println("整个向量化的结果"+filteredTemp);
            Instance filteredInstance = filteredTemp.instance(1);

            //借助trainData3的格式，不然预测的时候会报错
            demo.setClassIndex(0);
         /*   for(int j = 0 ; j < filteredTemp.numInstances(); j++){  //这个方法可以处理instances与instances之间的格式问题
                filteredTemp.instance(j).setDataset(last);
            }*/
            last.setClassIndex(0);
            System.out.println(filteredInstance);
          //  filteredInstance.setDataset(last);
       // System.out.println(filteredInstance);
        //System.out.println(last.instance(86));
            // System.out.println("向量化后的数据" + filteredInstance);

            double pred = model.classifyInstance(filteredInstance);
            //model.distributionForInstance(filteredInstance) ;
            if (pred < 0.5)

                System.out.println("这是一个安全的sql语句");
            else if (pred >= 0.5)

                System.out.println("这是一个sql注入语句");



    }
}
