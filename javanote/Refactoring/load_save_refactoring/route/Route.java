package usoppMVC.route;

import usoppMVC.Request;
import usoppMVC.Utils;
import usoppMVC.models.Session;
import usoppMVC.models.User;
import usoppMVC.service.MessageService;
import usoppMVC.service.SessionService;
import usoppMVC.service.UserService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

import static usoppMVC.Utils.html;

public class Route {

    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/message", Route::routeMessage);
        map.put("/", Route::routeIndex);
        map.put("/static", Route::routeImage);

        map.putAll(RouteTodo.routeMap());
        map.putAll(RouteUser.routeMap());
        return map;
    }
    public static byte[] routeMessage(Request request) {
        HashMap<String, String> data = null;
        if (request.method.equals("POST")) {
            data = request.form;
        } else if (request.method.equals("GET")) {
            data = request.query;
        } else {
            String m = String.format("Unknown method (%s)", request.method);
            throw new RuntimeException(m);
        }

        Utils.log("request message data (%s)", data);
        if (data != null) {
            // HashMap 的 toString 方法, 可以把内部的 key 和 value 的值
            // 都放进一个字符串当中
            MessageService.add(data);
        }

        String header = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n";
        String body = html("html_basic.html");
        // [{author=222, message=2sdfsdfsdf}]
        body = body.replace("{messages}", MessageService.messageListShowString());
        String resposne = header + body;
        return resposne.getBytes();
    }

    public static User currentUser(Request request) {
        if (request.cookies.containsKey("session_id")) {
            String sessionId = request.cookies.get("session_id");
            Session session = SessionService.findBySession(sessionId);
            User user = UserService.findById(session.userId);
            if (user == null) {
                return UserService.guest();
            } else {
                return user;
            }
        } else {
            return UserService.guest();
        }

    }

    public static byte[] routeIndex(Request request) {
        User user = currentUser(request);
        Utils.log("index username (%s)", user.username);


        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/html");

        String body = html("index.html");
        body = body.replace("{username}", user.username);
        String response = responseWithHeader(200, header, body);
        return response.getBytes();
    }

    public static byte[] routeImage(Request request) {
        String imageName = request.query.get("file");
        String dir = "static";
        String filepath = String.format("%s/%s", dir, imageName);

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

    public static byte[] route404(Request request) {
        String body = "<html><body><h1>404</h1><br><img src='/doge2.gif'></body></html>";
        String response = "HTTP/1.1 404 NOT OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes();
    }

    public static String responseWithHeader(int code, HashMap<String, String> headerMap, String body) {
        String header = String.format("HTTP/1.1 %s\r\r", code);

        for (String key : headerMap.keySet()) {
            String value = headerMap.get(key);
            String item = String.format("%s: %s \r\n", key, value);
            header = header + item;
        }
        String response = String.format("%s\r\n\r\n%s", header, body);
        return response;
    }

    public static byte[] redirect(String url) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Location", url);
        String body = "";
        String response = responseWithHeader(302, headers, body);
        return response.getBytes();
    }
}
