package usoppMVC;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

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

    public static byte[] loadImage(String path) {
        // String path = dir + "/" + htmlName;
        byte[] data = new byte[1];
        try (FileInputStream is = new FileInputStream(path)) {
            byte[] tmp = is.readAllBytes();
            data = tmp;
        } catch (IOException e) {
            String m = String.format("Load file <%s> error <%s>", path, e);
            throw new RuntimeException(m);
        }
        return data;
    }


    public static void save(String path, String data) {
        try (FileOutputStream os = new FileOutputStream(path)) {
            os.write(data.getBytes());
        } catch (IOException e) {
            String m = String.format("Save file <%s> error <%s>", path, e);
            throw new RuntimeException(m);
        }
    }

    public static String html(String htmlName) {
        String dir = "template";
        String path = String.format("%s/%s", dir, htmlName);
        // String path = dir + "/" + htmlName;
        byte[] body = new byte[1];
        try (FileInputStream is = new FileInputStream(path)) {
            byte[] tmp = is.readAllBytes();
            body = tmp;
        } catch (IOException e) {
            e.printStackTrace();
        }

        String r = new String(body, StandardCharsets.UTF_8);
        return r;
    }
}
