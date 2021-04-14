package com.yovya.diytomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yovya.diytomcat.http.Request;
import com.yovya.diytomcat.http.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Bootstrap {
    public static void main(String[] args) {
        System.out.println("server启动了");
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
                   if (!FileUtil.exist(util.ROOT,path)) {
                       http_body = "404!";
                   } else {
                       http_body = FileUtil.readUtf8String(new File(util.ROOT,path));
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
            e.printStackTrace();
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


}
