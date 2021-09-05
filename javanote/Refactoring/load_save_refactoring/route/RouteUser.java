package usoppMVC.route;

import usoppMVC.Request;
import usoppMVC.models.Session;
import usoppMVC.models.User;
import usoppMVC.models.UserRole;
import usoppMVC.service.SessionService;
import usoppMVC.service.UserService;

import java.util.HashMap;
import java.util.function.Function;

import static usoppMVC.Utils.html;

public class RouteUser {
    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/login", RouteUser::routeLogin);
        map.put("/register", RouteUser::routeRegister);
        map.put("/admin", RouteUser::routeAdmin);
        return map;
    }


    public static byte[] routeAdmin(Request request) {
        User currenUser = Route.currentUser(request);

        if (currenUser.role == UserRole.admin) {
            HashMap<String, String> header = new HashMap<>();
            String body = "admin can see";
            String response = Route.responseWithHeader(200, header, body);
            return response.getBytes();
        } else {
            // 1. 返回 404
            HashMap<String, String> header = new HashMap<>();
            String body = "";
            String response = Route.responseWithHeader(404, header, body);
            return response.getBytes();

            // 2. 重定向
            // return Route.redirect("/login");
        }
    }

    public static byte[] routeLogin(Request request) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/html");

        HashMap<String, String> data = null;
        String result = "";
        String loginUserName = "游客";
        if (request.method.equals("POST")) {
            data = request.form;
            boolean valid = UserService.validLogin(data);
            if (valid) {
                User u = UserService.loginUser(data);
                loginUserName = u.username;
                Session session = SessionService.add(u.id);
                header.put("Set-Cookie", String.format("session_id=%s", session.sessionId));
                result = "登录成功";
            } else {
                result = "登录失败";
            }
        }

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
                UserService.add(data);
                ;
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
