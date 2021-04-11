import cn.hutool.core.util.StrUtil;
import com.yovya.diytomcat.Minibrowser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTomcat {
    static int port = 8880;
//    static String ip = "static.how2j.cn";
    static String ip = "localhost";
    static String uri = "/diytomcat.html";
    @BeforeClass
    public static void before() {
//        if (NetUtil.isUsableLocalPort(port)) {
//            System.out.println("请启用该端口"+port+"！");
//            System.exit(-1);
//        } else {
//            System.out.println("开始执行...");
//        }
        System.out.println("开始执行");
    }

    public String getContent() {
        String address = StrUtil.format("http://{}:{}{}",ip,port,uri);
        System.out.println("访问地址：" + address);
        try {
            return Minibrowser.getContentString(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Test
    public void testHelloTomcat() {
        String result = getContent();
        Assert.assertEquals(result,"This is a msg from server...\n");
    }
}
