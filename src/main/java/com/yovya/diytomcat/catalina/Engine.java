package com.yovya.diytomcat.catalina;

import com.yovya.diytomcat.XmlUtil;

import java.util.List;

public class Engine {
    private Host defaultHost;
    private List<Host> hosts;
    private Service service;


    public Engine(Service service) {
        this.service = service;
        this.defaultHost = getDefaultHost();
        this.hosts = XmlUtil.getAllHosts(this);
        checkDefaultHost();
    }

    public Host getDefaultHost() {
        List<Host> hosts = XmlUtil.getAllHosts(this);
        for (Host h: hosts) {
            if ("localhost".equals(h.getName())) {
                return h;
            }
        }
        return null;

    }

    private void checkDefaultHost() {
        if (defaultHost == null)
            throw new RuntimeException("找不到默认的主机");
    }

    public void setDefaultHost(Host defaultHost) {
        this.defaultHost = defaultHost;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }
}
