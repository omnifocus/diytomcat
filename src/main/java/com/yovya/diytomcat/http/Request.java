package com.yovya.diytomcat.http;

import cn.hutool.core.util.StrUtil;
import com.yovya.diytomcat.Minibrowser;
import com.yovya.diytomcat.catalina.Context;
import com.yovya.diytomcat.catalina.Engine;

import java.io.IOException;
import java.net.Socket;

public class Request {
    private String uri;
    private String requestString;
    private Socket socket;
    private Context context;
//    private Host host;
    private Engine engine;
    public Request(Engine engine, Socket socket) throws IOException {
        this.engine = engine;
        this.socket = socket;
        parseRequestString();
        parseUri();
        configureContext();
    }

    private void configureContext() {
        String name = StrUtil.subBetween(uri,"/","/");
        if (name == null) {
            name = "";
        }
        String path = "/" + name;
//        context = host.getContextMap().get(path);
        context = engine.getDefaultHost().getContext(path);
        if (!path.equals("/")) {
            uri = StrUtil.removePrefix(uri,path);
        }




// before ===================================
//        this.contextMap = new HashMap<>();
//        Context context ;
//        if (this.uri.equals("/")) {
//            // /默认取ROOT下的index.html
//            context = new Context("/", Util.ROOT + "/index.html" );
//        } else {
//            //如/A
//            if (StrUtil.subBetween(this.uri,"/","/") == null ) {
//                //是文件 如/a.html
//                if (this.uri.contains(".")) {
//                    context = new Context(this.uri,Util.ROOT  + this.uri) ;
//                } else
//                    //是目录 如
//                context = new Context(this.uri,Util.WEB_APP_FOLDER  + this.uri + "/index.html") ;
//            } else
//                //如 /A/xx.thml
//                context = new Context(this.uri,Util.WEB_APP_FOLDER  + this.uri);
//        }
//        this.contextMap.put(this.uri,context);
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
