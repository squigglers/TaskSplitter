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
        FragmentManager fm = getFragmentManager();
        UserTasksFragment fragment = (UserTasksFragment) fm.findFragmentById(R.id.user_tasks_fragment);

        fragment.setSession(session);
        fragment.setDbhelper(dbhelper);
    }
}