package com.yovya.diytomcat.http;

import cn.hutool.core.util.StrUtil;
import com.yovya.diytomcat.Minibrowser;

import java.io.IOException;
import java.net.Socket;

public class Request {
    private String uri;
    private String requestString;
    private Socket socket;

    public Request(Socket socket) throws IOException {
        this.socket = socket;
        parseRequestString();
        parseUri();
    }

    private void parseRequestString() throws IOException {
        requestString = new String(Minibrowser.readBytes(socket.getInputStream()));
    }

    private void parseUri() throws IOException{
        String tmp = StrUtil.subBetween(requestString," ", " ");
        if (!tmp.contains("?")) {
            uri = tmp;
            return;
        }
        uri = StrUtil.subBefore(tmp,'?',true);
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
