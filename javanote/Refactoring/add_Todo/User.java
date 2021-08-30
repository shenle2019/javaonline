package usoppMVC.models;

public class User {
    public Integer id;
    public String username;
    public String password;


    @Override
    public String toString() {
        String s = String.format(
            "(id %s, username: %s, password: %s)",
            this.id,
            this.username,
            this.password
        );
        return s;
    }
}
