package com.yovya.diytomcat;

import cn.hutool.core.io.FileUtil;
import com.yovya.diytomcat.catalina.Engine;
import com.yovya.diytomcat.catalina.Host;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class XmlUtil {
    public static String getHostName() {
        String path = Util.CONF_FOLDER + "/" + "server.xml";
        String xmlContent = FileUtil.readUtf8String(path);
        Document doc = Jsoup.parse(xmlContent);
        Element e = doc.select("host").first();
        return e.attr("name");
    }

    public static Elements scannServerXml() {
        // 文件保存为字符串
        String xml = FileUtil.readUtf8String(Util.SERVER_XML);
        Document doc = Jsoup.parse(xml);
        Elements contexts = doc.select("context");
        return contexts;
    }


    //取到engine 的属性 host名
    public static String getDefaultHostName(Engine engine) {
        String xml = FileUtil.readUtf8String(Util.SERVER_XML);
        Document doc = Jsoup.parse(xml);
        Element e = doc.select("engine").first();
        return e.attr("defaultHost");
    }

    //取到所有的host
    public static List<Host> getAllHosts(Engine engine) {
        List<Host> hosts = new ArrayList<>();
        String xml = FileUtil.readUtf8String(Util.SERVER_XML);
        Document doc = Jsoup.parse(xml);
        Elements es = doc.select("host");
        for (Element e: es) {
            //拿出name和传入的engine
            Host host = new Host(e.attr("name"),engine);
            hosts.add(host);
        }
        return hosts;
    }
}
