package com.yovya.diytomcat;

import com.yovya.diytomcat.catalina.Server;

public class Bootstrap {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
