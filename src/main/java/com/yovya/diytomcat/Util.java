package com.yovya.diytomcat;

import cn.hutool.log.LogFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Util {
    public static final String response_head_202 = "HTTP/1.1 200 OK\r\n Content-Type:{}\r\n\r\n";
    public static final String WEB_APP_FOLDER = System.getProperty("user.dir") + "/webapps";
    public static final String ROOT = WEB_APP_FOLDER + "/ROOT/";
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(20,30,60, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(10));
    public static void run(Runnable task) {
        executor.execute(task);
    }

    public static void main(String[] args) {
        //获取到的就是项目的根目录
        System.out.println(System.getProperty("user.dir"));
        LogFactory.get().info("{}","xx");
    }
}
