package com.yovya.diytomcat.catalina;

import com.yovya.diytomcat.XmlUtil;

public class Service {
    private Engine engine;
    private String name;
    private Server server;

    public Service(Server server) {
        this.server = server;
        this.name = XmlUtil.getServiceName();
        this.engine = new Engine(this);
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
