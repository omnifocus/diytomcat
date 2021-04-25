import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yovya.diytomcat.Minibrowser;
import com.yovya.diytomcat.util;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestTomcat {
    static int port = 8880;
//    static String ip = "static.how2j.cn";
    static String ip = "localhost";
    static String uri = "/timeConsume.html";
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

    @Test
    public void testFileUitl() {
        System.out.println(util.ROOT);
        System.out.println(FileUtil.exist(util.ROOT,"index.html"));
    }

    @Test
    public void testTimeConsume() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
//        TimeInterval ti = new TimeInterval();
        TimeInterval ti = DateUtil.timer();
        for (int i=0;i<3;i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String res = getContent();
                    System.out.println(res);
                }
            });
        }
        //关闭线程池后才有效果
        executor.shutdown();
        executor.awaitTermination(1,TimeUnit.HOURS);
        Assert.assertTrue(ti.intervalMs()>3000);

    }
}
