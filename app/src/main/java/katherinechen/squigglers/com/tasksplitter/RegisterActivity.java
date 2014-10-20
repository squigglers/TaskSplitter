package katherinechen.squigglers.com.tasksplitter;

import android.app.FragmentManager;
import android.os.Bundle;


public class RegisterActivity extends BaseActivity implements SessionInterface {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        session = super.session;
        setFragmentSession();
    }

    @Override
    public void setFragmentSession() {
        FragmentManager fm = getFragmentManager();
        RegisterFragment fragment = (RegisterFragment) fm.findFragmentById(R.id.register_fragment);
        fragment.setSession(session);
    }
}
