package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;

public class GroupPageActivity extends RightDrawerBaseActivity implements SessionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        setFragmentInfo();
    }

    @Override
    public void setFragmentInfo() {
        //get bundle info
        int bundleGroupId = getIntent().getExtras().getInt(PageTransitions.GROUPID);
        int bundleUserId = getIntent().getExtras().getInt(PageTransitions.USERID);
        String bundleGroupName = getIntent().getExtras().getString(PageTransitions.GROUPNAME);

        //set title of activity
        setTitle(bundleGroupName);

        //access code part (top half)
        FragmentManager fm = getFragmentManager();
        GroupPageFragment fragment = (GroupPageFragment) fm.findFragmentById(R.id.group_page_fragment);

        fragment.setSession(session);
        fragment.setDbhelper(dbhelper);
        fragment.setIDs(bundleGroupId, bundleUserId);

        //my tasks part (second half)
        UserTasksFragment fragment2 = (UserTasksFragment) fm.findFragmentById(R.id.my_tasks_fragment);
        fragment2.setSession(session);
        fragment2.setDbhelper(dbhelper);
        fragment2.setIDs(bundleGroupId, bundleUserId);
    }
}
