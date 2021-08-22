// class URL {
//     String protocol;
//     String host;
//     int port;
//     String path;
//
//     private static void log(String format, Object... args) {
//         System.out.println(String.format(format, args));
//     }
//
//     URL(String url) {
//         String separator = "://";
//         Integer i = url.indexOf(separator);
//
//         // 解析协议
//         String protocol = "";
//         String u = "";
//         if (i.equals(-1)) {
//             protocol = "http";
//             u = url;
//         } else {
//             protocol = url.substring(0, i);
//             u = url.substring(i + separator.length());
//         }
//         log("protocol (%s)", protocol);
//         log("剩下的 url (%s)", u);
//
//         i = u.indexOf("/");
//         String host = "";
//         String path = "";
//         if (i.equals(-1)) {
//             path = "/";
//             host = u;
//         } else {
//             path = u.substring(i);
//             host = u.substring(0, i);
//         }
//         log("path (%s)", path);
//         log("host (%s)", host);
//
//
//         i = host.indexOf(":");
//         int port;
//         if (i.equals(-1)) {
//             if (protocol.equals("http")) {
//                 port = 80;
//             } else {
//                 port = 443;
//             }
//         } else {
//             String[] parts = host.split(":", 2);
//             host = parts[0];
//             port = Integer.parseInt(parts[1]);
//         }
//
//         log("解析完毕 (%s) (%s) (%s) (%s)", protocol, host, port, path);
//
//         this.protocol = protocol;
//         this.host = host;
//         this.port = port;
//         this.path = path;
//     }
// }


public class URLParse {
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
    /*
    url 是字符串, 可能的值如下
    'g.cn'
    'g.cn/'
    'g.cn:3000'
    'g.cn:3000/search'
    'http://g.cn'
    'https://g.cn'
    'http://g.cn/'
    返回一个数组, 内容如下 {protocol, host, port, path}
    */
    public static void main(String[] args) {
        String url = "hello";
        log("%s", url.substring(1, 3));
        URL u = new URL(url);

        log("%s", u.path);
        log("%s", u.protocol);
        log("%s", u.port);
        log("%s", u.host);

    }
}
