package usopppMVC.models;
import usopppMVC.Utils;

import java.util.ArrayList;
import java.util.HashMap;

// Service 这一层, 负责具体的读写数据逻辑, 从哪读, 存哪里
public class UserService {
    public static void add(HashMap<String, String> form) {
        User m = new User();
        String username = form.get("username");
        String password = form.get("password");
        String note = form.get("note");
        m.username = username;
        m.password= password;
        m.note= note;

        ArrayList<User> messageList = load();
        messageList.add(m);
        UserService.save(messageList);
    }

     static void save(ArrayList<User> list) {
        StringBuilder sb = new StringBuilder();
        for (User m: list) {
            String username = m.username;
            sb.append(username);
            sb.append("\n");

            String password = m.password;
            sb.append(password);
            sb.append("\n");

            String note = m.note;
            sb.append(note);
            sb.append("\n");
        }
        Utils.log("saveFile (%s)", sb.toString());

        String filename = User.class.getSimpleName() + ".txt";
        Utils.save(filename, sb.toString());
    }

    static ArrayList<User> load() {
        String filename = User.class.getSimpleName() + ".txt";
        String content = Utils.load(filename).strip();
        String[] lines = content.split("\n");

        ArrayList<User> ms = new ArrayList<>();

        int fieldCount = 3;
        // 如果 User.txt 里面至少用一个用户的数据, 再去读取, 实例化 User
        if (lines.length >= fieldCount) {
            for (int i = 0; i < lines.length; i = i + fieldCount) {
//                Utils.log("line (%s)", lines[i]);
                String username = lines[i];
                String password = lines[i + 1];
                String note = lines[i + 2];

                User m = new User();
                m.username = username;
                m.password = password;
                m.note = note;
                ms.add(m);
            }
        }
        return ms;
    }

    public static String userListShowString () {
        ArrayList<User> all = load();
        StringBuilder sb = new StringBuilder();

        for (User m: all) {
            // <br> 在 html 里面代表换行符
            String s = String.format("%s: %s <br>", m.username, m.password);
            sb.append(s);
        }

        return sb.toString();
    }

    public static boolean validLogin(HashMap<String, String> form) {
//        Utils.log("login data (%s)", form);
        String username = form.get("username");
        String password = form.get("password");

        ArrayList<User> userList = load();

        for (User user: userList) {
            if (username.equals(user.username) && password.equals(user.password)) {
                return true;
            }
        }
        return false;
    }

    public static User loginUser(HashMap<String, String> form) {
//        Utils.log("login data (%s)", form);
        String username = form.get("username");
        String password = form.get("password");

        ArrayList<User> userList = load();

        for (User user: userList) {
            if (username.equals(user.username) && password.equals(user.password)) {
                return user;
            }
        }

        return null;
    }

    public static User findByUsername(String username) {
//        Utils.log("findByUsername222  (%s)", username);
        User result = new User();
        result = null;
        ArrayList<User> userList = load();
        for (User user: userList) {
            if (username.equals(user.username)) {
                result = user;
            }
        }
        return result;
    }

    public static User findByUsernamePassword(String username, String password) {
        Utils.log("findByUsernamePassword333  (%s), (%s)", username, password);
        User result = new User();
        result = null;
        ArrayList<User> userList = load();
        for (User user: userList) {
            if (username.equals(user.username) && password.equals(user.password)) {
                result = user;
            }
        }
        return result;
    }

}
