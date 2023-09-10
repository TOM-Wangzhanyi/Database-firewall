import com.sun.jndi.rmi.registry.ReferenceWrapper_Stub;
import org.junit.Test;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.converters.ConverterUtils;
import weka.core.stemmers.IteratedLovinsStemmer;
import weka.core.stopwords.MultiStopwords;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.Enumeration;
import java.util.Random;

/**
 * ClassName: TestEvalFc
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/1 - 15:43
 * @Version: v1.0
 */

//这个方法是最终的方法
public class TestEvalFc {
    @Test

    //测试用eval来评测FilterdClaasifer
    public void testEvalFc() throws Exception {
        FilteredClassifier fc = new FilteredClassifier();
        //读取文件
        Instances data = ConverterUtils.DataSource.read("M.arff");
        Instances data2 = ConverterUtils.DataSource.read("TestData.arff");
        Instances demo = ConverterUtils.DataSource.read("demo.arff");
        demo.setClassIndex(1);
        //设置第二列为标签列
        data.setClassIndex(1);
        data2.setClassIndex(1);
        //构造向量化方法并使用
        StringToWordVector filter = new StringToWordVector() ;
        filter.setMinTermFreq(30);
        filter.setWordsToKeep(1200);
        filter.setTokenizer(new WordTokenizer());
        filter.setIDFTransform(true);
        filter.setTFTransform(true);
        filter.setStemmer(new IteratedLovinsStemmer());
        fc.setFilter(filter);
        //关键是这个大小写
     //   filter.setLowerCaseTokens(true);
        J48 tree = new J48() ;
        /*String[] normalOptions = new String[]{"-S","2.0","-T","-1.0"};
        tree.setOptions(normalOptions);*/

        //tree.setSeed(1);
        /*tree.setBinarySplits(true);
        tree.setNumFolds(10);*/
        fc.setClassifier(tree);

        fc.setBatchSize("10");
        Evaluation evaluation = new Evaluation(data) ;
        evaluation.crossValidateModel(fc,data,10, new Random(1));
        Evaluation eval = new Evaluation(data) ;
        eval.crossValidateModel(fc,data,10, new Random(1));

        System.out.println("f1score：" + eval.fMeasure(0));
        System.out.println("precision：" + eval.precision(0)); //错误率
        System.out.println("错误率：" + eval.errorRate()); //错误率
        System.out.println("recall：" + eval.recall(0)); //错误率

    }

    @Test

