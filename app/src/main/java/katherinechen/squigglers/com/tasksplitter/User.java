package katherinechen.squigglers.com.tasksplitter;

/**
 * Created by lindsaycampbell on 12/8/14.
 */
public class User {
    private int userId;
    private String userName;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
