import org.junit.Test;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;
import weka.core.stemmers.IteratedLovinsStemmer;
import weka.core.stopwords.MultiStopwords;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

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
        filter.setLowerCaseTokens(true);
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
        String options[]=new String[9];//训练参数数组

        options[0]="-P";

        options[1] = "100" ;

        options[2]="-batch-size";

        options[3]="150";

        options[4]="-I";

        options[5]="100";

        options[6] ="-B";

        options[7] = "-depth" ;
        options[8] = "0" ;
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
        filter.setWordsToKeep(1100);
        filter.setTokenizer(new WordTokenizer());
        filter.setIDFTransform(true);
        filter.setTFTransform(true);
        filter.setStemmer(new IteratedLovinsStemmer());
        fc.setFilter(filter);
        filter.setStopwordsHandler(new MultiStopwords());
        //关键是这个大小写
        filter.setLowerCaseTokens(true);
        RandomForest tree = new RandomForest() ;
        tree.setOptions(options);
        /*String[] normalOptions = new String[]{"-S","2.0","-T","-1.0"};
        tree.setOptions(normalOptions);*/

        //tree.setSeed(1);
        /*tree.setBinarySplits(true);
        tree.setNumFolds(10);*/
        fc.setClassifier(tree);
        fc.buildClassifier(data);

        Evaluation eval = new Evaluation(data) ;
        eval.evaluateModel(fc,data2) ;

        System.out.println("f1score：" + eval.fMeasure(0));
        System.out.println("precision：" + eval.precision(0)); //错误率
        System.out.println("错误率：" + eval.errorRate()); //错误率
        System.out.println("recall：" + eval.recall(0)); //错误率

        //保存模型，参数一为模型保存文件，cfs为要保存的模型
        SerializationHelper.write("fc.model", fc);
        System.out.println(fc.getClassifier().toString());

    }

    @Test

    //用这个方法就可以加载模型并判断模型了
    public void testLoadFc() throws Exception {
        FilteredClassifier fc = (FilteredClassifier) weka.core.SerializationHelper.read("fc.model");
     //   System.out.println(fc.toString());
        Instances data = ConverterUtils.DataSource.read("M.arff");
        Instances data2 = ConverterUtils.DataSource.read("TestData.arff");
        Instances data3 = ConverterUtils.DataSource.read("M.arff");
        //设置第二列为标签列
        data.setClassIndex(1);
        data2.setClassIndex(1);
        data3.setClassIndex(1);

        Evaluation eval = new Evaluation(data) ;
        eval.evaluateModel(fc,data2) ;

        System.out.println("测试数据的f1score：" + eval.fMeasure(0));
        System.out.println("测试数据precision：" + eval.precision(0)); //错误率
        System.out.println("测试数据的错误率：" + eval.errorRate()); //错误率
        System.out.println("测试数据的recall：" + eval.recall(0)); //错误率

        System.out.println("------------------------------------");


        Evaluation eval2 = new Evaluation(data) ;
        eval2.evaluateModel(fc,data3) ;

        System.out.println("训练数据f1score：" + eval2.fMeasure(0));
        System.out.println("训练数据的precision：" + eval2.precision(0)); //错误率
        System.out.println("训练数据的错误率：" + eval2.errorRate()); //错误率
        System.out.println("训练数据的recall：" + eval2.recall(0)); //错误率

    }
}
