import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.yovya.diytomcat.Minibrowser;
import org.apache.tools.ant.types.Assertions;
import org.jsoup.internal.StringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTomcat {
    static int port = 80;
    static String ip = "static.how2j.cn";
    static String uri = "/diytomcat.html";
    @BeforeClass
    public static void before() {
        if (!NetUtil.isUsableLocalPort(port)) {
            System.out.println("端口已占用！");
            System.exit(-1);
        } else {
            System.out.println("开始执行...");
        }
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
        Assert.assertEquals(result,"hello diytomcat");
    }
}
