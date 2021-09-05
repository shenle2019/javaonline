package usoppMVC.service;

import usoppMVC.Utils;
import usoppMVC.models.*;

import java.util.ArrayList;
import java.util.HashMap;

// Service 这一层, 负责具体的读写数据逻辑, 从哪读, 存哪里
public class CommentService {
    public static void add(HashMap<String, String> form, User user) {
        String content = form.get("content");
        Integer weiboId = Integer.valueOf(form.get("weibo_id"));

        ArrayList<Comment> all = load();

        Comment m = new Comment();
        m.id = all.size() + 1;
        m.content = content;
        m.userId = user.id;
        m.weiboId = weiboId;

        all.add(m);
        CommentService.save(all);
    }

    public static void delete(Integer id) {
        ArrayList<Comment> all = load();
        for (int i = 0; i < all.size(); i++) {
            Comment e = all.get(i);
            if (e.id.equals(id)) {
                all.remove(e);
            }
        }
        save(all);
    }

    public static void updateContent(Integer id, String content) {
        ArrayList<Comment> all = load();
        for (int i = 0; i < all.size(); i++) {
            Comment e = all.get(i);
            if (e.id.equals(id)) {
                e.content = content;
            }
        }
        save(all);
    }

    public static Comment findById(Integer id) {
        ArrayList<Comment> all = load();
        for (int i = 0; i < all.size(); i++) {
            Comment e = all.get(i);
            if (e.id.equals(id)) {
                return e;
            }
        }

        // 索引的用法
        // Comment todo = indexInteger.get(id);
        return null;
    }

     static void save(ArrayList<Comment> list) {
        ModelFactory.save(Comment.class.getSimpleName(), list, new Serialiable<Comment>() {
            @Override
            public String serialize(Comment model) {
                StringBuilder sb = new StringBuilder();
                Integer id = model.id;
                sb.append(id);
                sb.append("\n");

                String content = model.content;
                sb.append(content);
                sb.append("\n");

                Integer userId = model.userId;
                sb.append(userId);
                sb.append("\n");

                Integer weiboId = model.weiboId;
                sb.append(weiboId);
                sb.append("\n");
                return sb.toString();
            }
        });
    }

    static ArrayList<Comment> load() {
        ArrayList<Comment> ms = ModelFactory.load(Comment.class.getSimpleName(), 4, modelData -> {
            Integer id = Integer.valueOf(modelData.get(0));
            String weiContent = modelData.get(1);
            Integer userId = Integer.valueOf(modelData.get(2));
            Integer weiboId = Integer.valueOf(modelData.get(3));

            Comment m = new Comment();
            m.id = id;
            m.content = weiContent;
            m.userId = userId;
            m.weiboId = weiboId;
            return m;
        });
        return ms;

    }


    public static ArrayList<Comment> commentListByUserId(Integer userId) {
        ArrayList<Comment> all = load();

        ArrayList<Comment> r = new ArrayList<>();
        for (Comment t: all) {
            if (t.userId.equals(userId)) {
                r.add(t);
            }
        }
        return r;
    }

    public static ArrayList<Comment> commentListByWeibo(Integer weiboId) {
        ArrayList<Comment> all = load();

        ArrayList<Comment> r = new ArrayList<>();
        for (Comment t: all) {
            if (t.weiboId.equals(weiboId)) {
                r.add(t);
            }
        }
        return r;
    }

    public static String commentListHtml (Integer weiboId) {
        ArrayList<Comment> all = commentListByWeibo(weiboId);
        // usopp: 21111

        StringBuilder sb = new StringBuilder();

        for (Comment m: all) {
            // <br> 在 html 里面代表换行符
            User user = UserService.findById(m.userId);
            String weiboHtml = String.format("<h3>\n" +
                    "  &nbsp &nbsp &nbsp &nbsp  评论 %s. %s: %s \n" +
                    "    <a href=\"/comment/delete?id=%s\">删除</a>\n" +
                    "    <a href=\"/comment/edit?id=%s\">编辑</a>\n" +
                    "      \n" +
                    "</h3>",
                    m.id,
                    user.username,
                    m.content,
                    m.id,
                    m.id,
                    m.id);
            sb.append(weiboHtml);
        }
        return sb.toString();
    }
}
