package com.yovya.diytomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.yovya.diytomcat.http.Request;
import com.yovya.diytomcat.http.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Bootstrap {
    public static void main(String[] args) {
        logJVM();
        try (ServerSocket ss = new ServerSocket(8880)) {
           while (true) {
               Socket s = ss.accept();
               InputStream is = s.getInputStream();
               Request request = new Request(s);
               System.out.println("服务端收到的uri:" + request.getUri());
               System.out.println("服务端收到数据：" + new String(request.getRequestString()));

//               final OutputStream outputStream = s.getOutputStream();
//               String http_head = "HTTP/1.1 200 OK\r\n Content-Type:text/html\r\n\r\n";
//               String http_body = "This is a msg from server...";
//               outputStream.write((http_head + http_body).getBytes());

               String http_body = "";
               String uri = request.getUri();
               //如果uri是/，返回自定义的字符
               //如果不是/,去ROOT目录下查找去掉/后的文件,找到就读出来，找不到就提示404
               if ("/".equals(uri))
                    http_body = "This is a msg from server...";
               else {
                   String path = StrUtil.removePrefix(uri,"/");
                   File f = FileUtil.file(util.ROOT,path);
                   if (!f.exists()) {
                       http_body = "404!";
                   } else {
                       if (f.getName().equals("timeConsume.html")) {
                        Thread.sleep(1000);
                       }
                       http_body = FileUtil.readUtf8String(f);
                   }
               }

               Response response = new Response();
               response.getWriter().println(http_body);
               handle200(response,s);

               //关闭流
               is.close();
               s.close();
           }
        } catch (Exception e) {
            // TODO
            LogFactory.get().error(e);


        }
    }

    private static void handle200(Response response, Socket s) throws IOException {
        byte[] headerbytes = String.format(util.response_head_202,response.getContentType()).getBytes();
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
