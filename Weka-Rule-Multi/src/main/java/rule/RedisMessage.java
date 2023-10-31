package rule;

import java.util.Date;

/**
 * ClassName: MyMeggase
 * Package: rule
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/10/18 - 11:51
 * @Version: v1.0
 */
//相较于DroolsRule多了ruleName和type两个字段
    //但是序列化的时候这个是空的，所以放弃了，应该是继承的问题，直接将DroolsRule进行修改，增加字段
public class RedisMessage extends DroolsRule{
    // "add" , "update" , "delete"
    private String type ;

    private String ruleName ;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public RedisMessage(String type, String ruleName) {
        super();
        this.type = type;
        this.ruleName = ruleName ;

    }
}