    //测试FilteredClassifier设置options并用eval来评测模型，并保存模型
    public void testSetOptionAndSaveFc() throws Exception {
        String options[]={"-L","25"} ;
        FilteredClassifier fc = new FilteredClassifier();
        //读取文件
        Instances data = ConverterUtils.DataSource.read("src/main/resources/sqlData/M.arff");
        Instances data2 = ConverterUtils.DataSource.read("src/main/resources/sqlData/TestData.arff");
        Instances demo = ConverterUtils.DataSource.read("src/main/resources/sqlData/demo.arff");
        /*for(int i = 0 ; i < data.numInstances() ; i++){
            String temp = data.instance(i).stringValue(0);
            String tableName ;
            try {
                tableName = JsqlparserUtil.getTableName(temp);
            }
            catch (Exception e){
                tableName = null ;
            }
            temp = temp + "  tableName:" + tableName ;
         //   System.out.println(temp);
            String cloumnNames = null;
            if (null != tableName) {
                JsqlparserUtil jsqlparserUtil = new JsqlparserUtil();
                try {
                    cloumnNames = jsqlparserUtil.getCloumnNames(temp);
                }
                catch (Exception e){
                    cloumnNames = null ;
                }
           //     System.out.println("cloumnNames:" + cloumnNames);
            }
            temp = temp + "  ClonumNames:" + cloumnNames;
            data.instance(i).setValue(0,temp);
        }
        for(int i = 0 ; i < data2.numInstances() ; i++){
            String temp = data2.instance(i).stringValue(0);
            String tableName ;
            try {
                tableName = JsqlparserUtil.getTableName(temp);
            }
            catch (Exception e){
                tableName = null ;
            }
            temp = temp + "  tableName:" + tableName ;
         //   System.out.println(temp);
            String cloumnNames = null;
            if (null != tableName) {
                JsqlparserUtil jsqlparserUtil = new JsqlparserUtil();
                try {
                    cloumnNames = jsqlparserUtil.getCloumnNames(temp);
                }
                catch (Exception e){
                    cloumnNames = null ;
                }
                //     System.out.println("cloumnNames:" + cloumnNames);
            }
            temp = temp + "  ClonumNames:" + cloumnNames;
            data2.instance(i).setValue(0,temp);
        }*/
        demo.setClassIndex(1);
        //设置第二列为标签列
        data.setClassIndex(1);
        data2.setClassIndex(1);
        //构造向量化方法并使用
        StringToWordVector filter = new StringToWordVector() ;
        filter.setMinTermFreq(30);
        filter.setWordsToKeep(1100);
        filter.setTokenizer(new WordTokenizer());
        filter.setIDFTransform(true);
        filter.setTFTransform(true);
        filter.setStemmer(new IteratedLovinsStemmer());
        filter.setStopwordsHandler(new MultiStopwords());
        //关键是这个大小写
        filter.setLowerCaseTokens(true);
        fc.setFilter(filter);
        REPTree tree = new REPTree();
        tree.setOptions(options);
        /*String[] normalOptions = new String[]{"-S","2.0","-T","-1.0"};
        tree.setOptions(normalOptions);*/

        //tree.setSeed(1);
        /*tree.setBinarySplits(true);
        tree.setNumFolds(10);*/
        fc.setClassifier(tree);
        data.randomize(new Random(0));
        fc.buildClassifier(data);

        Evaluation eval = new Evaluation(data) ;
        eval.evaluateModel(fc,data2) ;

        System.out.println("测试数据的f1score：" + eval.fMeasure(1));
        System.out.println("测试数据的precision：" + eval.precision(1)); //错误率
        System.out.println("测试数据的errorRate：" + eval.errorRate()); //错误率
        System.out.println("测试数据的recall：" + eval.recall(1)); //错误率

        //保存模型，参数一为模型保存文件，cfs为要保存的模型
        SerializationHelper.write("src/main/resources/trained-Classifier/fc1.model", fc);
        System.out.println(fc.getClassifier().toString());

    }

    @Test

    //用这个方法就可以加载模型并进行预测了
    public void testLoadFc() throws Exception {
        FilteredClassifier fc = (FilteredClassifier) weka.core.SerializationHelper.read("src/main/resources/trained-Classifier/fc1.model");
     //   System.out.println(fc.toString());
     //   Instances data = ConverterUtils.DataSource.read("src/main/resources/sqlData/MMM.arff");
        Instances data2 = ConverterUtils.DataSource.read("src/main/resources/sqlData/TestData.arff");
        Instances data3 = ConverterUtils.DataSource.read("src/main/resources/sqlData/M.arff");

        /*for(int i = 0 ; i < data3.numInstances() ; i++){
            String temp = data3.instance(i).stringValue(0);
            String tableName ;
            try {
                tableName = JsqlparserUtil.getTableName(temp);
            }
            catch (Exception e){
                tableName = null ;
            }
            temp = temp + "  tableName:" + tableName ;
            //   System.out.println(temp);
            String cloumnNames = null;
            if (null != tableName) {
                JsqlparserUtil jsqlparserUtil = new JsqlparserUtil();
                try {
                    cloumnNames = jsqlparserUtil.getCloumnNames(temp);
                }
                catch (Exception e){
                    cloumnNames = null ;
                }
                //     System.out.println("cloumnNames:" + cloumnNames);
            }
            temp = temp + "  ClonumNames:" + cloumnNames;
            data3.instance(i).setValue(0,temp);
        }
        for(int i = 0 ; i < data2.numInstances() ; i++){
            String temp = data2.instance(i).stringValue(0);
            String tableName ;
            try {
                tableName = JsqlparserUtil.getTableName(temp);
            }
            catch (Exception e){
                tableName = null ;
            }
            temp = temp + "  tableName:" + tableName ;
            //   System.out.println(temp);
            String cloumnNames = null;
            if (null != tableName) {
                JsqlparserUtil jsqlparserUtil = new JsqlparserUtil();
                try {
                    cloumnNames = jsqlparserUtil.getCloumnNames(temp);
                }
                catch (Exception e){
                    cloumnNames = null ;
                }
                //     System.out.println("cloumnNames:" + cloumnNames);
            }
            temp = temp + "  ClonumNames:" + cloumnNames;
            data2.instance(i).setValue(0,temp);
        }*/
        //设置第二列为标签列
     //   data.setClassIndex(1);
        data2.setClassIndex(1);
        data3.setClassIndex(1);

        Evaluation eval = new Evaluation(data2) ;
        eval.evaluateModel(fc,data2) ;

        System.out.println("测试数据的f1score：" + eval.fMeasure(1));
        System.out.println("测试数据precision：" + eval.precision(1)); //错误率
        System.out.println("测试数据的错误率：" + eval.errorRate()); //错误率
        System.out.println("测试数据的recall：" + eval.recall(1)); //错误率
        System.out.println("混淆矩阵" + eval.toMatrixString());

        System.out.println("------------------------------------");

        Evaluation eval2 = new Evaluation(data3) ;
        eval2.evaluateModel(fc,data3) ;

        System.out.println("训练数据f1score：" + eval2.fMeasure(1));
        System.out.println("训练数据的precision：" + eval2.precision(1)); //错误率
        System.out.println("训练数据的错误率：" + eval2.errorRate()); //错误率
        System.out.println("训练数据的recall：" + eval2.recall(1)); //错误率
        System.out.println("混淆矩阵" + eval2.toMatrixString());
    }


