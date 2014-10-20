package katherinechen.squigglers.com.tasksplitter;

import android.os.Bundle;

public class LoggedInBaseActivity extends BaseActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = super.session;

        //check to make sure user is logged in
        session.checkLogin();
    }
}
