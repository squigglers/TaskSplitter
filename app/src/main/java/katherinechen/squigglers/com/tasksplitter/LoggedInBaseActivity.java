package katherinechen.squigglers.com.tasksplitter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LoggedInBaseActivity extends BaseActivity {

    private Cursor leftCursor;
    protected DrawerLayout drawerLayout;
    private ListView leftDrawerList;
    private int chosenGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check to make sure user is logged in
        session.checkLogin();
    }

    @Override
    public void onResume() {
        super.onResume();

        //get drawerlayout id
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        //set the list's click listener to get the groupId clicked on
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            leftCursor.moveToPosition(position);
            chosenGroupId = leftCursor.getInt(leftCursor.getColumnIndexOrThrow(DbHelper.Groupy._ID));
            String groupName = leftCursor.getString(leftCursor.getColumnIndexOrThrow(DbHelper.Groupy.NAME));

            selectGroup(groupName);
            }
        });
    }


    //open new user tasks activity with the group selected and the current user's tasks
    private void selectGroup(String groupName) {
        //close the drawer
        drawerLayout.closeDrawer(leftDrawerList);

        //go to group page
        PageTransitions.goToGroupPage(chosenGroupId, groupName, this, session);
    }
}
