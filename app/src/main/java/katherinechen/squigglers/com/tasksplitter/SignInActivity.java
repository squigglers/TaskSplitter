package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class SignInActivity extends BaseActivity implements SessionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setFragmentInfo();
    }

    @Override
    public void setFragmentInfo() {
        FragmentManager fm = getFragmentManager();
        SignInFragment fragment = (SignInFragment) fm.findFragmentById(R.id.sign_in_fragment);

        fragment.setSession(session);
        fragment.setDbhelper(dbhelper);
    }
}
