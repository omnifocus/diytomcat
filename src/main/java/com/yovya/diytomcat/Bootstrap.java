package com.yovya.diytomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.yovya.diytomcat.catalina.Context;
import com.yovya.diytomcat.http.Request;
import com.yovya.diytomcat.http.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Bootstrap {
    public static Map<String,Context> contextMap = new HashMap<>();
    public static void main(String[] args) {
        logJVM();
        loadContextPath();
        scannServerXml();
        try (ServerSocket ss = new ServerSocket(8880)) {
           while (true) {
               Socket s = ss.accept();
               Runnable runnable = new Runnable() {
                   @Override
                   public void run() {
                      try {
                          InputStream is = s.getInputStream();
                          Request request = new Request(s);
                          System.out.println("服务端收到的uri:" + request.getUri());
                          System.out.println("服务端收到数据：" + new String(request.getRequestString()));

//               final OutputStream outputStream = s.getOutputStream();
//               String http_head = "HTTP/1.1 200 OK\r\n Content-Type:text/html\r\n\r\n";
//               String http_body = "This is a msg from server...";
//               outputStream.write((http_head + http_body).getBytes());
                          String http_body;
                          String uri = request.getUri();
                          if ("/".equals(uri)) {
                              http_body = "This is a msg from server...";
                          } else {

                              String path = new File(request.getContext().getDocBase(), StrUtil.removePrefix(uri,"/")).getPath();
                              http_body = FileUtil.readUtf8String(path);
                          }
                          Response response = new Response();
                          response.getWriter().println(http_body);
                          handle200(response,s);

                          //关闭流
                          is.close();
                          s.close();
                      } catch (Exception e) {
                          LogFactory.get().error(e);
                      }
                   }
               };

                Util.run(runnable);

           }
        } catch (Exception e) {
            // TODO
            LogFactory.get().error(e);


        }
    }

    private static void scannServerXml() {
        // 文件保存为字符串
        String xml = FileUtil.readUtf8String(Util.SERVER_XML);
        Document doc = Jsoup.parse(xml);
        Elements contexts = doc.select("context");
        for (Element e : contexts) {
            contextMap.put(e.attr("path"),new Context(e.attr("path"),e.attr("docBase")));
        }
    }

    // 混杂着 / 和 不带/的文件名
    private static void loadContextPath() {
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

    private static void handle200(Response response, Socket s) throws IOException {
        byte[] headerbytes = String.format(Util.response_head_202,response.getContentType()).getBytes();
        byte[] bodybytes = response.getBytes();
        //把上面两个字节数组拼出来
        byte[] responsebytes = new byte[headerbytes.length + bodybytes.length];
        System.arraycopy(headerbytes,0,responsebytes,0,headerbytes.length);
        System.arraycopy(bodybytes,0,responsebytes,headerbytes.length,bodybytes.length);
        //写出去
        OutputStream os = s.getOutputStream();
        os.write(responsebytes);
        os.close();
    }

    private static void logJVM() {
        Map<String,String> infos = new LinkedHashMap<>();
        infos.put("Server version", "How2J DiyTomcat/1.0.1");
        infos.put("Server built", "2020-04-08 10:20:22");
        infos.put("Server number", "1.0.1");
        infos.put("OS Name\t", SystemUtil.get("os.name"));
        infos.put("OS Version", SystemUtil.get("os.version"));
        infos.put("Architecture", SystemUtil.get("os.arch"));
        infos.put("Java Home", SystemUtil.get("java.home"));
        infos.put("JVM Version", SystemUtil.get("java.runtime.version"));
        infos.put("JVM Vendor", SystemUtil.get("java.vm.specification.vendor"));
        Set<String> keys = infos.keySet();
        for (String key : keys) {
            LogFactory.get().info(key+":\t\t" + infos.get(key));
        }
    }

}
