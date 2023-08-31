import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.pmml.jaxbbindings.True;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;
import wzy.model.SingleModel;

import java.awt.dnd.DropTarget;
import java.util.TreeSet;

/**
 * ClassName: Test
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/31 - 0:35
 * @Version: v1.0
 */
public class Test {
    @org.junit.Test
    public void test() throws Exception {
        FilteredClassifier fc = new FilteredClassifier();
        //读取文件
        Instances data = ConverterUtils.DataSource.read("M.arff");
        Instances data2 = ConverterUtils.DataSource.read("TestData.arff");
        Instances demo = ConverterUtils.DataSource.read("demo.arff");
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
        //关键是这个大小写
        filter.setLowerCaseTokens(true);
        J48 tree = new J48() ;
        /*tree.setBinarySplits(true);
        tree.setNumFolds(10);*/
        fc.setClassifier(tree);

        fc.setBatchSize("10");
        fc.buildClassifier(data);


        long startTime=System.currentTimeMillis();   //获取开始时间\

        double sum = 0 ;
        double erro0 = 0 ;
        double erro1 = 0 ;
        data2.setClassIndex(1);

        for(int i = 0 ; i < data2.numInstances() ; i++) {

            double[] distribution = fc.distributionForInstance(data2.instance(i));
            if(distribution[0] >distribution[1]) {
                if(data2.instance(i).classValue() == 0)
                    sum++ ;
                else
                    erro0++ ;
                System.out.println("安全语句");
            }
            else {
                if(data2.instance(i).classValue() == 1)
                    sum++ ;
                else
                    erro1++ ;
                System.out.println("注入语句警告！！！！！！！！！！！！！！！！！");
            }
        }

        System.out.println("判断结果"+sum / data2.numInstances());
        System.out.println("结果"+sum);
        System.out.println("个数"+data2.numInstances());
        System.out.println("batchsize"+fc.getBatchSize());

        System.out.println("erro0是"+erro1);
        System.out.println("erro1是"+erro0);

        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

    }

    @org.junit.Test

    //这个方法才是最终的方法，好险好险
    public void testSingle() throws Exception {
        FilteredClassifier fc = SingleModel.getInstance() ;
        Instances demo = ConverterUtils.DataSource.read("demo.arff");
        demo.setClassIndex(1);
        for(int i = 0 ; i < 500 ; i++) {

            double[] distribution = fc.distributionForInstance(demo.instance(i));
            if(distribution[0] >distribution[1])
                System.out.println("可行");
            else
                System.out.println("不行");
        }

    }

    @org.junit.Test

    //这个方法才是最终的方法，好险好险
    public void testInstance() throws Exception {
        FilteredClassifier fc = SingleModel.getInstance() ;
        Instances demo = ConverterUtils.DataSource.read("demo.arff");
        demo.setClassIndex(1);
        Instance instance = new DenseInstance(2) ;
        String sql = "SELECT town ( s )  FROM whom UNION";
        instance.setDataset(demo);
        instance.setValue(0, sql);
        instance.setValue(1, "1");   //没有这个会报错
        instance.setDataset(demo);
            double[] distribution = fc.distributionForInstance(instance);
            if(distribution[0] >distribution[1])
                System.out.println("可行");
            else
                System.out.println("不行");
        }

    }

