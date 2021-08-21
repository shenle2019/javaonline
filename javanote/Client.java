import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class Client {
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void main(String[] args) {
        // final 修饰的变量，表示不可被修改
        // 其他语言一般叫 const
        final String host = "douban.com";
        final int port = 80;
        final String path = "/";

        String request = String.format("GET %s HTTP/1.1\r\nHost: %s\r\n\r\n", path, host);
        log("请求:\n%s", request);

        // 这是建立网络连接的方法
        try (Socket socket = new Socket(host, port)) {
            // 发送 HTTP 请求给服务器
            socketSendAll(socket, request);

            // 接受服务器的响应数据
            String response = socketReadAll(socket);
            // 输出响应的数据
            log("响应:\n%s", response);
        } catch (Exception e) {
            log("error:" + e.getMessage());
        }
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
