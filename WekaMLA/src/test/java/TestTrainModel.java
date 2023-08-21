import org.junit.Test;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

/**
 * ClassName: TestTrainModel
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/21 - 11:21
 * @Version: v1.0
 */
public class TestTrainModel {
    public void testTrainModel() throws Exception {
        Instances data = ConverterUtils.DataSource.read("C:\\\\Users\\\\wzyxi\\\\Desktop\\\\M.arff");//打乱顺
     /*   for(Instance instance : data){  //将sql解析并替换
            SqlToSqlParse.sqlparse(instance) ;
        }*/
        System.out.println("原始数据" + data.instance(0));
        /*ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File("structure.arff"));
        saver.writeBatch();*/

        StringToWordVector filter = new StringToWordVector() ;
        filter.setMinTermFreq(30);
        filter.setWordsToKeep(1200);
        filter.setTokenizer(new WordTokenizer());
        filter.setIDFTransform(true);
        filter.setTFTransform(true);
        data.setClassIndex(1);
        filter.setInputFormat(data);
        //  filter.setAttributeIndices("");
        Instances filterdata = Filter.useFilter(data,filter);
        System.out.println("向量化后的数据" + filterdata.instance(0));
        // System.out.println(data);
        // 假设data是Instances对象
       /* ArffSaver saver = new ArffSaver();
        saver.setInstances(filterdata);

// 指定文件路径
        saver.setFile(new File("C:\\\\Users\\\\wzyxi\\\\Desktop\\\\output1.arff"));

// 保存ARFF文件
        saver.writeBatch();*/
        J48 classifier = new J48();
        //NaiveBayes classifier = new NaiveBayes();
        //SMO classifier = new SMO();
        //代表最后一列的元素作为标签
        // data.setClassIndex(data.numAttributes()-1);
        filterdata.setClassIndex(0);

        Evaluation eval = new Evaluation( filterdata );
        eval.crossValidateModel( classifier, filterdata, 10, new Random(1));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toMatrixString());
        /*//测试预测究竟需要什么类型
        classifier.classifyInstance(data.instance(1)) ;*/
        //保存模型
        ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("J48.model"));
        oos1.writeObject(classifier);
        oos1.close();

        ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("vector-filter.model"));
        oos2.writeObject(filter);
        oos2.close();

    }


    @Test
    public void testVecotrData() throws Exception {
        Instances data = ConverterUtils.DataSource.read("C:\\\\Users\\\\wzyxi\\\\Desktop\\\\M.arff");//打乱顺
        StringToWordVector filter = new StringToWordVector() ;
        filter.setMinTermFreq(30);
        filter.setWordsToKeep(1200);
        filter.setTokenizer(new WordTokenizer());
        filter.setIDFTransform(true);
        filter.setTFTransform(true);
        data.setClassIndex(1);
        filter.setInputFormat(data);
        data = Filter.useFilter(data,filter);

        int trainSize = (int) Math.round(data.numInstances() * 0.80);
        int testSize = data.numInstances() - trainSize;
        Instances train = new Instances(data, 0, trainSize);
        Instances test = new Instances(data, trainSize, testSize);

        J48 tree = new J48();

        train.setClassIndex(0);
        test.setClassIndex(0);


        tree.buildClassifier(train); //训练
        //获取测试集测试后的数据
        double sum = test.numInstances();//测试语料实例数
        double right = 0.0f;

        for(int  i = 0;i<sum;i++)//测试分类结果
        {
            if(tree.classifyInstance(test.instance(i))==test.instance(i).classValue())//如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
            {
                right++;//测试正确
            }
        }
        System.out.println("Decision Tree classification precision:"+(right/sum));

        right = 0.0f ;
        for(int  i = 0;i<data.numInstances();i++)//测试分类结果
        {
            if(tree.classifyInstance(data.instance(i))==data.instance(i).classValue())//如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
            {
                right++;//测试正确
            }
        }
        System.out.println("Decision Tree classification precision:"+(right/data.numInstances()));

        tree.classifyInstance(data.instance(1)) ;
        ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("J48.model"));
        oos1.writeObject(tree);
        oos1.close();

        ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("vector-filter.model"));
        oos2.writeObject(filter);
        oos2.close();

    }

    @Test
    public void testLoadModel() throws Exception {
        Instances data = ConverterUtils.DataSource.read("C:\\\\Users\\\\wzyxi\\\\Desktop\\\\M.arff");//打乱顺

        ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("J48.model"));
        J48 tree = (J48) ois1.readObject();
        ois1.close();

        //加载向量化方法
        ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("vector-filter.model"));
        StringToWordVector filter = (StringToWordVector) ois2.readObject();
        ois2.close();

        data.setClassIndex(1);
        filter.setInputFormat(data);
        data = Filter.useFilter(data,filter);

        int trainSize = (int) Math.round(data.numInstances() * 0.80);
        int testSize = data.numInstances() - trainSize;
        Instances train = new Instances(data, 0, trainSize);
        Instances test = new Instances(data, trainSize, testSize);

        train.setClassIndex(0);
        test.setClassIndex(0);

        double sum = test.numInstances();//测试语料实例数
        double right = 0.0f;

        for(int  i = 0;i<sum;i++)//测试分类结果
        {
            if(tree.classifyInstance(test.instance(i))==test.instance(i).classValue())//如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
            {
                right++;//测试正确
            }
        }
        System.out.println("Decision Tree classification precision:"+(right/sum));
    }
}

