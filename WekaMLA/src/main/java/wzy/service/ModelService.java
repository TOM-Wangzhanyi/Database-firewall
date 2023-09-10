package wzy.service;

import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * ClassName: ModelService
 * Package: wzy.service
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/10 - 13:50
 * @Version: v1.0
 */
//讲词法解析的部分注释掉了，目前还没有什么想法
public class ModelService {
    public static void main(String[] args) throws Exception {
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
}
