package wzy.model;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.function.Predicate;

/**
 * ClassName: SingleModel
 * Package: wzy.model
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/31 - 0:50
 * @Version: v1.0
 */
public class SingleModel {
    private static FilteredClassifier fc ;
    static {
        fc = new FilteredClassifier();
        //读取文件
        Instances data = null;
        try {
            data = ConverterUtils.DataSource.read("M.arff");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Instances demo = null;
        try {
            demo = ConverterUtils.DataSource.read("demo.arff");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        demo.setClassIndex(1);
        //设置第二列为标签列
        data.setClassIndex(1);
        //构造向量化方法并使用
        StringToWordVector filter = new StringToWordVector() ;
        filter.setMinTermFreq(30);
        filter.setWordsToKeep(1200);
        filter.setTokenizer(new WordTokenizer());
        filter.setIDFTransform(true);
        filter.setTFTransform(true);
        fc.setFilter(filter);
        fc.setClassifier(new J48());

        try {
            fc.buildClassifier(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SingleModel(){
        ;
    }
    public static FilteredClassifier getInstance(){
        return fc ;
    }

}
