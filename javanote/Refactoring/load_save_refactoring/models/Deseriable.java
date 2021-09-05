package usoppMVC.models;


import java.util.List;

public interface Deseriable<T> {
    T deserialize(List<String> modelData);
}
