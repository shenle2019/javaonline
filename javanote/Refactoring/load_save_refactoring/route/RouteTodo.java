package usoppMVC.route;


import usoppMVC.Request;
import usoppMVC.Utils;
import usoppMVC.models.Todo;
import usoppMVC.models.User;
import usoppMVC.service.TodoService;
import usoppMVC.service.UserService;

import java.util.HashMap;
import java.util.function.Function;

public class RouteTodo {
    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/todo", RouteTodo::indexView);
        map.put("/todo/add", RouteTodo::add);
        map.put("/todo/delete", RouteTodo::delete);
        map.put("/todo/update", RouteTodo::update);
        map.put("/todo/edit", RouteTodo::editView);

        return map;
    }

    public static byte[] indexView(Request request) {
        User currentUser = Route.currentUser(request);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        String body = Utils.html("todo_index.html");
        body = body.replace("{todos}", TodoService.todoListHtml(currentUser.id));
        String response = Route.responseWithHeader(200, headers, body);
        return response.getBytes();
    }

    public static byte[] add(Request request) {
        User currentUser = Route.currentUser(request);
        Utils.log("add 函数");
        HashMap<String, String> form = request.form;
        Utils.log("form %s", form);
        TodoService.add(form, currentUser);
        return Route.redirect("/todo");
    }

    public static byte[] delete(Request request) {
        Utils.log("delete 函数");
        HashMap<String, String> query = request.query;
        Utils.log("query %s", query);
        Integer id = Integer.valueOf(query.get("id"));
        TodoService.delete(id);
        return Route.redirect("/todo");
    }

    public static byte[] editView(Request request) {
        HashMap<String, String> query = request.query;
        Integer id = Integer.valueOf(query.get("id"));
        Todo todo = TodoService.findById(id);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        String body = Utils.html("todo_edit.html");
        body = body.replace("{todo_id}", todo.id.toString());
        body = body.replace("{todo_content}", todo.content);
        String response = Route.responseWithHeader(200, headers, body);
        return response.getBytes();
    }

    public static byte[] update(Request request) {
        Utils.log("delete 函数");
        HashMap<String, String> form = request.form;
        Integer id = Integer.valueOf(form.get("id"));
        String content = form.get("content");
        TodoService.updateContent(id, content);
        return Route.redirect("/todo");
    }
}
