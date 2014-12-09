package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class UserTasksActivity extends LoggedInBaseActivity implements SessionInterface {
    public int userId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tasks);

//        userId = getIntent().getExtras().getInt("userId");
        //if(userId == -1)
        //    userId = session.getUserId();

        setFragmentInfo();
    }

    @Override
    public void setFragmentInfo() {
        FragmentManager fm = getFragmentManager();
        //Bundle bundle = new Bundle();
        //bundle.putInt("userId", userId);
        UserTasksFragment fragment = (UserTasksFragment) fm.findFragmentById(R.id.user_tasks_fragment);

        //fragment.setArguments(bundle);
        fragment.setSession(session);
        fragment.setDbhelper(dbhelper);
    }
}