# Java-web学习笔记007

## Cookie 和 Session

Cookie 和 Session的几个问题：

- cookie 是什么？

- 客户端和服务器怎么使用 cookie？

  

怎么知道是哪个用户？
发请求的时候
GET /index?id=1

服务器给浏览器的响应头中, 带上 Set-Cookie: username: usopp这一行
之后, 浏览器给的服务器发的所有请求头, 都会带上
Cookie: username=usopp
这一行

Cookie 可以解决 http 请求无状态的问题
我们可以辨别出几个 http 请求都是同一个用户发的



- session 是什么？

- session 有什么用？

- 客户端和服务器端分别怎么实现 session？

- session 持久化 （持久化就是重启后仍然可以使用）的两种方式：

  - 保存到文件
  - 加密加签名

- session 共享 / sso(single sign on) / 单点登录

  

但是, 我们没有解决伪造的问题

我让 cookie 存一个无意义的字符串
然后我的服务器上存一个 HashMap, 存了哪个无意义的字符串对应哪个username

这样子我们就能解决伪造的问题了

这就是 Session(会话)，保证一个会话中对应一个cookie和对应的无意义的字符串（映射到hashmap中的user），

其他session中的无意义字符串不一样，导致不是同一个session。

1. 记住用户状态
2. 解决伪造

session 本质还是要给浏览器存一个东西
你要存一个没人能猜到的东西
一个随机的字符串, token(令牌)
有令牌. 服务器上就要存一个对应关系表, 那个令牌对那个用户

补充说明：

Cookie虽然在一定程度上解决了“保持状态”的需求，但是由于Cookie本身最大支持4096字节，以及Cookie本身保存在客户端，可能被拦截或窃取，因此就需要有一种新的东西，它能支持更多的字节，并且他保存在服务器，有较高的安全性。这就是Session。

问题来了，基于HTTP协议的无状态特征，服务器根本就不知道访问者是“谁”。那么上述的Cookie就起到桥接的作用。

我们可以给每个客户端的Cookie分配一个唯一的id，这样用户在访问时，通过Cookie，服务器就知道来的人是“谁”。然后我们再根据不同的Cookie的id，在服务器上保存一段时间的私密资料，如“账号密码”等等。

总结而言：Cookie弥补了HTTP无状态的不足，让服务器知道来的人是“谁”；但是Cookie以文本的形式保存在本地，自身安全性较差；所以我们就通过Cookie识别不同的用户，对应的在Session里保存私密的信息以及超过4096字节的文本。

另外，上述所说的Cookie和Session其实是共通性的东西，不限于语言和框架。

参考文档：[Cookie和Session](https://www.cnblogs.com/yunwangjun-python-520/p/10926360.html)



## 其他补充

怎么偷令牌？
发一个请求的路径

你的电脑-你的路由器-小区路由器-电信-服务器

https 就是用 http 发送加密的数据



- Session 需要设置过期时间，这个时间是有讲究的





## 代码实例

Model层添加

```java
package usoppMVC.models;

public class Session {
    public String sessionId;
    public Integer userId;

    @Override
    public String toString() {
        String s = String.format(
            "(sessionId: %s, userId: %s)",
            this.sessionId,
            this.userId
        );
        return s;
    }
}

```

Service层添加：

SessionService.java

```java
package usoppMVC.service;

import usoppMVC.Utils;
import usoppMVC.models.Session;
import usoppMVC.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

// Service 这一层, 负责具体的读写数据逻辑, 从哪读, 存哪里
public class SessionService {
    public static Session add(Integer userId) {
        Session m = new Session();
        m.sessionId = UUID.randomUUID().toString();
        m.userId = userId;

        ArrayList<Session> sessionArrayList = load();
        sessionArrayList.add(m);
        SessionService.save(sessionArrayList);
        return m;
    }

    static void save(ArrayList<Session> list) {
        StringBuilder sb = new StringBuilder();
        for (Session m : list) {
            String sessionId = m.sessionId;
            sb.append(sessionId);
            sb.append("\n");

            Integer userId = m.userId;
            sb.append(userId);
            sb.append("\n");
        }
        Utils.log("saveFile (%s)", sb.toString());

        String filename = Session.class.getSimpleName() + ".txt";
        Utils.save(filename, sb.toString());
    }

    static ArrayList<Session> load() {
        String filename = Session.class.getSimpleName() + ".txt";
        String content = Utils.load(filename);
        String[] lines = content.split("\n");

        if (content.strip().length() != 0) {
            ArrayList<Session> ms = new ArrayList<>();
            int fieldCount = 2;
            for (int i = 0; i < lines.length; i = i + fieldCount) {
                String sessionId = lines[i];
                Integer userId = Integer.valueOf(lines[i + 1]);

                Session m = new Session();
                m.sessionId = sessionId;
                m.userId = userId;
                ms.add(m);
            }
            return ms;
        } else {
            return new ArrayList<>();
        }
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

```

路由Route中添加判断，当前用户，如果能找到session_id，则返回user，如果找不到则返回【游客】；如果没有session_id，也返回【游客】；

```java
    public static User currentUser(Request request) {
        if (request.cookies.containsKey("session_id")) {
            String sessionId = request.cookies.get("session_id");
            Session session = SessionService.findBySession(sessionId);
            User user = UserService.findById(session.userId);
            if (user == null) {
                return UserService.guest();
            } else {
                return user;
            }
        } else {
            return UserService.guest();
        }

    }
```

后续也有判断 ：

```java
public static byte[] routeIndex(Request request) {
    User user = currentUser(request);
```



用户登录时判断

```java
    public static byte[] routeLogin(Request request) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/html");

        HashMap<String, String> data = null;
        String result = "";
        String loginUserName = "游客";
        if (request.method.equals("POST")) {
            data = request.form;
            boolean valid = UserService.validLogin(data);
            if (valid) {
                User u = UserService.loginUser(data);
                loginUserName = u.username;
                Session session = SessionService.add(u.id);
                header.put("Set-Cookie", String.format("session_id=%s", session.sessionId));
                result = "登录成功";
            } else {
                result = "登录失败";
            }
        }

        String body = html("login.html");
        body = body.replace("{loginResult}", result);
        body = body.replace("{loginUser}", loginUserName);

        String response = Route.responseWithHeader(200, header, body);
        return response.getBytes();
    }
```



Request类添加：

```java
    public class Request {
    public HashMap<String, String> cookies;
    
    //...省略其他内容，添加addheaders中添加cookie设置
    public void addHeaders(String headString) {
        this.headers = new HashMap<>();

        String[] lineArray = headString.split("\r\n");

        for (int i = 1; i < lineArray.length; i++) {
            String line = lineArray[i];
            String[] kv = line.split(":", 2);
            this.headers.put(kv[0].strip(), kv[1].strip());
        }

        // Cookie: username=usopp; id=1; ddddddd=; ddddddd;

        this.cookies = new HashMap<>();
        if (this.headers.containsKey("Cookie")) {
            String values = this.headers.get("Cookie");
            String args[] = values.split(";");
            for (String kvString: args) {
                String[] kv = kvString.split("=", 2);
                if (kv.length >= 2) {
                    this.cookies.put(kv[0].strip(), kv[1].strip());
                } else {
                    this.cookies.put(kv[0].strip(), kv[0].strip());
                }
            }
        }
      }
    }

```



