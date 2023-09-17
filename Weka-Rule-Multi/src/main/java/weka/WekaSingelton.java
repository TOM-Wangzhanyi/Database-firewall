package weka;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

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
    //获取一个乐观锁
    private static final StampedLock stampedLock = new StampedLock();
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
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            fc = (FilteredClassifier) weka.core.SerializationHelper.read(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    public static void changeDemo(String path) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            demo = ConverterUtils.DataSource.read(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }
    private WekaSingelton(){
        ;
    }
}
