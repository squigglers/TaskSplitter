package katherinechen.squigglers.com.tasksplitter;

import android.os.Bundle;

public class LoggedInBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check to make sure user is logged in
        session.checkLogin();
    }
}
