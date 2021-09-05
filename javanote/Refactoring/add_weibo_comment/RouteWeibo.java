package usoppMVC.route;


import usoppMVC.Request;
import usoppMVC.Utils;
import usoppMVC.models.Todo;
import usoppMVC.models.User;
import usoppMVC.models.UserRole;
import usoppMVC.models.Weibo;
import usoppMVC.service.TodoService;
import usoppMVC.service.UserService;
import usoppMVC.service.WeiboService;

import java.util.HashMap;
import java.util.function.Function;

public class RouteWeibo {
    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/weibo/index", RouteWeibo::indexView);
        map.put("/weibo/add", RouteWeibo::add);
        map.put("/weibo/delete", RouteWeibo::delete);
        map.put("/weibo/update", RouteWeibo::update);
        map.put("/weibo/edit", RouteWeibo::editView);

        return map;
    }

    public static byte[] indexView(Request request) {
//        Utils.log("in weibo indexView");

        HashMap<String, String> query = new HashMap<>();
        if (request.method.equals("POST")) {
            query = request.form;
        } else if (request.method.equals("GET")) {
            query = request.query;
        } else {
            String m = String.format("Unknown method (%s)", request.method);
            throw new RuntimeException(m);
        }

        if(query == null) {
            User currentUser = Route.currentUser(request);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/html");
            String body = Utils.html("weibo/weibo_index.html");
            body = body.replace("{weibos}", WeiboService.weiboListHtml());
            String response = Route.responseWithHeader(200, headers, body);
            return response.getBytes();
        }else{
            Integer id = Integer.valueOf(query.get("user_id"));
            User userfind = UserService.findById(id);
            Integer id2 = userfind.id;
            User currentUser = Route.currentUser(request);
            if (id2 != -1 && id2 != null) {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "text/html");
                String body = Utils.html("weibo/weibo_index.html");
                body = body.replace("{weibos}", WeiboService.weiboListHtml_byuserid(id2));
                String response = Route.responseWithHeader(200, headers, body);
                return response.getBytes();
            }else{
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "text/html");
                String body = Utils.html("weibo/weibo_index.html");
                body = body.replace("{weibos}", WeiboService.weiboListHtml());
                String response = Route.responseWithHeader(200, headers, body);
                return response.getBytes();
            }
        }
        }

    public static byte[] add(Request request) {
        User currentUser = Route.currentUser(request);
        if (currentUser.role == UserRole.guest) {
            return Route.redirect("/login");
        } else {
//            Utils.log("add 函数");
            HashMap<String, String> form = request.form;
//            Utils.log("form %s", form);
            WeiboService.add(form, currentUser);
            return Route.redirect("/weibo/index");
        }
    }

    public static byte[] delete(Request request) {
//        Utils.log("delete 函数");
        HashMap<String, String> query = request.query;
//        Utils.log("query %s", query);
        Integer id = Integer.valueOf(query.get("id"));
        WeiboService.delete(id);
        return Route.redirect("/weibo/index");
    }

    public static byte[] editView(Request request) {
        HashMap<String, String> query = request.query;
        Integer id = Integer.valueOf(query.get("id"));
        Weibo weibo = WeiboService.findById(id);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        String body = Utils.html("weibo/weibo_edit.html");
        body = body.replace("{weibo_id}", weibo.id.toString());
        body = body.replace("{weibo_content}", weibo.content);
        String response = Route.responseWithHeader(200, headers, body);
        return response.getBytes();
    }

    public static byte[] update(Request request) {
        HashMap<String, String> form = request.form;
        Integer id = Integer.valueOf(form.get("id"));
        String content = form.get("content");
        WeiboService.updateContent(id, content);
        return Route.redirect("/weibo/index");
    }
}
