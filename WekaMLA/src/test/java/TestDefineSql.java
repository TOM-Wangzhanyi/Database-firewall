import org.junit.Test;
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
    //这个方法可以作为最终的方法了，目前问题剩下向量化以及单双引号
public class TestDefineSql {
    @Test
    public void testDefineSql() throws Exception {


        long startTime = System.currentTimeMillis();

        String sql = "SELECT * FROM cream WHERE break = 'around 88888888888 ";
        ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("J48.model"));
        J48 model = (J48) ois1.readObject();
        ois1.close();

        //加载向量化方法
        ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("vector-filter.model"));
        StringToWordVector filter = (StringToWordVector) ois2.readObject();
        ois2.close();


    //    Instances trainData = ConverterUtils.DataSource.read("structure.arff");
        Instances trainData3 = ConverterUtils.DataSource.read("MMM.arff");
        Instances trainData2 = ConverterUtils.DataSource.read("demo.arff");

        for(int i = 0 ; i < 1000 ; i++) {

            Instance instance = new DenseInstance(2);
            //借助trainData2的格式
            instance.setDataset(trainData2);
            instance.setValue(0, sql);
            instance.setValue(1, "1");   //没有这个会报错
          //  System.out.println("原始数据" + instance);
            trainData2.add(1, instance);
            // System.out.println(trainData.instance(1));
            trainData2.setClassIndex(1);
            filter.setInputFormat(trainData2);
            Instances filteredTemp = Filter.useFilter(trainData2, filter);
            filteredTemp.setClassIndex(0);
            //   System.out.println("整个向量化的结果"+filteredTemp);
            Instance filteredInstance = filteredTemp.instance(1);

            //借助trainData3的格式，不然预测的时候会报错
            trainData3.setClassIndex(0);
            filteredInstance.setDataset(trainData3);
            // System.out.println("向量化后的数据" + filteredInstance);


            double pred = model.classifyInstance(filteredInstance);
            //model.distributionForInstance(filteredInstance) ;
            if (pred < 0.5)
                ;
             //   System.out.println("这是一个安全的sql语句");
            else if (pred >= 0.5)
                ;
              //  System.out.println("这是一个sql注入语句");


        }
        long overTime = System.currentTimeMillis();      //获取结束时间
        System.out.println("程序运行时间为："+(overTime-startTime)+"毫秒");

    }
}
