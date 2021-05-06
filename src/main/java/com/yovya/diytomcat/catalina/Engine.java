package com.yovya.diytomcat.catalina;

import com.yovya.diytomcat.XmlUtil;

import java.util.List;

public class Engine {
    private Host defaultHost;
    private List<Host> hosts;


    public Engine() {
        //一初始化 就 给两个属性赋值
        this.defaultHost = getDefaultHost();
        this.hosts = XmlUtil.getAllHosts(this);
    }

    public Host getDefaultHost() {
        List<Host> hosts = XmlUtil.getAllHosts(this);
        for (Host h: hosts) {
            if ("localhost".equals(h.getName())) {
                return h;
            }
        }
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
