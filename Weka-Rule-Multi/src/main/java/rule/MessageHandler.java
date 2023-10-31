package rule;

/**
 * ClassName: aaa
 * Package: rule
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/10/18 - 10:59
 * @Version: v1.0
 */
import com.alibaba.fastjson.JSONObject;
import entity.Result;
import redis.clients.jedis.JedisPubSub;

public class MessageHandler extends JedisPubSub {

    /*
     * channel频道接收到新消息后，执行的逻辑
     */
    @Override
    public void onMessage(String channel, String message) {


        // 执行逻辑
        System.out.println(channel + "频道发来消息：" + message);
        DroolsRule droolsMeaagse = JSONObject.parseObject(message, DroolsRule.class);
        System.out.println(droolsMeaagse.getClass());


        //由此判断，这个传过来还原的时候就出问题了
//        System.out.println(redisMessage.getRuleId());
//        System.out.println(redisMessage.getKieBaseName());
//        System.out.println(redisMessage.getKiePackageName());
//        System.out.println(redisMessage.getRuleContent());


        if(droolsMeaagse.getType().equals("add")){
            DroolsRuleServiceImpl.getInstance().addDroolsRule(droolsMeaagse);
        }else if(droolsMeaagse.getType().equals("update")){
            DroolsRuleServiceImpl.getInstance().updateDroolsRule(droolsMeaagse);
        }
        else if(droolsMeaagse.getType().equals("delete")){
            DroolsRuleServiceImpl.changeDelete(droolsMeaagse.getRuleId(), droolsMeaagse.getRuleName());
        }else {
            System.out.println("没有匹配到");
            System.out.println(droolsMeaagse.getType());
        }
        if("close channel".equals(message)){
            this.unsubscribe(channel);
        }
    }

    /*
     * channel频道有新的订阅者时执行的逻辑
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println(channel + "频道新增了"+ subscribedChannels +"个订阅者");
    }

    /*
     * channel频道有订阅者退订时执行的逻辑
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(channel + "频道退订成功");
    }

}
