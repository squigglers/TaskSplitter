package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class CreateGroupActivity extends LoggedInBaseActivity implements SessionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        setFragmentInfo();
    }

    @Override
    public void setFragmentInfo() {
        FragmentManager fm = getFragmentManager();
        CreateGroupFragment fragment = (CreateGroupFragment) fm.findFragmentById(R.id.create_group_fragment);

        fragment.setSession(super.session);
        fragment.setDbhelper(super.dbhelper);
    }
}
