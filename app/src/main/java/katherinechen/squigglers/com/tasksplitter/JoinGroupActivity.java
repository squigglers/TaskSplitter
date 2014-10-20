package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class JoinGroupActivity extends LoggedInBaseActivity implements SessionInterface {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        session = super.session;
        setFragmentSession();
    }

    @Override
    public void setFragmentSession() {
        FragmentManager fm = getFragmentManager();
        JoinGroupFragment fragment = (JoinGroupFragment) fm.findFragmentById(R.id.join_group_fragment);
        fragment.setSession(session);
    }
}
