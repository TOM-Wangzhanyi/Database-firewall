package rule;

import entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ClassName: DroolsManager
 * Package: wzy
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/28 - 17:35
 * @Version: v1.0
 */
@Component
@Slf4j
public class DroolsManager {

    // 此类本身就是单例的
    private KieServices kieServices = KieServices.get();
    // kie文件系统，需要缓存，如果每次添加规则都是重新new一个的话，则可能出现问题。即之前加到文件系统中的规则没有了
    private KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    // 可以理解为构建 kmodule.xml
    private KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
    // 需要全局唯一一个，如果每次加个规则都新创建一个，那么就需要销毁之前创建的kieContainer，如果此时有正在使用的KieSession，则可能有问题
    private KieContainer kieContainer;

    private static  DroolsManager droolsManager = new DroolsManager() ;



    //加上读写锁，只允许一个线程写，允许多个线程同时读
    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private final Lock rlock = rwlock.readLock();
    private final Lock wlock = rwlock.writeLock();



    //全部更新，关键existkiebase方法要返回null，不然会导致找包的时候报空指针
    public void destroy() {
        //   kieFileSystem.delete("src/main/resources/");
        //   kieServices = KieServices.get() ;
        kieFileSystem = kieServices.newKieFileSystem() ;
        kieModuleModel = kieServices.newKieModuleModel() ;
        //    kieFileSystem.delete("");
        if (kieContainer != null) {
            kieContainer.dispose();
            kieContainer = null;  // 这样exist方法就会返回null
        }

    }

    /**
     * 判断该kbase是否存在
     */
    public boolean existsKieBase(String kieBaseName) {
        if (null == kieContainer) {
            return false;
        }
        Collection<String> kieBaseNames = kieContainer.getKieBaseNames();
        if (kieBaseNames.contains(kieBaseName)) {
            return true;
        }
        log.info("需要创建KieBase:{}", kieBaseName);
        return false;
    }

    public void deleteDroolsRule(DroolsRule droolsRule, String ruleName) {
        wlock.lock();
        try{
            String kieBaseName = droolsRule.getKieBaseName();
            String packageName = droolsRule.getKiePackageName();
            if (existsKieBase(kieBaseName)) {
                KieBase kieBase = kieContainer.getKieBase(kieBaseName);
                System.out.println(kieBase.getKiePackages());
                System.out.println(kieBase.getKiePackage("first").getRules());
                kieBase.removeRule(packageName, ruleName);
                String file = "src/main/resources/" + droolsRule.getKiePackageName() + "/" + droolsRule.getRuleId() + ".drl";
                kieFileSystem.delete(file);
                buildKieContainer();
                log.info("删除kieBase:[{}]包:[{}]下的规则:[{}]", kieBaseName, packageName, ruleName);
            }
        }finally {
            wlock.unlock();
        }
    }

    /**
     * 添加或更新 drools 规则
     */
    public void addOrUpdateRule(DroolsRule droolsRule) {
        wlock.lock();
        try{ // 获取kbase的名称
            String kieBaseName = droolsRule.getKieBaseName();
            // 判断该kbase是否存在
            boolean existsKieBase = existsKieBase(kieBaseName);
            // 该对象对应kmodule.xml中的kbase标签
            KieBaseModel kieBaseModel = null;
            if (!existsKieBase) {
                // 创建一个kbase
                kieBaseModel = kieModuleModel.newKieBaseModel(kieBaseName);
                // 不是默认的kieBase
                kieBaseModel.setDefault(false);
                // 设置该KieBase需要加载的包路径
                kieBaseModel.addPackage(droolsRule.getKiePackageName());
                // 设置kieSession
                kieBaseModel.newKieSessionModel(kieBaseName + "-session")
                        // 不是默认session
                        .setDefault(false);
            } else {
                // 获取到已经存在的kbase对象
                kieBaseModel = kieModuleModel.getKieBaseModels().get(kieBaseName);
                // 获取到packages
                List<String> packages = kieBaseModel.getPackages();
                if (!packages.contains(droolsRule.getKiePackageName())) {
                    kieBaseModel.addPackage(droolsRule.getKiePackageName());
                    log.info("kieBase:{}添加一个新的包:{}", kieBaseName, droolsRule.getKiePackageName());
                } else {
                    //说明kiebase以及对应的package都已经存在了的，已经不需要哦再去加载了
                    kieBaseModel = null;
                }
            }
            //drl文件中的package名字要和kiebase的package名字相对应，package全局唯一，而且一个kbase对应多个package，package之间不隔离
            //kbase之间隔离
            String file = "src/main/resources/" + droolsRule.getKiePackageName() + "/" + droolsRule.getRuleId() + ".drl";
            log.info("加载虚拟规则文件:{}", file);
            kieFileSystem.write(file, droolsRule.getRuleContent());

            if (kieBaseModel != null) {
                String kmoduleXml = kieModuleModel.toXML();
                log.info("加载kmodule.xml:[\n{}]", kmoduleXml);
                kieFileSystem.writeKModuleXML(kmoduleXml);
            }

            buildKieContainer();}

        finally {
            wlock.unlock(); // 释放写锁
        }
    }

    /**
     * 构建KieContainer
     */
    private void buildKieContainer() {
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        // 通过KieBuilder构建KieModule下所有的KieBase
        kieBuilder.buildAll();
        // 获取构建过程中的结果
        Results results = kieBuilder.getResults();
        // 获取错误信息
        List<Message> messages = results.getMessages(Message.Level.ERROR);
        if (null != messages && !messages.isEmpty()) {
            for (Message message : messages) {
                log.error(message.getText());
            }
            throw new RuntimeException("加载规则出现异常");
        }
        // KieContainer只有第一次时才需要创建，之后就是使用这个
        if (null == kieContainer) {
            kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        } else {
            // 实现动态更新，创建kieContainer开销很大，这样的更新，对于已存在的session是可见的，不需要销毁原来创建好的session
            ((KieContainerImpl) kieContainer).updateToKieModule((InternalKieModule) kieBuilder.getKieModule());
        }
    }

    /**
     * 触发规则，此处简单模拟，会向规则中插入一个String类型的值
     */
    public Result fireRule(String kieBaseName, String param) {
        rlock.lock();
        try{Result result = new Result(100) ;
            // 创建kieSession
            KieSession kieSession = kieContainer.newKieSession(kieBaseName + "-session");
            // StringBuilder resultInfo = new StringBuilder();
            // kieSession.setGlobal("resultInfo", resultInfo);
            kieSession.insert(param);
            kieSession.insert(result) ;
            System.out.println("插入数据了"+param);
            kieSession.fireAllRules();
            System.out.println("触发规则了");
            kieSession.dispose();
            //  return resultInfo.toString();
            return result ;}finally {
            rlock.unlock();
        }
    }

    private DroolsManager(){
    }
    public static DroolsManager getInstance(){
        return droolsManager ;
    }












//    public static void changeAdd(DroolsRule droolsRule){
//        droolsManager.addOrUpdateRule(droolsRule);
//    }
//    public static void changeDelete(DroolsRule droolsRule, String ruleName){
//        droolsManager.deleteDroolsRule(droolsRule, ruleName);
//    }
}

