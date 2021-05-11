package com.yovya.diytomcat.catalina;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.yovya.diytomcat.Util;
import com.yovya.diytomcat.XmlUtil;
import com.yovya.diytomcat.http.Request;
import com.yovya.diytomcat.http.Response;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Server {
    private Service service;

    public Server() {
        this.service = new Service(this);
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void start() {
        logJVM();
        init();
    }

    private void init() {
        Service service = new Service(this);
        try (ServerSocket ss = new ServerSocket(8880)) {
            while (true) {
                final Socket s = ss.accept();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request request = new Request(service, s);
                            System.out.println("服务端收到的uri:" + request.getUri());
                            System.out.println("服务端收到数据：" + new String(request.getRequestString()));

//               final OutputStream outputStream = s.getOutputStream();
//               String http_head = "HTTP/1.1 200 OK\r\n Content-Type:text/html\r\n\r\n";
//               String http_body = "This is a msg from server...";
//               outputStream.write((http_head + http_body).getBytes());
                            String http_body;
                            String uri = request.getUri();
                            if ("/500.html".equals(uri)) {
                                throw new RuntimeException("a deliberately thrown error");
                            }
                            String path;
                            Context context = request.getContext();
                            if ("/".equals(uri)) {
                                //如果配置文件没有设置欢迎文件,以空返回
                                path = XmlUtil.getDefaultFile(context);
                            }
                            //如果uri不包括prefix,返回原uri
                            path = StrUtil.removePrefix(uri, "/");

                            File f = new File(context.getDocBase(), path);
                            if (!f.exists()) {
                                handle404(s, uri);
                                return;
                            }


                            String filePath = f.getPath();
                            http_body = FileUtil.readUtf8String(filePath);
                            Response response = new Response();

                            String ext = StrUtil.subAfter(path, ".", true);
                            String contentType = XmlUtil.getMimeType(ext);
                            response.setContentType(contentType);

                            response.getWriter().println(http_body);
                            handle200(response, s);

                        } catch (Exception e) {
                            handle500(s, e);
                            LogFactory.get().error(e);
                        } finally {
                            //关闭流
                            if (s != null) {
                                try {
                                    s.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                };

                Util.run(runnable);

            }
        } catch (Exception e) {
            // TODO
            LogFactory.get().error(e);


        }
    }

    private void handle500(Socket s, Exception e) {
        String resp_head = Util.response_head_500;
        StringBuilder sb = new StringBuilder();
        String msg1 = e.toString();
        String msg2 = e.getMessage();
        sb.append(msg1 + "\n");
        //每条堆栈信息
        Arrays.stream(e.getStackTrace()).forEach((msg) -> {
            sb.append(msg + "\n");
        });
        String resp_body = StrUtil.format(Util.textFormat_500, msg1, msg2, sb.toString());
        try {
            s.getOutputStream().write((resp_head + resp_body).getBytes());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void handle200(Response response, Socket s) throws IOException {
        byte[] headerbytes = StrUtil.format(Util.response_head_202, response.getContentType()).getBytes();
        byte[] bodybytes = response.getBytes();
        //把上面两个字节数组拼出来
        byte[] responsebytes = new byte[headerbytes.length + bodybytes.length];
        System.arraycopy(headerbytes, 0, responsebytes, 0, headerbytes.length);
        System.arraycopy(bodybytes, 0, responsebytes, headerbytes.length, bodybytes.length);
        //写出去
        OutputStream os = s.getOutputStream();
        os.write(responsebytes);
        os.close();
    }

    private void handle404(Socket s, String uri) throws IOException {
        byte[] responsebytes = (Util.response_head_404 + StrUtil.format(Util.textFormat_404, uri, uri)).getBytes();
        //写出去
        OutputStream os = s.getOutputStream();
        os.write(responsebytes);
        os.close();
    }

    private void logJVM() {
        Map<String, String> infos = new LinkedHashMap<>();
        infos.put("Server version", "How2J DiyTomcat/1.0.1");
        infos.put("Server built", "2020-04-08 10:20:22");
        infos.put("Server number", "1.0.1");
        infos.put("OS Name\t", SystemUtil.get("os.name"));
        infos.put("OS Version", SystemUtil.get("os.version"));
        infos.put("Architecture", SystemUtil.get("os.arch"));
        infos.put("Java Home", SystemUtil.get("java.home"));
        infos.put("JVM Version", SystemUtil.get("java.runtime.version"));
        infos.put("JVM Vendor", SystemUtil.get("java.vm.specification.vendor"));
        Set<String> keys = infos.keySet();
        for (String key : keys) {
            LogFactory.get().info(key + ":\t\t" + infos.get(key));
        }
    }
}
