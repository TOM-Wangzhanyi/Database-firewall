package rule;

import entity.Result;
import mapper.FireWallMapper;
import multi.InitThreadPool;
import org.apache.ibatis.session.SqlSession;
import redis.clients.jedis.Jedis;
import rule.DroolsManager;
import rule.DroolsRule;
import rule.DroolsRuleService;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ClassName: DroolsRuleServiceImpl
 * Package: wzy
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/29 - 13:27
 * @Version: v1.0
 */
//用了static代码块来初始化droolsManager，然后后面用单例模式来调用这个droolsManager ；
public class DroolsRuleServiceImpl implements DroolsRuleService {






    private static DroolsRuleServiceImpl droolsRuleServiceimpl ;

    private DroolsManager droolsManager = DroolsManager.getInstance() ;

    static {

        SqlSession sqlSession = com.wzy.mybatis.utils.SqlSessionUtils.getSqlSession();
        FireWallMapper mapper = sqlSession.getMapper(FireWallMapper.class);
        List<DroolsRule> rules = mapper.getAllRule();
        droolsRuleServiceimpl = new DroolsRuleServiceImpl() ;
        for(DroolsRule rule : rules){
            if(rule.getType().equals("add")){
                droolsRuleServiceimpl.addDroolsRule(rule);
            }else if(rule.getType().equals("update")){
                droolsRuleServiceimpl.updateDroolsRule(rule);
            }
            else if(rule.getType().equals("delete")){
                droolsRuleServiceimpl.deleteDroolsRule(rule.getRuleId(),rule.getRuleName());
            }
        }



        Jedis jedis = new Jedis("127.0.0.1", 6379);
        MessageHandler handler = new MessageHandler();

        ExecutorService executor = InitThreadPool.getInstance();
        executor.execute(() -> {
            jedis.subscribe(handler, "channel1");
        });
    }

    /**
     * 模拟数据库
     */
    private Map<Long, DroolsRule> droolsRuleMap = new HashMap<>(16);

    //这里应该改成从数据库中读取信息
    @Override
    public List<DroolsRule> findAll() {
        return new ArrayList<>(droolsRuleMap.values());
    }

    @Override
    public void addDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setCreatedTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    @Override
    public void updateDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setUpdateTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    @Override
    public void deleteDroolsRule(Long ruleId, String ruleName) {
        DroolsRule droolsRule = droolsRuleMap.get(ruleId);
        if (null != droolsRule) {
            droolsRuleMap.remove(ruleId);
            droolsManager.deleteDroolsRule(droolsRule, ruleName);
        }
    }
    public DroolsManager getDroolsManager(){
        return droolsManager ;
    }

    private DroolsRuleServiceImpl(){

    }
    public static DroolsRuleService getInstance(){
        return droolsRuleServiceimpl ;
    }


    public static void changeAdd(DroolsRule droolsRule){
        droolsRuleServiceimpl.addDroolsRule(droolsRule);
    }
    public static void changeUpdate(DroolsRule droolsRule){

    }
    public static void changeDelete(Long ruleId, String ruleName){
        droolsRuleServiceimpl.deleteDroolsRule(ruleId, ruleName);
    }

    public void init(){
        ;
    }
}

