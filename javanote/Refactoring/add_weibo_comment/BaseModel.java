package usoppMVC.models;

import usoppMVC.Utils;

import java.lang.reflect.Field;

public class BaseModel {

    @Override
    public String toString() {
        // 得到所有的属性
        Field[] fields = this.getClass().getDeclaredFields();

        // Messageauthor: usopp3message: 5555
        // (Message: author(usopp), message(5555))

        /*
        (Message:
        author: usopp
        message: 5555
        )
         */
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(this.getClass().getSimpleName() + ":\n");

        // 遍历属性
        for (Field f: fields) {
            // a.author
            try {
                // 得到属性的值
                Object v = f.get(this);
                String kv = String.format("%s: %s\n", f.getName(), v);
                sb.append(kv);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sb.append(")");
        return sb.toString();
    }
}
