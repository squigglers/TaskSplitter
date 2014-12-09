package katherinechen.squigglers.com.tasksplitter;

public class Group {
    private int groupId;
    private String groupname;
    private String accessCode;

    public Group(int groupId, String groupname, String accessCode) {
        this.groupId = groupId;
        this.groupname = groupname;
        this.accessCode = accessCode;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getGroupname() {
        return groupname;
    }
}
