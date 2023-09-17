package multi;

import weka.classifiers.meta.FilteredClassifier;
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
import java.util.concurrent.Callable;

/**
 * ClassName: MLUse
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/22 - 18:44
 * @Version: v1.0
 */
public class MLUse implements Callable<String> {
        private String sql ;
        @Override
        public String call() throws Exception {
            FilteredClassifier fc = (FilteredClassifier) weka.core.SerializationHelper.read("src/main/resources/trained-Classifier/fc.model");
            Instances demo = ConverterUtils.DataSource.read("src/main/resources/sqlData/demo.arff");
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
                return "这个是一个安全的Sql语句";
            if(result > 0.5)
                return "这是一个危险的Sql注入语句";
return null ;
        }
        public MLUse(String str){
            this.sql = str ;
        }
    }

