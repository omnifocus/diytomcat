package com.yovya.diytomcat.http;

import cn.hutool.core.util.StrUtil;
import com.yovya.diytomcat.Minibrowser;

import java.io.IOException;
import java.io.InputStream;

public class Request {
    private String uri;
    private String requestString;

    private void parseRequestString(InputStream is) throws IOException {
        requestString = new String(Minibrowser.readBytes(is));
    }

    private void parseUri() throws IOException{
        String tmp = StrUtil.subBetween(requestString," ", " ");
        if (!tmp.contains("?")) {
            uri = tmp;
            return;
        }
        uri = StrUtil.subBefore(tmp,'?',true);
    }

    public void parse(InputStream is) throws IOException{
        parseRequestString(is);
        parseUri();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequestString() {
        return requestString;
    }

    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }
}
