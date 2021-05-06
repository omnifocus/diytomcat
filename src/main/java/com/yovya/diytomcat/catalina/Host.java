package com.yovya.diytomcat.catalina;

import com.yovya.diytomcat.Util;
import com.yovya.diytomcat.XmlUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Host {
    private String name;
    private Engine engine;
    private Map<String,Context> contextMap = new HashMap<>();

    public Host(String name,Engine engine) {
        this();
        this.name = name;
        this.engine = engine;
    }

    public Host() {
        loadContextPath();
        handleContexts(XmlUtil.scannServerXml());
//        this.name = XmlUtil.getHostName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Context> getContextMap() {
        return contextMap;
    }

    public Context getContext(String path) {
        return contextMap.get(path);
    }

    public void setContextMap(Map<String, Context> contextMap) {
        this.contextMap = contextMap;
    }



    // 混杂着 / 和 不带/的文件名
    private  void loadContextPath() {
        String webRoot = Util.WEB_APP_FOLDER;
        File file = new File(webRoot);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                String name = f.getName();
                String path = "/";
                if (!"ROOT".equals(name)) {
                    path = path + name;
                }
                contextMap.put(path,new Context(path,f.getAbsolutePath()));
            }
        }
    }

    private void handleContexts(Elements contexts) {
        for (Element e : contexts) {
            contextMap.put(e.attr("path"),new Context(e.attr("path"),e.attr("docBase")));
        }
    }
}
