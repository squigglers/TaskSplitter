package katherinechen.squigglers.com.tasksplitter;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MyGroupsActivity extends BaseActivity implements SessionInterface,
        MyGroupsFragment.OnFragmentInteractionListener {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        session = super.session;
        setFragmentSession();
    }

    @Override
    public void setFragmentSession() {
        FragmentManager fm = getFragmentManager();
        MyGroupsFragment fragment = (MyGroupsFragment) fm.findFragmentById(R.id.my_groups_fragment);
        fragment.setSession(session);
    }

    public void onFragmentInteraction(String id) {
        Log.i("Info ", id);
    }
}
