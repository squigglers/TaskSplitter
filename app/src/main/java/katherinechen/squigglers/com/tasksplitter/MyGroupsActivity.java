package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;


public class MyGroupsActivity extends LoggedInBaseActivity implements SessionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        MyGroupsFragment list = new MyGroupsFragment();

        //getActivity().getSupportFragmentManager().
        //        beginTransaction().add(android.R.id.content,list).commit();        session = super.session;
        setFragmentInfo();
    }

    @Override
    public void setFragmentInfo() {
        FragmentManager fm = getFragmentManager();
        MyGroupsFragment fragment = (MyGroupsFragment) fm.findFragmentById(R.id.my_groups_fragment);
        /*MyGroupsFragment fragment = new MyGroupsFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.commit();*/
        fragment.setSession(super.session);
        fragment.setDbhelper(super.dbhelper);
    }

    public void onFragmentInteraction(String id) {
        Log.i("Info ", id);
    }
}
