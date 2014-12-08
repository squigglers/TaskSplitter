package katherinechen.squigglers.com.tasksplitter.dummy;

/**
 * Created by lindsaycampbell on 12/8/14.
 */
public class Task {
    private int taskId;
    private String taskname;

    public Task(int taskId, String taskname) {
        this.taskId = taskId;
        this.taskname = taskname;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskname() {
        return taskname;
    }
}



