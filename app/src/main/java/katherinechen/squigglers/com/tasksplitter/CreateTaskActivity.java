package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class CreateTaskActivity extends LoggedInBaseActivity implements SessionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        setFragmentInfo();
    }

    @Override
    public void setFragmentInfo() {
        FragmentManager fm = getFragmentManager();
        CreateTaskFragment fragment = (CreateTaskFragment) fm.findFragmentById(R.id.create_task_fragment);

        fragment.setSession(session);
        fragment.setDbhelper(dbhelper);
    }
}
