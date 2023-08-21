package wzy.model;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.Random;

/**
 * ClassName: TrainModel
 * Package: wzy.model
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/20 - 11:47
 * @Version: v1.0
 */

//训练模型并评测结果
public class TrainModel {
    public static void trainJ48() throws Exception {
        //读取文件
        Instances data = ConverterUtils.DataSource.read("C:\\\\Users\\\\wzyxi\\\\Desktop\\\\MMM.arff");
        //对Weka中的数据集data进行随机化混洗
        data.randomize(new Random(0));
        //下面四行是分组，8：2
        int trainSize = (int) Math.round(data.numInstances() * 0.80);
        int testSize = data.numInstances() - trainSize;
        Instances train = new Instances(data, 0, trainSize);
        Instances test = new Instances(data, trainSize, testSize);
        //设置训练集和测试机中标签的下标
        train.setClassIndex(0);
        test.setClassIndex(0);
        //选用J48决策树模型
        J48 tree = new J48();
        //训练模型
        tree.buildClassifier(train); //训练
        //获取测试集测试后的数据
        Evaluation eval = new Evaluation(test);
        eval.evaluateModel(tree, test);
        //Evaluation类中包含的方法
        System.out.println("f1score：" + eval.fMeasure(0));
        System.out.println("precision：" + eval.precision(0)); //错误率
        System.out.println("错误率：" + eval.errorRate()); //错误率
        System.out.println("recall：" + eval.recall(0)); //错误率

    }
    //测试J48的效果，用官方的评测指标
    public static void evaluateJ48(Instances data) throws Exception
    {
        J48 classifier = new J48();
        data.setClassIndex(0);
        Evaluation eval = new Evaluation( data );
        eval.crossValidateModel( classifier, data, 10, new Random(1));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toMatrixString());
    }
    //评测随机森林的
    public static void evaluateRepTree(Instances data) throws Exception {
        RandomForest classifier = new RandomForest() ;
        data.setClassIndex(0);
        Evaluation eval = new Evaluation( data );
        eval.crossValidateModel( classifier, data, 10, new Random(1));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toMatrixString());
        StringToWordVector sw = new StringToWordVector() ;
    }
}
