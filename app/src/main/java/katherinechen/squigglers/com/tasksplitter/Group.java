package katherinechen.squigglers.com.tasksplitter;

public class Group {
    private int groupId;
    private String groupname;

    public Group(int groupId, String groupname) {
        this.groupId = groupId;
        this.groupname = groupname;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupname() {
        return groupname;
    }
}
