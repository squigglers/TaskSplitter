package katherinechen.squigglers.com.tasksplitter;

import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class RightDrawerBaseActivity extends LoggedInBaseActivity {

    private Cursor rightCursor;
    private ListView rightDrawerList;

    @Override
    public void onResume() {
        super.onResume();

        //------RIGHT navigation drawer to show users in the group------
        rightDrawerList = (ListView) findViewById(R.id.right_drawer);

        //get cursor filled with userid and name
        int groupId = 2;
        rightCursor = dbhelper.getUsersInGroup(groupId);

        //which fields we want to display
        String[] rightFrom = new String[]{DbHelper.User.NAME};
        //display item we want to bind our data to
        int[] rightTo = new int[]{android.R.id.text1};

        //create simple cursor adapter and set to left drawer
        SimpleCursorAdapter rightDrawerAdapter =
                new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, rightCursor, rightFrom, rightTo, 0);
        rightDrawerList.setAdapter(rightDrawerAdapter);
    }
}
