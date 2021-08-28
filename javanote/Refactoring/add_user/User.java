package usopppMVC.models;

public class User {
    public String username;
    public String password;
    public String note;


    @Override
    public String toString() {
        String s = String.format(
            "(username: %s, password: %s, note: %s)",
            this.username,
            this.password,
            this.note
        );
        return s;
    }
}
