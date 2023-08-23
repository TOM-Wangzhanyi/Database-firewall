import java.util.concurrent.Callable;

/**
 * ClassName: SimpleUse
 * Package: PACKAGE_NAME
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/8/22 - 18:30
 * @Version: v1.0
 */
public class SimpleUse implements Callable<String>{

    private String str ;
    @Override
    public String call() {
        System.out.println(Thread.currentThread().getName() + "   执行callable的call方法" + str);
        return "告诉主线程，我很好";
    }
    public SimpleUse(String str){
        this.str = str ;
    }
}
