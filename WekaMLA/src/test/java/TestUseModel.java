import org.junit.Test;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import wzy.model.TestSql;
import wzy.model.TrainAndSaveModel;
import wzy.model.TrainModel;

/**
 * ClassName: TestTrainModel
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/20 - 12:05
 * @Version: v1.0
 */
public class TestUseModel {
    @Test
    public void testTrainJ48() throws Exception {
        TrainModel.trainJ48();
    }
    @Test
    public void evalJ48() throws Exception {
        Instances data = ConverterUtils.DataSource.read("C:\\\\Users\\\\wzyxi\\\\Desktop\\\\MMM.arff");
        TrainModel.evaluateJ48(data);
    }
    @Test
    public void testEvalRF() throws Exception {
        Instances data = ConverterUtils.DataSource.read("C:\\\\Users\\\\wzyxi\\\\Desktop\\\\MMM.arff");
        TrainModel.evaluateRepTree(data);
    }
    @Test
    public void testTrainJ48WithVector() throws Exception {
        TrainAndSaveModel.trainAndSaveModel();
    }
    @Test
    //说明泛化能力不行
    public void testLoadModelAndTestSql() throws Exception {
        Instances trainData = ConverterUtils.DataSource.read("TestData.arff");
        trainData.setClassIndex(1);
        double sum = 0 ;
        for(int i = 0 ; i < trainData.numInstances() ; i++) {
            double pre  = TestSql.testSql(trainData.instance(i).stringValue(0));
            if(pre == trainData.instance(i).classValue())
                sum++ ;
        }
        System.out.println("解雇送"+ sum);
    }
    @Test
    public void testLoadModelAndTestSqlA() throws Exception {
        TestSql.testSql("SelECT * FROM user WHERE id = 1");
    }
}
