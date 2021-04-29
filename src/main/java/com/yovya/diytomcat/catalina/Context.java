package com.yovya.diytomcat.catalina;

public class Context {
    private String path;
    private String docBase;

    public Context(String path, String docBase) {
        this.path = path;
        this.docBase = docBase;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDocBase() {
        return docBase;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }
}
