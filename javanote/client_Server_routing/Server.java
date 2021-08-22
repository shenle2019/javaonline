import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


class Request {
    String rawData;
    String path;
    String method;
    String body;
    HashMap<String, String> query;
    HashMap<String, String> form;

    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    Request(String rawRequest) {
        this.rawData = rawRequest;
        String[] parts = rawRequest.split("\r\n\r\n", 2);
        this.body = parts[1];
        String headers = parts[0];

        // 解析 http 头
        String[] lines = headers.split("\r\n");
        String requestLine = lines[0];
        String[] requestLineData = requestLine.split(" ");
        this.method = requestLineData[0];

        // 解析 path
        this.parsePath(requestLineData[1]);

        // 解析 body
        this.parseForm(body);
    }

    public void parsePath(String path) {
        // /message?author=xxx&message=111
        Integer index = path.indexOf("?");

        if (index == -1) {
            this.path = path;
            this.query = null;
        } else {
            this.path = path.substring(0, index);
            String queryString = path.substring(index + 1);
//            log("queryString <%s>", queryString);
            String[] args = queryString.split("&");

            // for (int i = 0; i < args.length; i++) {
            //     String e = args[i];
            // }

            this.query = new HashMap<>();
            for (String e: args) {
//                log("args e (%s)", e);
                // author=
                String[] kv = e.split("=", 2);
//                log("kv length %s", kv.length);
                String k = kv[0];
                String v = kv[1];
                log("k (%s), v(%s)", k, v);
                this.query.put(k, v);
            }
        }
    }

    public void parseForm(String body) {
        // author=ddd&message=ddd
        // 如果 body 部分为空, 那么 this.form = null;
        // form 指的就是 body 里面的表单数据
        Integer body_length = body.length();

        if (body_length == 0){
            this.form = null;
//            log("************ (%s)",this.form);
        }else{
            String[] args = body.split("&");
            this.form = new HashMap<>();
            for (String e: args) {
//                log("args e (%s)", e);
                // author=
                String[] kv = e.split("=", 2);
//                log("kv length %s", kv.length);
                String k = kv[0];
                String v = kv[1];
//                log("k (%s), v(%s)", k, v);
                this.form.put(k, v);
            }

        }
    }
}

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
        log("服务器启动, 访问 http://localhost:%s", port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // accept 方法会一直停留在这里等待连接
                try (Socket socket = serverSocket.accept()) {
                    // 客户端连接上来了
                    log("client 连接成功");
                    // 读取客户端请求数据
                    String requestString = socketReadAll(socket);
                    byte[] response;
                    if (requestString.length() > 0) {
                        // 输出响应的数据
                        log("请求:\n%s", requestString);
                        // 解析 request 得到 path
                        Request r = new Request(requestString);

                        // 根据 path 来判断要返回什么数据
                        response = responseForPath(r.path);
                    } else {
                        response = new byte[1];
                        log("接受到了一个空请求");
                    }
                    socketSendAll(socket, response);
                }
            }
        } catch (IOException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
    }

    private static String html(String path) {
        byte[] body = new byte[1];
        //body = new byte[1000];
        body = new byte[40000];
        try (FileInputStream is = new FileInputStream(path)) {
            byte[] tmp = is.readAllBytes();
            body = tmp;
        } catch (IOException e) {
            e.printStackTrace();
        }

        String r = new String(body, StandardCharsets.UTF_8);
        return r;
    }

    private static byte[] routeMessage() {
        String header = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n";
        String body = html("html_basic.html");
        String resposne = header + body;
        return resposne.getBytes();
    }

    private static byte[] routeIndex() {
        String body = "<html><body><a href='/login'>LOGIN</a> <a href='/'>HOME</a>HOME PAGE <img src='/doge.gif'><img src='/doge2.gif'></body></html>";
        String header = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n";
        String response = header + body;
        return response.getBytes();
    }

    private static byte[] routeLogin() {
        String body = "<html><body><a href='/login'>LOGIN</a> <a href='/'>HOME</a><h1>please login</h1></body></html>";
        String response = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes();
    }

    private static byte[] routeImage(String filepath) {
        // body
        String header = "HTTP/1.1 200 very OK\r\nContent-Type: image/gif;\r\n\r\n";

        byte[] body = null;
        try (FileInputStream is = new FileInputStream(filepath)) {
            body = is.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 拼接 header 和图片数据, 新建一个更大的 bytes 数组, 把 header 数组 和 body 数据里面的数据, 往新数组里面复制
        byte[] part1 = header.getBytes();
        byte[] response = new byte[part1.length + body.length];
        System.arraycopy(part1, 0, response, 0, part1.length);
        System.arraycopy(body, 0, response, part1.length, body.length);

        // 也可以用 ByteArrayOutputStream
        // ByteArrayOutputStream writer = new ByteArrayOutputStream();
        // try {
        //     writer.write(header.getBytes());
        //     writer.write(body);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        // response = writer.toByteArray();
        return response;
    }

    private static byte[] route404() {
        String body = "<html><body><h1>404</h1><br><img src='/doge2.gif'></body></html>";
        String response = "HTTP/1.1 404 NOT OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes();
    }

    private static byte[] responseForPath(String path) {
        log("reponseForPath <%s>", path);
        byte[] response;
        if (path.equals("/")) {
            response = routeIndex();
        } else if (path.equals("/login")) {
            response = routeLogin();
        } else if (path.equals("/message")) {
            response = routeMessage();
        } else if (path.equals("/doge.gif")) {
            response = routeImage("doge.gif");
        } else if (path.equals("/doge2.gif")) {
            response = routeImage("doge2.gif");
        } else {
            response = route404();
        }

        return response;
    }


    private static String socketReadAll(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(input);
        int bufferSize = 100;
        // 初始化指定长度的数组
        char[] data = new char[bufferSize];
        // 通常我们会用 StringBuilder 来处理字符串拼接
        // 它比 String 相加运行效率快很多
        StringBuilder sb = new StringBuilder();
        while (true) {
            // 读取数据到 data 数组，从 0 读到 data.length
            // size 是读取到的字节数
            int size = reader.read(data, 0, data.length);

            // 判断是否读到数据
            if (size > 0) {
                sb.append(data, 0, size);
            }
            // 把字符数组的数据追加到 sb 中
            log("size and data: " + size + " || " + data.length);
            // 读到的 size 比 bufferSize 小，说明已经读完了
            if (size < bufferSize) {
                break;
            }
        }
        return sb.toString();
    }

    private static void socketSendAll(Socket socket, byte[] r) throws IOException {
        OutputStream output = socket.getOutputStream();
        output.write(r);
    }
}
