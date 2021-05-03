package com.yovya.diytomcat;

import cn.hutool.core.io.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
}
