package usoppMVC.route;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import usoppMVC.Request;
import usoppMVC.Utils;
import usoppMVC.models.Todo;
import usoppMVC.models.User;
import usoppMVC.service.TodoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class RouteAjaxTodo {
    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/ajax/todo/index", RouteAjaxTodo::indexView);
        map.put("/ajax/todo/add", RouteAjaxTodo::add);
        map.put("/ajax/todo/all", RouteAjaxTodo::all);

        return map;
    }
    public static byte[] all(Request request) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/json");
        ArrayList<Todo> arrayList = TodoService.load();
        String body = JSON.toJSONString(arrayList);
        String response = Route.responseWithHeader(200, headers, body);
        return response.getBytes();
    }

    public static byte[] indexView(Request request) {
        User currentUser = Route.currentUser(request);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        String body = Utils.html("ajax_todo_index.html");
        String response = Route.responseWithHeader(200, headers, body);
        return response.getBytes();
    }

    public static byte[] add(Request request) {

        // 从 json 字符串里面取数据
        String jsonString = request.body;
        JSONObject jsonForm = JSON.parseObject(jsonString);
        String content = jsonForm.getString("content");
        Utils.log("json content (%s)", content);

        // 添加数据
        User currentUser = Route.currentUser(request);
        HashMap<String, String> form = new HashMap<>();
        form.put("content", content);
        Todo todo = TodoService.add(form, currentUser);


        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/json");
        String body = JSON.toJSONString(todo);
        String response = Route.responseWithHeader(200, headers, body);
        return response.getBytes();
    }

}
