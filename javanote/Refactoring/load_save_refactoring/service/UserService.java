package usoppMVC.service;

import usoppMVC.Utils;
import usoppMVC.models.*;

import java.util.ArrayList;
import java.util.HashMap;

// Service 这一层, 负责具体的读写数据逻辑, 从哪读, 存哪里
public class UserService {
    public static void add(HashMap<String, String> form) {
        User m = new User();
        String username = form.get("username");
        String password = form.get("password");

        ArrayList<User> userList = load();

        m.id = userList.size() + 1;
        m.username = username;
        m.password = password;
        m.role = UserRole.normal;

        userList.add(m);
        UserService.save(userList);
    }

    static void save(ArrayList<User> list) {
        ModelFactory.save(User.class.getSimpleName(), list, new Serialiable<User>() {
            @Override
            public String serialize(User model) {
                StringBuilder sb = new StringBuilder();
                Integer id = model.id;
                sb.append(id);
                sb.append("\n");

                String username = model.username;
                sb.append(username);
                sb.append("\n");

                String password = model.password;
                sb.append(password);
                sb.append("\n");

                UserRole role = model.role;
                sb.append(role);
                sb.append("\n");
                return sb.toString();
            }
        });
    }

    static ArrayList<User> load() {
        ArrayList<User> ms = ModelFactory.load(User.class.getSimpleName(), 4, modelData -> {
            Integer id = Integer.valueOf(modelData.get(0));
            String username = modelData.get(1);
            String password = modelData.get(2);
            UserRole role =  UserRole.valueOf(modelData.get(3));

            User m = new User();
            m.id = id;
            m.username = username;
            m.password = password;
            m.role = role;
            return m;
        });
        return ms;
    }

    public static String userListShowString() {
        ArrayList<User> all = load();
        StringBuilder sb = new StringBuilder();

        for (User m : all) {
            // <br> 在 html 里面代表换行符
            String s = String.format("id: %s,%s: %s <br>", m.id, m.username, m.password);
            sb.append(s);
        }

        return sb.toString();
    }

    public static boolean validLogin(HashMap<String, String> form) {
        Utils.log("login data (%s)", form);
        String username = form.get("username");
        String password = form.get("password");

        ArrayList<User> userList = load();

        for (User user : userList) {
            if (username.equals(user.username) && password.equals(user.password)) {
                return true;
            }
        }
        return false;
    }

    public static User loginUser(HashMap<String, String> form) {
        Utils.log("login data (%s)", form);
        String username = form.get("username");
        String password = form.get("password");

        ArrayList<User> userList = load();

        for (User user : userList) {
            if (username.equals(user.username) && password.equals(user.password)) {
                return user;
            }
        }

        return null;
    }

    public static User findByName(String username) {
        ArrayList<User> all = load();

        for (User u : all) {
            if (u.username.equals(username)) {
                return u;
            }
        }

        return null;
    }

    public static User findById(Integer id) {
        ArrayList<User> all = load();

        for (User u : all) {
            if (u.id.equals(id)) {
                return u;
            }
        }

        return null;
    }

    public static User guest() {
        User u = new User();
        u.id = -1;
        u.username = "游客";
        u.password = "";
        u.role = UserRole.guest;
        return u;
    }
}
