package com.yovya.diytomcat;

import com.sun.tools.internal.ws.wsdl.document.Input;
import com.sun.tools.internal.ws.wsdl.document.Output;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Minibrowser {
    public static void main(String[] args) throws Exception {
        String url = "http://static.how2j.cn/diytomcat.html";
        String contentString= getContentString(url);
        System.out.println(contentString);
        String httpString= getHttpString(url);
        System.out.println(httpString);
    }

    /**
     * 拼出请求行 请求头
     * 获取服务器返回的结果
     *
     * 请求头 Host必须指定
     * 最关键的是PrintWriter里的autoflush,要不然会卡住 (其实用socket的flush也可，
     *  最最关键是最后一定要输出一个\n
     * )
     *
     * 还有关键点，不能用baos.write(buff,0,len);
     *
     * @return
     * @throws Exception
     */
    public static byte[] getHttpBytes(String _url) throws Exception {
        URL url = new URL(_url);
        int port = url.getPort();
        if (url.getPort() == -1) {
            port = 80;
        }
        InetSocketAddress ias = new InetSocketAddress(url.getHost(),port);
        Socket socket = new Socket();
        socket.connect(ias);
        String path = url.getPath();
        //如果没有路径，就默认/
        if (path.length() == 0) {
            path = "/";
        }
        String firstLine = "GET " + path +" HTTP/1.1 \r\n";
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Content-Type","text/html");
        headerMap.put("User-Agent","shilei's Minibrowser");
        headerMap.put("Host",url.getHost() + ":"+port);
//        headerMap.put("Accept","text/html");
//        headerMap.put("Conenction","close");
        Set<String> keys = headerMap.keySet();
        String headerLine = "";
        for (String key : keys) {
            headerLine += key + ":" + headerMap.get(key) + "\r\n";
        }
        String reqContent = firstLine + headerLine + "\n";
        OutputStream os = socket.getOutputStream();
        os.write(reqContent.getBytes());
//        os.flush();
        PrintWriter pWriter = new PrintWriter(os, true);
//        pWriter.print(reqContent);

        InputStream is = socket.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int buffer_size = 1024;
        byte[] buffer = new byte[buffer_size];
        int len = -1;
//        while((len = is.read(bs)) != -1) {
//            baos.write(bs,0,len);
//        }
      while (true) {
          int length = is.read(buffer);
          if(-1==length)
              break;
          baos.write(buffer, 0, length);
          if(length!=buffer_size)
              break;
      }
        byte[] result = baos.toByteArray();
        baos.flush();
        baos.close();
        is.close();
        os.close();
        socket.close();
        return result;
    }

    public static String getHttpString(String _url) throws Exception {
        return new String(getHttpBytes(_url));
    }

    /**
     * 从第一位挨个找，如(0,0+4)  (1,1+4) ...直找到最后
     * @param _url
     * @return
     * @throws Exception
     */
    public static byte[] getContentBytes(String _url) throws Exception {
        byte[] httpBytes = getHttpBytes(_url);
        if (httpBytes == null)
            return null;
        int pos = -1;
        byte[] targetByte= "\r\n\r\n".getBytes();
        int bytelen = targetByte.length;
        int len = httpBytes.length - bytelen;
        for (int i=0;i<len;i++) {
            byte[] tmp = Arrays.copyOfRange(httpBytes,i,i+bytelen);
            if (Arrays.equals(tmp,targetByte)) {
                pos = i;
                break;
            }
        }
        if (pos == -1) {
            return null;
        }
        pos += bytelen;
        return Arrays.copyOfRange(httpBytes,pos,httpBytes.length);
    }

    public static String getContentString(String _url) throws Exception {
        return new String(getContentBytes(_url));
    }
}
