import java.util.HashMap;

// class Request {
//     String rawData;
//     String path;
//     String method;
//     String body;
//     HashMap<String, String> query;
//     HashMap<String, String> form;
//
//     private static void log(String format, Object... args) {
//         System.out.println(String.format(format, args));
//     }
//
//     Request(String rawRequest) {
//         this.rawData = rawRequest;
//         String[] parts = rawRequest.split("\r\n\r\n", 2);
//         this.body = parts[1];
//         String headers = parts[0];
//
//         // 解析 http 头
//         String[] lines = headers.split("\r\n");
//         String requestLine = lines[0];
//         String[] requestLineData = requestLine.split(" ");
//         this.method = requestLineData[0];
//
//         // 解析 path
//         this.parsePath(requestLineData[1]);
//
//         // 解析 body
//         this.parseForm(body);
//     }
//
//     public void parsePath(String path) {
//         // /message?author=xxx&message=111
//         Integer index = path.indexOf("?");
//
//         if (index == -1) {
//             this.path = path;
//             this.query = null;
//         } else {
//             this.path = path.substring(0, index);
//             String queryString = path.substring(index + 1);
//             log("queryString <%s>", queryString);
//             String[] args = queryString.split("&");
//
//             // for (int i = 0; i < args.length; i++) {
//             //     String e = args[i];
//             // }
//
//             this.query = new HashMap<>();
//             for (String e: args) {
//                 log("args e (%s)", e);
//                 // author=
//                 String[] kv = e.split("=", 2);
//                 log("kv length %s", kv.length);
//                 String k = kv[0];
//                 String v = kv[1];
//                 log("k (%s), v(%s)", k, v);
//                 this.query.put(k, v);
//             }
//         }
//     }
//
//     public void parseForm(String body) {
//         // author=ddd&message=ddd
//         // 如果 body 部分为空, 那么 this.form = null;
//         // form 指的就是 body 里面的表单数据
//     }
// }

public class ReqeustParse {
    public static void main(String[] args) {
        String request = "POST /message?author=&message=111 HTTP/1.1\r\n" +
                "Host: localhost:9000\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Length: 22\r\n" +
                "Pragma: no-cache\r\n" +
                "Cache-Control: no-cache\r\n" +
                "Origin: http://localhost:9000\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: same-origin\r\n" +
                "Referer: http://localhost:9000/message\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: zh-CN,zh;q=0.9\r\n" +
                "Cookie: Idea-65fadc5a=bc833dc0-2e67-4a4b-ae67-f7f3c3aaf54c; Webstorm-a444f122=351b16de-8d04-4345-9ed2-a43532865ec1\r\n" +
                "\r\n" +
                "author=ddd&message=ddd";

        Request r = new Request(request);
    }
}
