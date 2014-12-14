package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;

public class ReassignActivity extends LoggedInBaseActivity implements SessionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reassign);

        setFragmentInfo();
    }

    @Override
    public void setFragmentInfo() {
        //get taskid from bundle
        int taskId = getIntent().getExtras().getInt(PageTransitions.TASKID);

        FragmentManager fm = getFragmentManager();
        ReassignFragment fragment = (ReassignFragment) fm.findFragmentById(R.id.reassign_fragment);

        fragment.setSession(session);
        fragment.setDbhelper(dbhelper);
        fragment.setIDs(taskId);
    }
}