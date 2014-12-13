package katherinechen.squigglers.com.tasksplitter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PageTransitions {

    public static String GROUPID = "GROUPID";
    public static String USERID = "USERID";
    public static String GROUPNAME = "GROUPNAME";

    public static void goToGroupPage(int groupId, String groupName, Context context, SessionManager session){
        //start new intent for the new activity
        Intent intent = new Intent(context, GroupPageActivity.class);

        //bundle groupid and userid
        Bundle bundle = new Bundle();
        bundle.putInt(GROUPID, groupId);
        bundle.putInt(USERID, session.getUserId());
        bundle.putString(GROUPNAME, groupName);
        intent.putExtras(bundle);

        //clear backstack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);;

        //start intent
        context.startActivity(intent);
    }

    public static void goToGroupPage(int groupId, String groupName, int userId, Context context, SessionManager session){
        //start new intent for the new activity
        Intent intent = new Intent(context, UserTasksActivity.class);

        //bundle groupid and userid
        Bundle bundle = new Bundle();
        bundle.putInt(GROUPID, groupId);
        bundle.putInt(USERID, userId);
        bundle.putString(GROUPNAME, groupName);
        intent.putExtras(bundle);

        //clear backstack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);;

        //start intent
        context.startActivity(intent);
    }
}
