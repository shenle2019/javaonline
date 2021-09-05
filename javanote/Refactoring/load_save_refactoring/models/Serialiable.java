package usoppMVC.models;


import java.util.List;

public interface Serialiable<T> {
    String serialize(T model);
}
