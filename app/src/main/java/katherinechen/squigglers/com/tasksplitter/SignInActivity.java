package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class SignInActivity extends BaseActivity implements SessionInterface {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        session = super.session;
        setFragmentSession();
    }

    @Override
    public void setFragmentSession() {
        FragmentManager fm = getFragmentManager();
        SignInFragment fragment = (SignInFragment) fm.findFragmentById(R.id.sign_in_fragment);
        fragment.setSession(session);
    }
}
