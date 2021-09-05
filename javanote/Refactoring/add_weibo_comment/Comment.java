package usoppMVC.models;

public class Comment extends BaseModel{
    public Integer id;
    public String content;
    public Integer userId;
    public Integer weiboId;

    // @Override
    // public String toString() {
    //     String s = String.format(
    //         "(id: %s, content: %s, completed: %s, userId)",
    //         this.id,
    //         this.content,
    //         this.completed,
    //          this.userId
    //     );
    //     return s;
    // }
}
