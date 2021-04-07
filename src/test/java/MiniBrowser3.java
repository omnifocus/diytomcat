import org.apache.jasper.tagplugins.jstl.core.Url;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MiniBrowser3 {
    public static void main(String[] args) {
        String url = "http://static.how2j.cn/diytomcat.html";
        String contentString= getContentString(url);
        System.out.println(contentString);
        System.out.println("-------------------------");
        String httpString= getHttpString(url);
        System.out.println(httpString);
    }

    private static String getHttpString(String url) {
        byte[] bs = getHttpBytes(url);
        return new String(bs);
    }

    private static byte[] getHttpBytes(String _url) {
        try {
            URL url = new URL(_url);
            final String host = url.getHost();
            int port = url.getPort();
            if (port == -1) {
                port = 80;
            }
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host,port));
            OutputStream os = socket.getOutputStream();
            String path = url.getPath();
            if (path.length() == 0) {
                path = "/";
            }
            String firstLine = "GET " + path + " HTTP/1.1\r\n";
            String headerString = "";
            Map<String,String> map = new HashMap<>();
            map.put("host",host + ":" + port);
            for (String key : map.keySet()) {
                headerString += key + ":" + map.get(key) + "\r\n";
            }
            String contentString = firstLine + headerString + "\n";

            os.write(contentString.getBytes());
            os.flush();

            final InputStream is = socket.getInputStream();
            byte[] bs = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            while (true) {
                len = is.read(bs);
                if (len == -1)
                    break;
                baos.write(bs,0,len);
                if (len != bs.length)
                    break;
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getContentString(String url) {
        byte[] bs = getContentByte(url);
        return new String(bs);
    }

    private static byte[] getContentByte(String url) {
        byte[] httpbs = getHttpBytes(url);
        String doubleReturnString = "\r\n\r\n";
        byte[] doubeReturnBytes = doubleReturnString.getBytes();
        int len = httpbs.length - doubeReturnBytes.length;
        byte[] tmp;
        int pos = -1;
        for (int i=0;i<len;i++) {
            tmp = Arrays.copyOfRange(httpbs,i,i+doubeReturnBytes.length);
            if (Arrays.equals(tmp,doubeReturnBytes)) {
                pos = i;
                break;
            }
        }
        if (pos == -1) {
            return null;
        }
        return Arrays.copyOfRange(httpbs,pos + doubeReturnBytes.length,httpbs.length);
    }
}
