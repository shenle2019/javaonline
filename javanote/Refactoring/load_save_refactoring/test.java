package usoppMVC;

class UserRoleClass {
    static String admin = "admin";
    static String normal = "normal";
    static String guest = "guest";
}


enum UserRoleEnum {
    admin, normal, guest
}


class User {
    UserRoleEnum role;
}


public class test {
    public static void main(String[] args) {
        User u = new User();
        u.role = UserRoleEnum.admin;

        Utils.log("是不是 admin %s");
    }
}
