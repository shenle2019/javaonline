package usoppMVC.service;

import usoppMVC.Utils;
import usoppMVC.models.*;

import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;
import java.util.HashMap;

// Service 这一层, 负责具体的读写数据逻辑, 从哪读, 存哪里
public class WeiboService {

    public static void add(HashMap<String, String> form, User user) {
        String content = form.get("content");

        ArrayList<Weibo> weiboList = load();

        Weibo m = new Weibo();
        m.id = weiboList.size() + 1;
        m.content = content;
        m.userId = user.id;

        weiboList.add(m);
        WeiboService.save(weiboList);
    }

    public static void delete(Integer id) {
        ArrayList<Weibo> weiboList = load();
        for (int i = 0; i < weiboList.size(); i++) {
            Weibo e = weiboList.get(i);
            if (e.id.equals(id)) {
                weiboList.remove(e);
            }
        }
        save(weiboList);
    }

    public static void updateContent(Integer id, String content) {
        ArrayList<Weibo> weiboList = load();
        for (int i = 0; i < weiboList.size(); i++) {
            Weibo e = weiboList.get(i);
            if (e.id.equals(id)) {
                e.content = content;
            }
        }
        save(weiboList);
    }

    public static Weibo findById(Integer id) {
        ArrayList<Weibo> weiboList = load();
        for (int i = 0; i < weiboList.size(); i++) {
            Weibo e = weiboList.get(i);
            if (e.id.equals(id)) {
                return e;
            }
        }

        // 索引的用法
        // Weibo todo = indexInteger.get(id);
        return null;
    }

    static void save(ArrayList<Weibo> list) {
        ModelFactory.save(Weibo.class.getSimpleName(), list, new Serialiable<Weibo>() {
            @Override
            public String serialize(Weibo model) {
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
                return sb.toString();
            }
        });
    }

    static ArrayList<Weibo> load() {
        ArrayList<Weibo> ms = ModelFactory.load(Weibo.class.getSimpleName(), 3, modelData -> {
            Integer id = Integer.valueOf(modelData.get(0));
            String weiContent = modelData.get(1);
            Integer userId = Integer.valueOf(modelData.get(2));

            Weibo m = new Weibo();
            m.id = id;
            m.content = weiContent;
            m.userId = userId;
            return m;
        });
        return ms;

    }


    public static ArrayList<Weibo> weiboListByUserId(Integer userId) {
        ArrayList<Weibo> all = load();

        ArrayList<Weibo> r = new ArrayList<>();
        for (Weibo t : all) {
            if (t.userId.equals(userId)) {
                r.add(t);
            }
        }

        return r;
    }

    public static String weiboListHtml() {
        Utils.log("weiboList htlml");
        ArrayList<Weibo> all = load();
        // usopp: 21111

        StringBuilder sb = new StringBuilder();

        for (Weibo m : all) {
            // <br> 在 html 里面代表换行符
            User user = UserService.findById(m.userId);
            String commentHtml = CommentService.commentListHtml(m.id);
            String weiboHtml = String.format("<h3>\n" +
                            "   微博 %s. %s: %s \n" +
                            "    <a href=\"/weibo/delete?id=%s\">删除</a>\n" +
                            "    <a href=\"/weibo/edit?id=%s\">编辑</a>\n" +
                            "    <a href=\"/comment?weibo_id=%s\">评论</a>\n" +
                            "     %s  \n" +
                            "</h3>",
                    m.id,
                    user.username,
                    m.content,
                    m.id,
                    m.id,
                    m.id,
                    commentHtml
            );
            sb.append(weiboHtml);
        }
        return sb.toString();
    }

    public static String weiboListHtml_byuserid(Integer userid) {
//        Utils.log("weiboList htlml");
        ArrayList<Weibo> all = load();
        // usopp: 21111

        StringBuilder sb = new StringBuilder();
        ArrayList<Weibo> new_weibo = new ArrayList<>();

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).userId.equals(userid)) {
                new_weibo.add(all.get(i));
            }
        }

        //Utils.log("__________m___(%s)", new_weibo);
        if (new_weibo.size() > 0) {
            for (Weibo m : new_weibo) {
                // <br> 在 html 里面代表换行符
                Utils.log("__________m___(%s)", m);
                Utils.log("__________userid___(%s)", userid);
                User user = UserService.findById(userid);
                String commentHtml = CommentService.commentListHtml(m.id);
                String weiboHtml = String.format("<h3>\n" +
                                "   微博 %s. %s: %s \n" +
                                "    <a href=\"/weibo/delete?id=%s\">删除</a>\n" +
                                "    <a href=\"/weibo/edit?id=%s\">编辑</a>\n" +
                                "    <a href=\"/comment?weibo_id=%s\">评论</a>\n" +
                                "     %s  \n" +
                                "</h3>",
                        m.id,
                        user.username,
                        m.content,
                        m.id,
                        m.id,
                        m.id,
                        commentHtml
                );
                sb.append(weiboHtml);
            }
        } else {
            for (Weibo m : all) {
                // <br> 在 html 里面代表换行符
                User user = UserService.findById(m.userId);
                String commentHtml = CommentService.commentListHtml(m.id);
                String weiboHtml = String.format("<h3>\n" +
                                "   微博 %s. %s: %s \n" +
                                "    <a href=\"/weibo/delete?id=%s\">删除</a>\n" +
                                "    <a href=\"/weibo/edit?id=%s\">编辑</a>\n" +
                                "    <a href=\"/comment?weibo_id=%s\">评论</a>\n" +
                                "     %s  \n" +
                                "</h3>",
                        m.id,
                        user.username,
                        m.content,
                        m.id,
                        m.id,
                        m.id,
                        commentHtml
                );
                sb.append(weiboHtml);
            }
        }            return sb.toString();
    }
}
