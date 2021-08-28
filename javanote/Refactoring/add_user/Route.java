package usopppMVC;

import usopppMVC.models.MessageService;
import usopppMVC.models.User;
import usopppMVC.models.UserService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Route {
    static ArrayList<User> userList = new ArrayList<>();

    private static String html(String htmlName) {
        String dir = "template";
        String path = String.format("%s/%s", dir, htmlName);
        // String path = dir + "/" + htmlName;
        byte[] body = new byte[1];
        try (FileInputStream is = new FileInputStream(path)) {
            byte[] tmp = is.readAllBytes();
            body = tmp;
        } catch (IOException e) {
            e.printStackTrace();
        }

        String r = new String(body, StandardCharsets.UTF_8);
        return r;
    }

    static byte[] routeMessage(Request request) {
        HashMap<String, String> data = null;
        if (request.method.equals("POST")) {
            data = request.form;
        } else if (request.method.equals("GET")) {
            data = request.query;
        } else {
            String m = String.format("Unknown method (%s)", request.method);
            throw new RuntimeException(m);
        }

//        Utils.log("request message data (%s)", data);
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

    static byte[] routeIndex() {
        String body = html("index.html");
        String header = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n";
        String response = header + body;
        return response.getBytes();
    }

    static byte[] routeLogin(Request request) {
        HashMap<String, String> data = null;
        String result = "";
        String loginUserName = "游客";
        if (request.method.equals("POST")) {
            data = request.form;
            boolean valid = UserService.validLogin(data);
            if (valid) {
                User u = UserService.loginUser(data);
                loginUserName = u.username;
                result = "登录成功";
            } else {
                result = "登录失败";
            }
        }
        Utils.log("findByUsernamePassword555 (%s)", UserService.findByUsernamePassword("1234567", "1234567"));
        String body = html("login.html");
        body = body.replace("{loginResult}", result);
        body = body.replace("{loginUser}", loginUserName);
        String response = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes();
    }

    static byte[] routeRegister(Request request) {
        HashMap<String, String> data = null;
        String result = "";
        if (request.method.equals("POST")) {
            data = request.form;
            if (data != null) {
                UserService.add(data);;
                result = "注册成功";
            }
        }
        String body = html("register.html");
        body = body.replace("{registerResult}", result);
        String response = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes();
    }

    static byte[] routeImage(String imageName) {
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

    static byte[] route404() {
        String body = "<html><body><h1>404</h1><br><img src='/doge2.gif'></body></html>";
        String response = "HTTP/1.1 404 NOT OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes();
    }
}
