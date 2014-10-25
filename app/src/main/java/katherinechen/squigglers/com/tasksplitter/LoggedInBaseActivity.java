package katherinechen.squigglers.com.tasksplitter;

import android.os.Bundle;

public class LoggedInBaseActivity extends BaseActivity {
    SessionManager session;
    DbHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = super.session;
        dbhelper = super.dbhelper;

        //check to make sure user is logged in
        session.checkLogin();
    }
}