    @Test

    //因为这样测试的数据和weka界面上的不一样，通过这个发现模型评价方法的参数设置错误了
    public void testFc() throws Exception {
        FilteredClassifier fc = (FilteredClassifier) weka.core.SerializationHelper.read("src/main/resources/trained-Classifier/fc.model");
        //   System.out.println(fc.toString());
        //   Instances data = ConverterUtils.DataSource.read("src/main/resources/sqlData/MMM.arff");
        Instances data2 = ConverterUtils.DataSource.read("src/main/resources/sqlData/TestData.arff");
        Instances data3 = ConverterUtils.DataSource.read("src/main/resources/sqlData/M.arff");
        //设置第二列为标签列
        //   data.setClassIndex(1);
        data2.setClassIndex(1);
        data3.setClassIndex(1);

        double sum1 = 0 , sum2 = 0 ,sum3 = 0 , sum4 = 0 ;
        for(int i = 0 ; i < data2.numInstances() ; i++){
            if(fc.classifyInstance(data2.instance(i)) > 0.5 && data2.instance(i).classValue() == 1 ) {
                sum1++;
            }
            else if(fc.classifyInstance(data2.instance(i)) > 0.5 && data2.instance(i).classValue() == 0)
                sum3++ ;
            if(fc.classifyInstance(data2.instance(i)) < 0.5 && data2.instance(i).classValue() == 0 )
                sum2++ ;
            else if(fc.classifyInstance(data2.instance(i)) < 0.5 &&  data2.instance(i).classValue() == 1)
                sum4++ ;
        }
        System.out.println("阳性的识别出来了："+ sum1 / data2.numInstances());
        System.out.println("假阳性："+ sum3 / data3.numInstances());
        System.out.println("阴性的被识别出来："+ sum2 / data2.numInstances());
        System.out.println("假阴性："+ sum4 / data2.numInstances());

        System.out.println("数据有"+"sum1:"+sum1 + "sum2"+sum2 +"sum3"+ sum3 +"sum4"+ sum4) ;


        Evaluation eval2 = new Evaluation(data2) ;
        eval2.evaluateModel(fc,data2) ;

        System.out.println("训练数据f1score：" + eval2.fMeasure(0));
        System.out.println("训练数据的precision：" + eval2.precision(0)); //错误率
        System.out.println("训练数据的错误率：" + eval2.errorRate()); //错误率
        System.out.println("训练数据的recall：" + eval2.recall(0)); //错误率

        System.out.println("混淆矩阵" + eval2.toMatrixString());
        System.out.println("混淆矩阵" + eval2.confusionMatrix());
    }


    @Test

    //用这个方法来对单个Sql语句进行评测
    public void testClassifySQL() throws Exception {
        String sql = "SELECT wide ( s )  FROM west" ;
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
       System.out.println("这个是一个安全的Sql语句");
       if(result > 0.5)
           System.out.println("这是一个危险的Sql注入语句");
    }
}
