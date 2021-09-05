package usoppMVC;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Request {
    public String rawData;
    public String path;
    public String method;
    public String body;
    public HashMap<String, String> query;
    public HashMap<String, String> form;
    public HashMap<String, String> headers;
    public HashMap<String, String> cookies;


    Request(String rawRequest) {
        this.rawData = rawRequest;
        String[] parts = rawRequest.split("\r\n\r\n", 2);
        this.body = parts[1];
        String headers = parts[0];

        // 解析 http 头
        String[] lines = headers.split("\r\n");
        String requestLine = lines[0];
        String[] requestLineData = requestLine.split(" ");
        this.method = requestLineData[0];
        this.addHeaders(headers);

        // 解析 path
        this.parsePath(requestLineData[1]);

        // 解析 body
        this.parseForm(this.body);
    }

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

    public void parsePath(String path) {
        // /message?author=xxx&message=111
        Integer index = path.indexOf("?");

        if (index == -1) {
            this.path = path;
            this.query = null;
        } else {
            this.path = path.substring(0, index);
            String queryString = path.substring(index + 1);
            String[] args = queryString.split("&");

            // for (int i = 0; i < args.length; i++) {
            //     String e = args[i];
            // }

            this.query = new HashMap<>();
            for (String e: args) {
                // author=
                String[] kv = e.split("=", 2);
                String k = kv[0];
                String v = kv[1];
                this.query.put(k, v);
            }
        }
    }

    public void parseForm(String body) {
        // author=ddd&message=ddd
        // 如果 body 部分为空, 那么 this.form = null;
        // form 指的就是 body 里面的表单数据

        if (body.length() > 0) {
            // 把中文和空格这些符号正确的解析出来
            body = URLDecoder.decode(body, StandardCharsets.UTF_8);
            String[] args = body.split("&");
            this.form = new HashMap<>();
            for (String e: args) {
                // author=sss%message=
                String[] kv = e.split("=");
                String k = kv[0];
                String v = kv[1];
                // Utils.log("k (%s), v(%s)", k, v);
                this.form.put(k, v);
            }
        } else {
            this.form = null;
        }
    }
}
