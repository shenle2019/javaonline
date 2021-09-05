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
