package katherinechen.squigglers.com.tasksplitter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PageTransitions {

    public static String GROUPID = "GROUPID";
    public static String USERID = "USERID";
    public static String GROUPNAME = "GROUPNAME";

    public static String TASKID = "TASKID";
    public static String ARCHIVED = "ARCHIVED";

    //grouppage for current user
    public static void goToGroupPage(int groupId, String groupName, int userId, Context context){
        //start new intent for the new activity
        Intent intent = new Intent(context, GroupPageActivity.class);
        boolean archived = false;

        //bundle
        Bundle bundle = new Bundle();
        bundle.putInt(GROUPID, groupId);
        bundle.putString(GROUPNAME, groupName);
        bundle.putInt(USERID, userId);
        bundle.putBoolean(ARCHIVED, false);
        intent.putExtras(bundle);

        //clear backstack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);;

        //start intent
        context.startActivity(intent);
    }

    //go to usertasksactivity with archived tasks
    public static void archivedTasks(int groupId, String groupName, int userId, Context context) {
        //start new intent for the new activity
        Intent intent = new Intent(context, UserTasksActivity.class);
        boolean archived = false;

        //bundle
        Bundle bundle = new Bundle();
        bundle.putInt(GROUPID, groupId);
        bundle.putString(GROUPNAME, groupName);
        bundle.putInt(USERID, userId);
        bundle.putBoolean(ARCHIVED, true);
        intent.putExtras(bundle);

        //clear backstack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);;

        //start intent
        context.startActivity(intent);
    }
}
