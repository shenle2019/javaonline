package usoppMVC.service;
import usoppMVC.Utils;
import usoppMVC.models.Deseriable;
import usoppMVC.models.Message;
import usoppMVC.models.ModelFactory;
import usoppMVC.models.Serialiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// interface Deseriable<T> {
//     T deserialize(List<String> modelData);
// }

// class MessageFactory {
//     static ArrayList<Message> load(String className, int fieldCount, Deseriable<Message> deserializer) {
//         String filename = className + ".txt";
//         String content = Utils.load(filename);
//         String[] lines = content.split("\n");
//         List<String> modelDataArray = Arrays.asList(lines);
//
//         ArrayList<Message> ms = new ArrayList<>();
//
//         for (int i = 0; i < modelDataArray.size(); i = i + fieldCount) {
//             List<String> modelData = modelDataArray.subList(i, i + fieldCount);
//             Message m = deserializer.deserialize(modelData);
//             ms.add(m);
//         }
//         return ms;
//     }
//
//     static <T> void save(String className, ArrayList<T> list, Serialiable<T> serializer) {
//         StringBuilder sb = new StringBuilder();
//
//         for (T m: list) {
//             String modelString = serializer.serialize(m);
//             sb.append(modelString);
//         }
//
//         String filename = className + ".txt";
//         Utils.save(filename, sb.toString());
//     }
// }


// Service 这一层, 负责具体的读写数据逻辑, 从哪读, 存哪里
public class MessageService {
    public static void add(HashMap<String, String> form) {
        Message m = new Message();
        String author = form.get("author");
        String message = form.get("message");
        m.author = author;
        m.message= message;

        ArrayList<Message> messageList = load();
        messageList.add(m);
        MessageService.save(messageList);
    }

     static void save(ArrayList<Message> list) {
        ModelFactory.save(Message.class.getSimpleName(), list, new Serialiable<Message>() {
            @Override
            public String serialize(Message model) {
                StringBuilder sb = new StringBuilder();
                String author = model.author;
                sb.append(author);
                sb.append("\n");

                String message = model.message;
                sb.append(message);
                sb.append("\n");
                return sb.toString();
            }
        });
    }


    // lambda 表达式的写法
    static ArrayList<Message> load() {
        ArrayList<Message> ms = ModelFactory.load(Message.class.getSimpleName(), 2, modelData -> {
            String author = modelData.get(0);
            String message = modelData.get(1);

            Message m = new Message();
            m.author = author;
            m.message = message;
            return m;
        });
        return ms;
    }


    // 匿名类的写法
    static ArrayList<Message> load1() {
        ArrayList<Message> ms = ModelFactory.load(Message.class.getSimpleName(), 2, new Deseriable<Message>() {
            @Override
            public Message deserialize(List<String> modelData) {
                String author = modelData.get(0);
                String message = modelData.get(1);

                Message m = new Message();
                m.author = author;
                m.message = message;
                return m;
            }
        });
        return ms;
    }

    public static String messageListShowString () {
        ArrayList<Message> all = load();
        // usopp: 21111

        StringBuilder sb = new StringBuilder();

        for (Message m: all) {
            // <br> 在 html 里面代表换行符
            String s = String.format("%s: %s <br>", m.author, m.message);
            sb.append(s);
        }

        return sb.toString();
    }
}
