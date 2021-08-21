import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;


public class Server {
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        // 监听请求
        // 获取请求数据
        // 发送响应数据
        // 我们的服务器使用 9000 端口
        // 不使用 80 的原因是 1024 以下的端口都要管理员权限才能使用
        int port = 9000;
        log("访问 http://localhost:%s", port);
        // 处于监听状态的 socket
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // accept 方法会一直停留在这里等待连接
                try (Socket socket = serverSocket.accept()) {
                    // 客户端连接上来了
                    log("client 连接成功");
                    // 读取客户端请求数据
                    String request = socketReadAll(socket);
                    // 输出响应的数据
                    log("请求:\n%s", request);

                    if (request.length() > 0) {
                        // 发送响应数据给客户端
                        String body = "<html><body>hello</body></html>";
                        String length = String.format("Content-Length: %s", body.length());
                        String response = "HTTP/1.1 200 very OK\r\n" +
                                "Content-Type: text/html;\r\n" +
                                length + "\r\n" +
                                "\r\n" +
                                body;
                        socketSendAll(socket, response);
                    } else {
                        log("接受到一个空请求");
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
    }
    private static String socketReadAll(Socket socket) throws IOException {
            InputStream input = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            // 指定读取的数据长度为 1024


            // 通常我们会用 StringBuilder 来处理字符串拼接
            // 它比 String 相加运行效率快很多
            // "a" + "b";
            StringBuilder response = new StringBuilder();
            // append 方法会把 data 数组的内容转成字符串
           int bufferSize = 1024;
            // 初始化指定长度的数组
            char[] data = new char[bufferSize];

            while (true) {
                int size = reader.read(data, 0, data.length);

                if (size > 0) {
                    response.append(data, 0, size);
                }

                log("size and data:" + size + " || " + data.length);

                if (size < bufferSize) {
                    break;
                }
            }

            // read 函数会把读到的数据复制到 data 数组中去
            // size 是实际读到的数据的长度, 字节为单位
            return response.toString();
    }

    private static void socketSendAll(Socket socket, String r) throws IOException {
        OutputStream output = socket.getOutputStream();
        output.write(r.getBytes());
    }
}
