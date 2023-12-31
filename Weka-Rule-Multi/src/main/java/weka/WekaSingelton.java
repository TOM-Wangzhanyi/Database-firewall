package weka;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

import static weka.core.SerializationHelper.read;

/**
 * ClassName: WekaSingelton
 * Package: weka
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/9/17 - 14:38
 * @Version: v1.0
 */
public class WekaSingelton {
    private static FilteredClassifier fc ;
    private static Instances demo ;


    static {
        try {
            fc = (FilteredClassifier) weka.core.SerializationHelper.read("src/main/resources/trained-Classifier/fc.model");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            demo = ConverterUtils.DataSource.read("src/main/resources/sqlData/demo.arff");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static FilteredClassifier getFcInstance(){
        return fc ;
    }
    public static Instances getDemoInstance(){
        return demo ;
    }

    public static void changeFc(String path) {

        try {
            fc = (FilteredClassifier) weka.core.SerializationHelper.read(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeDemo(String path) {
        try {
            demo = ConverterUtils.DataSource.read(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private WekaSingelton(){
        ;
    }
}
