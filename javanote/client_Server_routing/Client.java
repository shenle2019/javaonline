import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


class URL {
    String protocol;
    String host;
    int port;
    String path;

    private static void log(String format, Object... args) {
         System.out.println(String.format(format, args));
    }

    URL(String url) {
        String separator = "://";
        Integer i = url.indexOf(separator);

        // 解析协议
        String protocol = "";
        String u = "";
        if (i.equals(-1)) {
            protocol = "http";
            u = url;
        } else {
            protocol = url.substring(0, i);
            u = url.substring(i + separator.length());
        }
//        log("protocol (%s)", protocol);
//        log("剩下的 url (%s)", u);

        i = u.indexOf("/");
        String host = "";
        String path = "";
        if (i.equals(-1)) {
            path = "/";
            host = u;
        } else {
            path = u.substring(i);
            host = u.substring(0, i);
        }
//        log("path (%s)", path);
//        log("host (%s)", host);

        i = host.indexOf(":");
        int port;
        if (i.equals(-1)) {
            if (protocol.equals("http")) {
                port = 80;
            } else {
                port = 443;
            }
        } else {
            String[] parts = host.split(":", 2);
            host = parts[0];
            port = Integer.parseInt(parts[1]);
        }
//        log("解析完毕 (%s) (%s) (%s) (%s)", protocol, host, port, path);

        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
    }
}

public class Client {
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void main(String[] args) {
//        String url = "douban.com";
        String url = "http://localhost:9000/";
        String response = get(url);
        log("response\n <%s>", response);
    }

    public static String get(String url) {
        URL u = new URL(url);
        // final 修饰的变量，表示不可被修改
        // 其他语言一般叫 const
        final String host = u.host;
        final int port = u.port;
        final String path = u.path;

        String request = String.format("GET %s HTTP/1.1\r\nHost: %s\r\n\r\n", path, host);
        log("请求:\n%s", request);

        String response = "";
        // 这是建立网络连接的方法
        try (Socket socket = new Socket(host, port)) {
            // 发送 HTTP 请求给服务器
            socketSendAll(socket, request);

            // 接受服务器的响应数据
            response = socketReadAll(socket);
            // 输出响应的数据
            // log("响应:\n%s", response);
        } catch (Exception e) {
            log("error:" + e.getMessage());
        }return response;
    }

    private static String socketReadAll(Socket socket) throws IOException {
            InputStream input = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            // 指定读取的数据长度为 1024
            int bufferSize = 1024;
            // 初始化指定长度的数组
            char[] data = new char[bufferSize];
            // read 函数会把读到的数据复制到 data 数组中去
            // size 是实际读到的数据的长度, 字节为单位
            int size = reader.read(data, 0, data.length);

            // 通常我们会用 StringBuilder 来处理字符串拼接
            // 它比 String 相加运行效率快很多
            // "a" + "b";
            StringBuilder response = new StringBuilder();
            // append 方法会把 data 数组的内容转成字符串
            response.append(data, 0, size);
            return response.toString();
    }

    private static void socketSendAll(Socket socket, String request) throws IOException {
        OutputStream output = socket.getOutputStream();
            // bytes 就是二进制, write 发送的都是二进制
        output.write(request.getBytes());
    }
}
