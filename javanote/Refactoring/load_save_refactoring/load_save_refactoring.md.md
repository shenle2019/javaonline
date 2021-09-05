# load save refactoring



### MessageService save 和 load 函数，改成接口的写法

1. models模块中添加序列化、反序列化接口，然后实现一个公共的工厂类

   注意这里序列化返回值，反序列化返回值均为泛型。

```java
package usoppMVC.models;

import usoppMVC.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelFactory {
    public static <T> ArrayList<T> load(String className, int fieldCount, Deseriable<T> deserializer) {
	//这里 有个接口，接口里的方法 deserialize，需要实现；即反序列化，取出数据
	//public interface Deseriable<T> {
    //T deserialize(List<String> modelData);
    //}
        String filename = className + ".txt";
        String content = Utils.load(filename);
        String[] lines = content.split("\n");
        List<String> modelDataArray = Arrays.asList(lines);

        ArrayList<T> ms = new ArrayList<>();

        for (int i = 0; i < modelDataArray.size(); i = i + fieldCount) {
            List<String> modelData = modelDataArray.subList(i, i + fieldCount);
            T m = deserializer.deserialize(modelData);
            ms.add(m);
        }
        return ms;
    }

    public static <T> void save(String className, ArrayList<T> list, Serialiable<T> serializer) {
        StringBuilder sb = new StringBuilder();
		//这里 有个接口，接口里的方法 serialize，需要实现；即序列化，存数据
		//public interface Serialiable<T> {
		//	String serialize(T model);
		//}
        for (T m: list) {
            String modelString = serializer.serialize(m);
            sb.append(modelString);
        }

        String filename = className + ".txt";
        Utils.save(filename, sb.toString());
    }
}
```



2. service层中实现对应的方法,这里实现了load函数的两种写法：lambda 表达式的写法、匿名类写法。

```java
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

    //实现serialize方法，返回String类型值。
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
```



3. 参考 MessageService save 和 load 函数
   把 TodoService 的 save 和 load 函数改成接口的写法

   ```
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
   ```

4.  参考 MessageService save 和 load 函数
   把 SessionService 的 save 和 load 函数改成接口的写法

   ```
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
   ```

   

### 利用表驱动法，来改写路由，精简代码

```java

    private static byte[] responseForPath(Request request) {
        String path = request.path;
        Utils.log("reponseForPath <%s>", path);


        // 表驱动法
        HashMap<String, Function<Request, byte[]>> routeMap = Route.routeMap();
        Function<Request, byte[]> getF = routeMap.getOrDefault(request.path, Route::route404);
        byte[] response = getF.apply(request);

        // byte[] response;
        // if (path.equals("/")) {
        //     response = Route.routeIndex(request);
        // } else if (path.equals("/register")) {
        //     response = RouteUser.routeRegister(request);
        // } else if (path.equals("/login")) {
        //     response = RouteUser.routeLogin(request);
        // } else if (path.equals("/message")) {
        //     response = Route.routeMessage(request);
        // } else if (path.equals("/doge.gif")) {
        //     response = Route.routeImage("doge.gif");
        // } else if (path.equals("/doge2.gif")) {
        //     response = Route.routeImage("doge2.gif");
        // } else if (path.equals("/todo")) {
        //     response  = RouteTodo.indexView(request);
        // } else if (path.equals("/todo/add")) {
        //     response  = RouteTodo.add(request);
        // } else if (path.equals("/todo/delete")) {
        //     response  = RouteTodo.delete(request);
        // } else if (path.equals("/todo/edit")) {
        //     response  = RouteTodo.editView(request);
        // } else if (path.equals("/todo/update")) {
        //     response  = RouteTodo.update(request);
        // } else if (path.equals("/admin")) {
        //     response = RouteUser.routeAdmin(request);
        // } else {
        //     response = Route.route404();
        // }

        return response;
    }

    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/message", Route::routeMessage);
        map.put("/", Route::routeIndex);
        map.put("/static", Route::routeImage);

        map.putAll(RouteTodo.routeMap());
        map.putAll(RouteUser.routeMap());
        return map;
    }

   //其中hashmap中有默认方法
    
        @Override
    public V getOrDefault(Object key, V defaultValue) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
    }
```

分别实现route,比如Route中实现routeMessage路由 ，即可调用Route::routeMessage；

如果不在Route中，如Todo，则如下实现：

```java
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
    //...省略
}
```

