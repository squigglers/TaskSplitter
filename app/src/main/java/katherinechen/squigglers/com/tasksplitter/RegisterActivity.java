package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class RegisterActivity extends BaseActivity implements SessionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setFragmentInfo();
    }

    @Override
    public void setFragmentInfo() {
        FragmentManager fm = getFragmentManager();
        RegisterFragment fragment = (RegisterFragment) fm.findFragmentById(R.id.register_fragment);
        fragment.setSession(session);
        fragment.setDbhelper(dbhelper);
    }
}
