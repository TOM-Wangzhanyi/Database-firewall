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
    public void testLoadModelAndTestSql() throws Exception {
        TestSql.testSql("SELECT * FROM cream WHERE break = 'around'");
    }
    @Test
    public void testLoadModelAndTestSqlA() throws Exception {
        TestSql.testSql("SelECT * FROM user WHERE id = 1");
    }
}
