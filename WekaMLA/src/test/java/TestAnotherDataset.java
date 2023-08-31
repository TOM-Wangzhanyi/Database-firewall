import org.junit.Test;
import weka.classifiers.Evaluation;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.core.converters.ConverterUtils;
import weka.core.stemmers.Stemmer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;

/**
 * ClassName: TestAnotherDataset
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/29 - 22:55
 * @Version: v1.0
 */
public class TestAnotherDataset {

    @Test
    public void testAnotherDataSet() throws Exception {
        ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("J48.model"));
        J48 model = (J48) ois1.readObject();
        ois1.close();

        //加载向量化方法
        ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("vector-filter.model"));
        StringToWordVector filter = (StringToWordVector) ois2.readObject();
        ois2.close();

        Instances trainData2 = ConverterUtils.DataSource.read("MMM.arff");
        Instances trainData = ConverterUtils.DataSource.read("Test.arff");

        trainData.setClassIndex(1);
        filter.setInputFormat(trainData);
        Instances filteredTemp = Filter.useFilter(trainData, filter);
        filteredTemp.setClassIndex(0);

        trainData2.setClassIndex(0);
        //   System.out.println(filteredTemp.numInstances());
        for (int j = 0; j < filteredTemp.numInstances(); j++) {  //这个方法可以处理instances与instances之间的格式问题
            filteredTemp.instance(j).setDataset(trainData2);
        }

        Evaluation eval = new Evaluation(filteredTemp);
        eval.evaluateModel(model, filteredTemp);
        //Evaluation类中包含的方法
        System.out.println("f1score：" + eval.fMeasure(0));
        System.out.println("precision：" + eval.precision(0)); //错误率
        System.out.println("错误率：" + eval.errorRate()); //错误率
        System.out.println("recall：" + eval.recall(0)); //错误率
    }


    @Test
    public void testBuildDataSet() throws Exception {
        ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("J48.model"));
        J48 model = (J48) ois1.readObject();
        ois1.close();

        //加载向量化方法
        ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("vector-filter.model"));
        StringToWordVector filter = (StringToWordVector) ois2.readObject();
        ois2.close();

        Instances last = ConverterUtils.DataSource.read("MMM.arff");
        Instances TestData = ConverterUtils.DataSource.read("Test.arff");

        TestData.setClassIndex(1);
        filter.setInputFormat(TestData);
        Instances filteredTemp = Filter.useFilter(TestData, filter);
        filteredTemp.setClassIndex(0);

        //借助trainData3的格式，不然预测的时候会报错
        last.setClassIndex(0);
        int all = filteredTemp.numInstances();
        double sum = 0.0;
        for (int j = 0; j < filteredTemp.numInstances(); j++) {  //这个方法可以处理instances与instances之间的格式问题
            filteredTemp.instance(j).setDataset(last);
        }




           /* System.out.println("成功预测的数据有" + sum);
            System.out.println("总共的数有" + filteredTemp.numInstances());
            System.out.println("总共的数有" + all);*/
        Evaluation eval = new Evaluation(filteredTemp);
        eval.evaluateModel(model, filteredTemp);
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toMatrixString());
        // eval.crossValidateModel(model, filteredTemp, 10, new Debug.Random(1));
        //  System.out.println(eval.toSummaryString("\nResult", false));
        //  System.out.println(eval.toClassDetailsString());


    }

    @Test
    public void testInputMapperdClassifier() throws Exception {
        ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("J48.model"));
        J48 model = (J48) ois1.readObject();
        ois1.close();

        //加载向量化方法
        ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("vector-filter.model"));
        StringToWordVector filter = (StringToWordVector) ois2.readObject();
        ois2.close();


        //    Instances trainData = ConverterUtils.DataSource.read("structure.arff");
        Instances last = ConverterUtils.DataSource.read("MMM.arff");
        Instances test = ConverterUtils.DataSource.read("TestData.arff");

        System.out.println("一共有这么多语句哦" + test.numInstances());

        for(int i = 0 ; i < test.numInstances() ; i++) {

            Instances demo = ConverterUtils.DataSource.read("structure.arff");
            String sql = test.instance(i).stringValue(0);

          //  System.out.println(sql);

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
            last.setClassIndex(0);
            for(int j = 0 ; j < filteredTemp.numInstances(); j++){  //这个方法可以处理instances与instances之间的格式问题
                filteredTemp.instance(j).setDataset(last);
            }
            filteredInstance.setDataset(last);
            // System.out.println("向量化后的数据" + filteredInstance);


            double pred = model.classifyInstance(filteredInstance);
            //model.distributionForInstance(filteredInstance) ;


            if (pred < 0.5)

                System.out.println("这是一个安全的sql语句");
            else if (pred >= 0.5)

                System.out.println("这是一个sql注入语句");



        }
    }
}
