package guaMVC.service;
import guaMVC.Utils;
import guaMVC.models.*;
import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;

// Service 这一层, 负责具体的读写数据逻辑, 从哪读, 存哪里
public class TodoService {
    public static void add(HashMap<String, String> form, User user) {
        String content = form.get("content");

        ArrayList<Todo> todos = load();

        Todo m = new Todo();
        m.id = todos.size() + 1;
        m.content = content;
        m.completed = false;
        m.userId = user.id;

        todos.add(m);
        TodoService.save(todos);
    }

    public static void delete(Integer id) {
        ArrayList<Todo> todos = load();
        for (int i = 0; i < todos.size(); i++) {
            Todo e = todos.get(i);
            if (e.id.equals(id)) {
                todos.remove(e);
            }
        }
        save(todos);
    }

    public static void updateContent(Integer id, String content) {
        ArrayList<Todo> todos = load();
        for (int i = 0; i < todos.size(); i++) {
            Todo e = todos.get(i);
            if (e.id.equals(id)) {
                e.content = content;
            }
        }
        save(todos);
    }

    public static Todo findById(Integer id) {
        ArrayList<Todo> todos = load();
        for (int i = 0; i < todos.size(); i++) {
            Todo e = todos.get(i);
            if (e.id.equals(id)) {
                return e;
            }
        }
        return null;
    }

     static void save(ArrayList<Todo> list) {
         ModelFactory.save(Todo.class.getSimpleName(), list, new Serialiable<Todo>() {
             @Override
             public String serialize(Todo model) {
                 StringBuilder sb = new StringBuilder();
                 Integer id = model.id;
                 sb.append(id);
                 sb.append("\n");

                 String content = model.content;
                 sb.append(content);
                 sb.append("\n");

                 Boolean completed = model.completed;
                 sb.append(completed);
                 sb.append("\n");

                 Integer userId = model.userId;
                 sb.append(userId);
                 sb.append("\n");
                 return sb.toString();
                 }
         });
    }

    static ArrayList<Todo> load() {
        ArrayList<Todo> ms = ModelFactory.load(Todo.class.getSimpleName(), 4, modelData -> {

            Integer id = Integer.valueOf(modelData.get(0));
            String todoContent = modelData.get(1);
            Boolean completed = Boolean.valueOf(modelData.get(2));
            Integer userId = Integer.valueOf(modelData.get(3));

            Todo m = new Todo();
            m.id = id;
            m.content = todoContent;
            m.completed = completed;
            m.userId = userId;
            return m;
        });
        return ms;
    }


    public static String todoListShowString () {
        ArrayList<Todo> all = load();
        // gua: 21111

        StringBuilder sb = new StringBuilder();

        for (Todo m: all) {
            // <br> 在 html 里面代表换行符
            String s = String.format("%s: %s <br>", m.id, m.content);
            sb.append(s);
        }
        return sb.toString();
    }

    public static ArrayList<Todo> todoListByUserId(Integer userId) {
        ArrayList<Todo> all = load();

        ArrayList<Todo> r = new ArrayList<>();
        for (Todo t: all) {
            if (t.userId.equals(userId)) {
                r.add(t);
            }
        }

        return r;
    }

    public static String todoListHtml (Integer userId) {
        ArrayList<Todo> all = todoListByUserId(userId);
        // gua: 21111

        StringBuilder sb = new StringBuilder();

        for (Todo m: all) {
            // <br> 在 html 里面代表换行符
            String todoHtml = String.format("<h3>\n" +
                    "    %s. %s \n" +
                    "    <a href=\"/todo/delete?id=%s\">删除</a>\n" +
                    "    <a href=\"/todo/edit?id=%s\">编辑</a>\n" +
                    "    <a href=\"/todo/complete?id=%s\">完成</a>\n" +
                    "</h3>",
                    m.id,
                    m.content,
                    m.id,
                    m.id,
                    m.id);
            sb.append(todoHtml);
        }
        return sb.toString();
    }
}
