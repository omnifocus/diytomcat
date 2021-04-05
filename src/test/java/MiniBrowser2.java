import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 1.读 server响应的response数据时 要判断两次
 * 2.读 server响应的response数据时  注意截取！
 */
public class MiniBrowser2 {
    public static void main(String[] args) throws Exception {
        String url = "http://static.how2j.cn/diytomcat.html";
        String contentString= getContentString(url);
        System.out.println(contentString);
        System.out.println("-------------------------");
//        String httpString= getHttpString(url);
//        System.out.println(httpString);
    }

    static byte[] getHttpBytes(String url) throws Exception {
        URL _url = new URL(url);
        int port = _url.getPort();
        String host = _url.getHost();
        if (port == -1)
            port = 80;

//       socket连接server
        InetSocketAddress address = new InetSocketAddress(host,port);
        Socket socket = new Socket();
        socket.connect(address);

        String path = _url.getPath();
        if (path.length() == 0) {
            path = "/";
        }
        //发送http请求
        OutputStream os = socket.getOutputStream();
        String firstLine = "GET " + path + " HTTP/1.1 \r\n";
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Host",host + ":" + port);
        headerMap.put("User-Agent","shilei's agent");
        headerMap.put("Content-Type","text/html");
        String headerLines = "";
        Set<String> headers = headerMap.keySet();
        for (String header: headers) {
            headerLines += header + ":" + headerMap.get(header) + "\r\n";
        }

        String reqContent = firstLine + headerLines + "\n";
        os.write(reqContent.getBytes());
        os.flush();

        //获取server发送数据
        InputStream is = socket.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len;
        while (true) {
            len = is.read(buff);
            if (len == -1) {
                break;
            }
            baos.write(buff, 0,len);
            if (len != buff.length) {
                break;
            }
        }
        byte[] bs = baos.toByteArray();
        String receiveContent = new String(bs);
//        System.out.println("服务端发回的数据如下：\n" + receiveContent);

        return bs;
    }

    static String getHttpString(String url) throws Exception {
        return new String(getHttpBytes(url));
    }

    static byte[] getContentBytes(String url) throws Exception {
        byte[] httpBytes = getHttpBytes(url);
        byte[] doubleReturn = "\r\n\r\n".getBytes();
        int end = httpBytes.length - doubleReturn.length;
        byte[] temp;
        int pos = -1;
        for (int i = 0; i < end; i++) {
            temp = Arrays.copyOfRange(httpBytes, i, i + doubleReturn.length);
            if (Arrays.equals(doubleReturn, temp)) {
                pos = i;
                break;
            }
        }
        if (pos == -1) {
            return null;
        }

        return Arrays.copyOfRange(httpBytes, pos + doubleReturn.length, httpBytes.length);
    }

    static String getContentString(String url) throws Exception {
        return new String(getContentBytes(url));
    }
}
