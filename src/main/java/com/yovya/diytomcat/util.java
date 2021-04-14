package com.yovya.diytomcat;

public class util {
    public static final String response_head_202 = "HTTP/1.1 200 OK\r\n Content-Type:{}\r\n\r\n";
    public static final String WEB_APP_FOLDER = System.getProperty("user.dir") + "/webapps";
    public static final String ROOT = WEB_APP_FOLDER + "/ROOT/";
    public static void main(String[] args) {
        //获取到的就是项目的根目录
        System.out.println(System.getProperty("user.dir"));
    }
}
