package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class CreateTaskActivity extends LoggedInBaseActivity implements SessionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
    }

    @Override
    public void setFragmentInfo() {
        FragmentManager fm = getFragmentManager();
        //CreateGroupFragment fragment = (CreateGroupFragment) fm.findFragmentById(R.id.create_group_fragment);
        CreateGroupFragment fragment = new CreateGroupFragment();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();

        fragment.setSession(super.session);
        fragment.setDbhelper(super.dbhelper);
    }
}
