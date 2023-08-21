package wzy.model;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

/**
 * ClassName: TrainAndSaveModel
 * Package: wzy.model
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/20 - 11:53
 * @Version: v1.0
 */

//向量化数据并训练模型，并保存模型
    //获得可以保存的模型，必须使用buildClassifier来训练
public class TrainAndSaveModel {
    public static void trainAndSaveModel() throws Exception {
        //读取文件
        Instances data = ConverterUtils.DataSource.read("C:\\\\Users\\\\wzyxi\\\\Desktop\\\\M.arff");
        //构造向量化方法并使用
        StringToWordVector filter = new StringToWordVector() ;
        filter.setMinTermFreq(30);
        filter.setWordsToKeep(1200);
        filter.setTokenizer(new WordTokenizer());
        filter.setIDFTransform(true);
        filter.setTFTransform(true);
        //设置第二列为标签列
        data.setClassIndex(1);
        //向量化data
        filter.setInputFormat(data);
        data = Filter.useFilter(data,filter);

        //分割数据集
        int trainSize = (int) Math.round(data.numInstances() * 0.80);
        int testSize = data.numInstances() - trainSize;
        Instances train = new Instances(data, 0, trainSize);
        Instances test = new Instances(data, trainSize, testSize);
        //定义决策树模型
        J48 tree = new J48();
        //设置数据集的标签列，因为向量化后列会发生改变
        train.setClassIndex(0);
        test.setClassIndex(0);

        //训练模型
        tree.buildClassifier(train); //训练

        Evaluation eval = new Evaluation(test);
        eval.evaluateModel(tree, test);
        //Evaluation类中包含的方法
        System.out.println("f1score：" + eval.fMeasure(0));
        System.out.println("precision：" + eval.precision(0)); //错误率
        System.out.println("错误率：" + eval.errorRate()); //错误率
        System.out.println("recall：" + eval.recall(0)); //错误率

        ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("J48.model"));
        oos1.writeObject(tree);
        oos1.close();

        ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("vector-filter.model"));
        oos2.writeObject(filter);
        oos2.close();
    }
}
