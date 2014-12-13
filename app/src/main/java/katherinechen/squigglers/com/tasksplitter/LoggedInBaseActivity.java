package katherinechen.squigglers.com.tasksplitter;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LoggedInBaseActivity extends BaseActivity {

    private Cursor leftCursor;
    private ListView leftDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check to make sure user is logged in
        session.checkLogin();
    }

    @Override
    public void onResume() {
        super.onResume();

        //------LEFT navigation drawer to show groups------
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);

        //get cursor filled with groupid and groupname
        leftCursor = dbhelper.getUserGroupsCursor(session.getUserId());

        //which fields we want to display
        String[] leftFrom = new String[]{DbHelper.Groupy.NAME};
        //display item we want to bind our data to
        int[] leftTo = new int[]{android.R.id.text1};

        //create simple cursor adapter and set to left drawer
        SimpleCursorAdapter leftDrawerAdapter =
                new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, leftCursor, leftFrom, leftTo, 0);
        leftDrawerList.setAdapter(leftDrawerAdapter);
    }
}
