package usoppMVC.route;

import usoppMVC.Request;
import usoppMVC.models.User;
import usoppMVC.service.UserService;

import java.util.HashMap;

import static usoppMVC.Utils.html;

public class RouteUser {
    public static byte[] routeLogin(Request request) {
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

        HashMap<String, String> header = new HashMap<>();

        header.put("Content-Type", "text/html");
        header.put("Set-Cookie", String.format("username=%s", loginUserName));
        String body = html("login.html");
        body = body.replace("{loginResult}", result);
        body = body.replace("{loginUser}", loginUserName);

        String response = Route.responseWithHeader(200, header, body);
        return response.getBytes();
    }

    public static byte[] routeRegister(Request request) {
        HashMap<String, String> data = null;
        String result = "";
        if (request.method.equals("POST")) {
            data = request.form;
            if (data != null) {
                UserService.add(data);;
                result = "注册成功";
            }
        }

        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/html");

        String body = html("register.html");
        body = body.replace("{registerResult}", result);

        String response = Route.responseWithHeader(200, header, body);
        return response.getBytes();
    }
}
