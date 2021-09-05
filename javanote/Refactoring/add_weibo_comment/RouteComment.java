package usoppMVC.route;


import usoppMVC.Request;
import usoppMVC.Utils;
import usoppMVC.models.Comment;
import usoppMVC.models.User;
import usoppMVC.models.UserRole;
import usoppMVC.models.Weibo;
import usoppMVC.service.CommentService;
import usoppMVC.service.WeiboService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class RouteComment {
    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/comment/add", RouteComment::add);
        map.put("/comment", RouteComment::addView);
        map.put("/comment/delete", RouteComment::delete);
        map.put("/comment/update", RouteComment::update);
        map.put("/comment/edit", RouteComment::editView);

        return map;
    }

    public static byte[] add(Request request) {
        User currentUser = Route.currentUser(request);
        Utils.log("add 函数");
        HashMap<String, String> form = request.form;
        Utils.log("form %s", form);
        CommentService.add(form, currentUser);
        return Route.redirect("/weibo/index");
    }

    public static byte[] addView(Request request) {
        User currentUser = Route.currentUser(request);
        if (currentUser.role == UserRole.guest) {
            return Route.redirect("/login");
        } else {
            HashMap<String, String> query = request.query;
            Integer id = Integer.valueOf(query.get("weibo_id"));
            Weibo weibo = WeiboService.findById(id);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/html");
            String body = Utils.html("comment/comment_add.html");
            body = body.replace("{weibo_id}", weibo.id.toString());
            String response = Route.responseWithHeader(200, headers, body);
            return response.getBytes();
        }
    }

    public static byte[] delete(Request request) {

        HashMap<String, String> query = new HashMap<>();
        if (request.method.equals("POST")) {
            query = request.form;
        } else if (request.method.equals("GET")) {
            query = request.query;
        } else {
            String m = String.format("Unknown method (%s)", request.method);
            throw new RuntimeException(m);
        }

        Integer id = Integer.valueOf(query.get("id"));
        Comment comment = CommentService.findById(id);
        User currentUser = Route.currentUser(request);

        Integer id2 = comment.weiboId;
        Weibo weibo = WeiboService.findById(id2);

        if (currentUser.id  == comment.userId){
            CommentService.delete(id);
        }else if(currentUser.id == weibo.userId){
            CommentService.delete(id);
        }else{
            return Route.redirect("/weibo/index");
        }
        return Route.redirect("/weibo/index");
    }

    public static byte[] editView(Request request) {
        HashMap<String, String> query = new HashMap<>();
        if (request.method.equals("POST")) {
            query = request.form;
        } else if (request.method.equals("GET")) {
            query = request.query;
        } else {
            String m = String.format("Unknown method (%s)", request.method);
            throw new RuntimeException(m);
        }

        Integer id = Integer.valueOf(query.get("id"));
        Comment comment = CommentService.findById(id);

        User currentUser = Route.currentUser(request);
        if (currentUser.id != comment.userId) {
            return Route.redirect("/weibo/index");
        } else {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/html");
            String body = Utils.html("comment/comment_edit.html");
            body = body.replace("{comment_id}", comment.id.toString());
            body = body.replace("{comment_content}", comment.content);
            String response = Route.responseWithHeader(200, headers, body);
            return response.getBytes();
        }

    }

    public static byte[] update(Request request) {
//        Utils.log("update 函数");
        HashMap<String, String> form = request.form;
        Integer id = Integer.valueOf(form.get("id"));
        String content = form.get("content");
        CommentService.updateContent(id, content);
        return Route.redirect("/weibo/index");
    }


}
