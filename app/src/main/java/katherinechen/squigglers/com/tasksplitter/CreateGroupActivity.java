package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class CreateGroupActivity extends LoggedInBaseActivity implements SessionInterface {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        session = super.session;
        setFragmentSession();
    }

    @Override
    public void setFragmentSession() {
        FragmentManager fm = getFragmentManager();
        CreateGroupFragment fragment = (CreateGroupFragment) fm.findFragmentById(R.id.create_group_fragment);
        fragment.setSession(session);
    }
}
