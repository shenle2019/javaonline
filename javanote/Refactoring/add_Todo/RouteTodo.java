package usoppMVC.route;


import usoppMVC.Request;
import usoppMVC.Utils;
import usoppMVC.models.Todo;
import usoppMVC.models.User;
import usoppMVC.service.TodoService;
import usoppMVC.service.UserService;

import java.util.HashMap;

public class RouteTodo {
    public static byte[] indexView(Request request) {
        String username = request.cookies.get("username");
        User currentUser = UserService.findByName(username);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        String body = Utils.html("todo_index.html");
        body = body.replace("{todos}", TodoService.todoListHtml(currentUser.id));
        String response = Route.responseWithHeader(200, headers, body);
        return response.getBytes();
    }

    public static byte[] add(Request request) {
        String username = request.cookies.get("username");
        User currentUser = UserService.findByName(username);
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
