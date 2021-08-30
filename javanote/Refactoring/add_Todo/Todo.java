package usoppMVC.models;

public class Todo {
    public Integer id;
    public String content;
    public Boolean completed;
    public Integer userId;

    @Override
    public String toString() {
        String s = String.format(
            "(id: %s, content: %s, completed: %s, userId)",
            this.id,
            this.content,
            this.completed,
             this.userId
        );
        return s;
    }
}
