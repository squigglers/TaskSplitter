package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class UserTasksActivity extends RightDrawerBaseActivity implements SessionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tasks);

        setFragmentInfo();
    }

    @Override
    public void setFragmentInfo() {
        //get bundle info
        int bundleGroupId = getIntent().getExtras().getInt(PageTransitions.GROUPID);
        int bundleUserId = getIntent().getExtras().getInt(PageTransitions.USERID);
        String bundleGroupName = getIntent().getExtras().getString(PageTransitions.GROUPNAME);
        boolean bundleArchived = getIntent().getExtras().getBoolean(PageTransitions.ARCHIVED);

        //set title
        String title;
        if(bundleUserId != 0) { //for archived tasks
            String userName = dbhelper.getUserName(bundleUserId);
            title = bundleGroupName + " - " + userName;
        }
        else
            title = bundleGroupName + " - archived";
        setTitle(title);

        FragmentManager fm = getFragmentManager();
        UserTasksFragment fragment = (UserTasksFragment) fm.findFragmentById(R.id.user_tasks_fragment);

        fragment.setSession(session);
        fragment.setDbhelper(dbhelper);
        fragment.setIDs(bundleGroupId, bundleUserId);
        fragment.setArchived(bundleArchived);
    }
}