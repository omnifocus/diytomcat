package com.yovya.diytomcat;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Bootstrap {
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(8888)) {
           while (true) {
               Socket s = ss.accept();
               InputStream is = s.getInputStream();
               byte[] buffer = new byte[1024];
               is.read(buffer);
               System.out.println("服务端收到数据：" + new String(buffer));
               final OutputStream outputStream = s.getOutputStream();
               String http_head = "HTTP/1.1 200 OK\r\n Content-Type:text/html\r\n\r\n";
               String http_body = "This is a msg from server...";
               outputStream.write((http_head + http_body).getBytes());
               //关闭流
               outputStream.flush();
               outputStream.close();
               is.close();
               s.close();
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
