# Java-web学习笔记004

## 网址组成（四部分）
-----------------

1. 协议(protocol): http, shttp（https 是加密的 http）
2. 主机(host): g.cn  zhihu.com之类的网址
3. 端口(port): HTTP 协议默认是 80，因此一般不用填写。HTTPS 默认是 443。
4. 路径(path): 下面的「/」和「/question/1883」都是路径 


http://www.zhihu.com/
http://www.sina.com.cn:80/
https://www.zhihu.com:443/
http://www.zhihu.com/question/31838184
http://www.zhihu.com/
https://www.baidu.com/s?wd=324&rsv_spt=1&qq-pf-to=pcqq.group

IP地址：192.168.1.1
ipv4, ipv6
电脑通信靠IP地址，IP地址记不住就发明了域名（domain name）。
然后电脑自动向DNS服务器（domain name server）查询域名对应的IP地址。
比如g.cn这样的网址，可以通过电脑的ping程序查出对应 IP 地址
```
➜    ping g.cn
PING g.cn (74.125.69.160): 56 data bytes
```

## 端口是什么？
------------

一个比喻：
- 用邮局互相写信的时候，ip相当于地址（也可以看做邮编，地址是域名）。
- 端口是收信人姓名（因为一个地址比如公司、家只有一个地址，但是却可能有很多收信人）。
- 端口就是一个标记收信人的数字。
- 端口是一个 16 位的数字，所以范围是 0-65535（2**16）。



## 普通浏览器 HTTP 请求的头部
--------------------------

```
client 连接成功
请求:
GET / HTTP/1.1
Host: localhost:9000
Connection: keep-alive
Pragma: no-cache
Cache-Control: no-cache
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3
Sec-Fetch-Site: cross-site
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9


GET /favicon.ico HTTP/1.1
Host: localhost:2000
Connection: keep-alive
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36
Accept: image/webp,image/apng,image/*,*/*;q=0.8
DNT: 1
Referer: http://localhost:2000/
Accept-Encoding: gzip, deflate, br
Accept-Language: en-US,en;q=0.8,it-IT;q=0.6,it;q=0.4,zh;q=0.2

```

## HTTP (Hypertext Transfer Protocol) 协议 
--------
实际上底层传输的都是 10101010 二进制数据
 
一个传输协议，协议就是双方都遵守的规范。
为什么叫超文本传输协议呢，因为收发的是超文本信息。
超链接（hyperlink）链接
1. 浏览器（客户端）按照规定的格式发送文本数据（请求）到服务器
2. 服务器解析请求，按照规定的格式返回文本数据到浏览器
3. 浏览器解析得到的数据，并做相应处理

请求和返回是一样的数据格式，分为4部分：
1. 请求行或者响应行
2. Header（请求的 Header 中 Host 字段是必须的，其他都是可选）
3. \r\n\r\n（连续两个换行回车符，用来分隔Header和Body）
                                                             
4. Body（可选）

请求的格式，注意大小写（这是一个不包含Body的请求）：
原始数据如下

`GET  HTTP/1.1\r\nHost:g.cn\r\n\r\n`

打印出来如下
```
GET / HTTP/1.1
Host: g.cn

```

其中
1. GET 是请求方法（还有POST等，这就是个标志字符串而已）
2. / 是请求的路径（这代表根路径）
3. HTTP/1.1  中，1.1是版本号，通用了20年

具体字符串是 `GET / HTTP/1.1\r\nHost:g.cn\r\n\r\n`


## Client.java

- client客户端的作用是，发送请求，如`GET / HTTP/1.1\r\nHost:g.cn\r\n\r\n`
GET 后面第一个/是path
Host比如g.cn，也可以查豆瓣，douban.com

- 建立网络连接的方法
Socket socket = new Socket(host, port)
// 发送 HTTP 请求给服务器
            socketSendAll(socket, request);

- 然后接受response，解析出来
  // 接受服务器的响应数据
            String response = socketReadAll(socket);
            // 输出响应的数据
            log("响应:\n%s", response);
			
- 那么我们就要分别实现socketSendAll(socket, request)

    private static void socketSendAll(Socket socket, String request) throws IOException {
        OutputStream output = socket.getOutputStream();
            // bytes 就是二进制, write 发送的都是二进制
        output.write(request.getBytes());
    }
其中
字符转字节
```
StringBuilder sb = new StringBuilder();
sb.toString().getBytes(StandardCharsets.UTF_8);
```
字节转字符
```
byte[] response = new byte[1000];
String r = new String(response, StandardCharsets.UTF_8);

```

- 实现 socketReadAll(Socket socket)
```Java
    private static String socketReadAll(Socket socket) throws IOException {
            InputStream input = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            // 指定读取的数据长度为 1024
            int bufferSize = 1024;
            // 初始化指定长度的数组
            char[] data = new char[bufferSize];
            // read 函数会把读到的数据复制到 data 数组中去
            // size 是实际读到的数据的长度, 字节为单位
            int size = reader.read(data, 0, data.length);

            // 通常我们会用 StringBuilder 来处理字符串拼接
            // 它比 String 相加运行效率快很多
            // "a" + "b";
            StringBuilder response = new StringBuilder();
            // append 方法会把 data 数组的内容转成字符串
            response.append(data, 0, size);
            return response.toString();
    }
```

返回的数据如下
```
HTTP/1.1 301 Moved Permanently
Content-Length: 218                          
Content-Type: text/html; charset=UTF-8
Location: http://www.google.cn/
```

Body 和其他不重要的头部就不贴了

其中响应行（第一行）：
1. HTTP/1.1 是版本
2. 301 是「状态码」，参见文末链接
3. Moved Permanently 是状态码的描述

浏览器会自己解析Header部分，然后将Body显示成网页

## 豆瓣返回的响应
```
HTTP/1.1 301 Moved Permanently
Date: Thu, 17 Oct 2019 06:29:54 GMT
Content-Type: text/html
Content-Length: 162
Connection: keep-alive
Keep-Alive: timeout=30
Location: http://www.douban.com/
Server: dae
Strict-Transport-Security: max-age=15552000;

<html>
<head><title>301 Moved Permanently</title></head>
<body>
<center><h1>301 Moved Permanently</h1></center>
<hr><center>nginx</center>
</body>
</html>
```



## web服务器做什么
---------------
主要就是解析请求，发送相应的数据给客户端。
例如 client.java 就是模拟浏览器发送 HTTP 请求给服务器并把收到的所有信息打印出来。
使用的是最底层的 socket，现阶段不必关心这种低层，web开发是上层开发。
socket 就是插座,

localhost = 127.0.0.1
完整的 socket 要有四个东西
发送方的 ip 和 port, 接受方的 IP 和 port

编码
101010
1 或者 0, 就叫做一位
8 个 1 或 0, 分一组, 叫一个字节

101 转成 10 进制就是 5,

编码表, 每个数字, 对应一个字符
----
- 计算机对数据的表示
- ASCII
- gb gb2312 gbk(ISO 10646 国际标准兼容) gb18030
- unicode utf-8
- bytes String 互相转换


## Server

服务器就是要一直跑着的，所以主函数是run()
    public static void main(String[] args) {
        run();
    }
	
实现run函数，让它进入while循环，一直持续测试Socket socket = serverSocket.accept()
如果有连接，则执行String request = socketReadAll(socket);来读取数据
有request先返回一个html页面socketSendAll(socket, response);来返回数据
否则返回 log("接受到一个空请求");

分别实现方法socketReadAll
private static String socketReadAll(Socket socket) throws IOException	

和方法socketSendAll(socket, response);

 
