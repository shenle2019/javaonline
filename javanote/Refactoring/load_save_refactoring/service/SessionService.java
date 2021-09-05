package guaMVC.service;

import guaMVC.Utils;
import guaMVC.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

// Service 这一层, 负责具体的读写数据逻辑, 从哪读, 存哪里
public class SessionService {
    public static Session add(Integer userId) {
        Session m = new Session();
        m.sessionId = UUID.randomUUID().toString();
        ;
        m.userId = userId;

        ArrayList<Session> sessionArrayList = load();
        sessionArrayList.add(m);
        SessionService.save(sessionArrayList);
        return m;
    }


    static void save(ArrayList<Session> list) {
        ModelFactory.save(Session.class.getSimpleName(), list, new Serialiable<Session>() {
            @Override
            public String serialize(Session model) {
                StringBuilder sb = new StringBuilder();

                String sessionId = model.sessionId;
                sb.append(sessionId);
                sb.append("\n");

                Integer userId = model.userId;
                sb.append(userId);
                sb.append("\n");

                return sb.toString();
            }
        });
    }

    static ArrayList<Session> load() {
        ArrayList<Session> ms = ModelFactory.load(Session.class.getSimpleName(), 2, modelData -> {

            String sessionId = modelData.get(0);
            Integer userId = Integer.valueOf(modelData.get(1));

            Session m = new Session();
            m.sessionId = sessionId;
            m.userId = userId;
            return m;
        });
        return ms;
    }


    public static Session findBySession(String sessionId) {
        ArrayList<Session> all = load();

        for (Session s : all) {
            if (s.sessionId.equals(sessionId)) {
                return s;
            }
        }

        // 如果找不到 session, 返回一个假 session
        Session session = new Session();
        session.sessionId = "";
        session.userId = -1;
        return session;
    }
}
