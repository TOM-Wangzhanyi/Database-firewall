package wzy.model;

import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * ClassName: LoadModel
 * Package: wzy.model
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/20 - 12:10
 * @Version: v1.0
 */

//加载模型并判断单个sql
public class TestSql {
        public static void testSql(String sql) throws Exception {
            ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("J48.model"));
            J48 model = (J48) ois1.readObject();
            ois1.close();

            //加载向量化方法
            ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("vector-filter.model"));
            StringToWordVector filter = (StringToWordVector) ois2.readObject();
            ois2.close();
            // StringToWordVector filter = new StringToWordVector() ;

            //加载原始数据，用于测试
            Instances trainData = ConverterUtils.DataSource.read("structure.arff");
            Instance instance = new DenseInstance(2);
            instance.setDataset(trainData);
            instance.setValue(0, sql);
            instance.setValue(1,"1");   //没有这个会报错
            System.out.println("原始数据" + instance);
            trainData.add(1,instance);
           // System.out.println(trainData.instance(1));
            trainData.setClassIndex(1);
            filter.setInputFormat(trainData);
            Instances filteredTemp = Filter.useFilter(trainData, filter);
            filteredTemp.setClassIndex(0);
            //   System.out.println("整个向量化的结果"+filteredTemp);
            Instance filteredInstance = filteredTemp.instance(1);


            // System.out.println("向量化后的数据" + filteredInstance);

            double pred = model.classifyInstance(filteredInstance);
            //model.distributionForInstance(filteredInstance) ;
            if(pred < 0.5)
                System.out.println("这是一个安全的sql语句");
            else if(pred >= 0.5)
                System.out.println("这是一个sql注入语句");

        }
}
