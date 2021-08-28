
# 重构过程001

## 重构代码思路
目前代码的问题
- 一个文件有多个类，路由函数和Server混在一起，socket 操作和 log 函数, 都应该拆分出去等

1. Server中的Request类可以单独拎出来，作为请求，把关键部分
    String rawData;
    String path;
    String method;
    String body;
    HashMap<String, String> query;
    HashMap<String, String> form;
	等处理一下。
	
	注意这里的rawRequest为原始数据，path/query为解析后的路径用来做route用，method一般为get或者post，
	body/form主要是把form表单内容处理一下，改为key-value的键值对。
	
2. Server中的各种route，单独拎出来做一个类。
	Server中可以有各种route的入口，但是route做一个单独的类来分发路由，看的比较清爽。
	
3. Server中的socketReadAll和socketSendAll也可以单独拎出来，因为属于SocketOperator类，并不需要放到Server里。


- 图片文件和 html 文件都混在一起, 没放在单独的文件夹中

1. html的模板，可以由一个template目录存放，后续所有的html页面，都可以存放在这里

2. 图片可以作为static目录下的静态资源来存放。


## 新增留言板功能

- 用内存来存数据(用一个变量来存)

```Java
    // 叫静态变量, 类独有, 在类第一次载入的时候初始化
    static ArrayList<String> messageList = new ArrayList<>();
	
    static byte[] routeMessage(Request request) {
        HashMap<String, String> data = null;
		//解析method，分别为POST或者GET
        if (request.method.equals("POST")) {
            data = request.form;
        } else if (request.method.equals("GET")) {
            data = request.query;
        } else {
            String m = String.format("Unknown method (%s)", request.method);
            throw new RuntimeException(m);
        }

//        Utils.log("request message data (%s)", data);
        if (data != null) {
            // HashMap 的 toString 方法, 可以把内部的 key 和 value 的值
            // 都放进一个字符串当中
            String author = data.get("author");
            String message = data.get("message");
            int index = messageList.size() ;
            String m = String.format("%s. %s<br>", index + 1 , author);
            String n = String.format("%s. %s<br>", index + 2 , message);
            messageList.add(m);
            messageList.add(n);
            }
        Utils.log("messageList (%s)", messageList);

        String header = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n";
        String body = html("html_basic.html");
        // [{author=222, message=2sdfsdfsdf}]
        body = body.replace("{messages}", messageList.toString());

        String resposne = header + body;
        return resposne.getBytes();
    }
```

- 在 html 上显示留言数据，分别为GET方法和POST方法

```html
        <form action="/message" method="get">
            <!-- textarea 是一个文本域 -->
            <!-- name rows cols 都是属性 -->
            <input style="border: solid" name="author"/>
            <br/>
            <textarea name="message" rows="8" cols="40"></textarea>
            <br/>
			
			<button type="submit">GET 提交</button>
        </form>
		
		
		 <form action="/message" method="post">
            <input style="border: solid" name="author" />
            <!--<input name="name">-->
            <br>
            <textarea name="message" rows="8" cols="40"></textarea>
            <br>
            <button type="submit">POST 提交</button>
        </form>
```

- 留言数据 改成 txt 存储，会优化拆分留言板的逻辑, 向工作中的 MVC 框架靠拢
分出message类

```Java
class Message {
    String author;
    String message;

    Message() {

    }

    @Override
    public String toString() {
        String s = String.format(
                "(author: %s, message: %s)",
                this.author,
                this.message
        );
        return s;
    }
}
```

```Java
// Service 这一层, 负责具体的读写数据逻辑, 从哪读, 存哪里
class MessageService {
    static void add(HashMap<String, String> form) {
        Message m = new Message();
        String author = form.get("author");
        String message = form.get("message");
        m.author = author;
        m.message = message;

        ArrayList<Message> messageList = load();
        messageList.add(m);
        MessageService.save(messageList);

    }

    static void save(ArrayList<Message> list) {
        StringBuilder sb = new StringBuilder();
        for (Message m : list) {
            String author = m.author;
            sb.append(author);
            sb.append("\n");

            String message = m.message;
            sb.append(message);
            sb.append("\n");
        }
//        Utils.log("saveFile (%s)", sb.toString());

        String filename = Message.class.getSimpleName() + ".txt";
        Utils.save(filename, sb.toString());
    }

    static ArrayList<Message> load() {
        String filename = Message.class.getSimpleName() + ".txt";
        String content = Utils.load(filename);
        String[] lines = content.split("\n");

        ArrayList<Message> ms = new ArrayList<>();

        int fieldCount = 2;
        for (int i = 0; i < lines.length; i = i + fieldCount) {
//            Utils.log("line (%s)", lines[i]);
            String author = lines[i];
            String message = lines[i + 1];

            Message m = new Message();
            m.author = author;
            m.message = message;
            ms.add(m);

        }
        return ms;
    }


    static String messageListShowString() {
        ArrayList<Message> all = load();
        StringBuilder sb = new StringBuilder();
        for (Message m : all) {
            // <br> 在 html 里面代表换行符
            String s = String.format("%s: %s <br>", m.author, m.message);
            sb.append(s);
        }

        return sb.toString();
    }

    static Message findByAuthor(ArrayList<Message> messages, String author) {
        Message result = new Message();
        for (Message m : messages) {
            String auter_match = m.author;
            if (auter_match.equals(author)) {
                result.author = m.author;
                result.message = m.message;
                break;
            } else {
                result = null;
            }
        }
        return result;
    }
}

```


其中加载和保存文件内容，可以单独拎出来，作为工具类中的load和save方法。
```Java
    public static String load(String path) {
        // String path = dir + "/" + htmlName;
        byte[] body = new byte[1];
        try (FileInputStream is = new FileInputStream(path)) {
            byte[] tmp = is.readAllBytes();
            body = tmp;
        } catch (IOException e) {
            String m = String.format("Load file <%s> error <%s>", path, e);
            throw new RuntimeException(m);
        }

        String r = new String(body, StandardCharsets.UTF_8);
        return r;
    }
	
	    public static void save(String path, String data) {
        try (FileOutputStream os = new FileOutputStream(path)) {
            os.write(data.getBytes());
        } catch (IOException e) {
            String m = String.format("Save file <%s> error <%s>", path, e);
            throw new RuntimeException(m);
        }
    }

```







