package com.yovya.diytomcat.catalina;

import com.yovya.diytomcat.XmlUtil;

import java.util.List;

public class Engine {
    private Host defaultHost;
    private List<Host> hosts;
    private Service service;


    public Engine(Service service) {
        this.service = service;
        this.hosts = XmlUtil.getAllHosts(this);
        //这行放在下面，不用查第二遍
        this.defaultHost = getDefaultHost();
        checkDefaultHost();
    }

    public Host getDefaultHost() {
        List<Host> hosts = this.hosts;
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
