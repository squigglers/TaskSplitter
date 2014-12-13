package katherinechen.squigglers.com.tasksplitter;

import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class RightDrawerBaseActivity extends LoggedInBaseActivity {

    private Cursor rightCursor;
    private ListView rightDrawerList;

    private int chosenGroupId;
    private int chosenUserId;

    @Override
    public void onResume() {
        super.onResume();

        //------RIGHT navigation drawer to show users in the group------
        rightDrawerList = (ListView) findViewById(R.id.right_drawer);

        //get cursor filled with userid and name
        chosenGroupId = getIntent().getExtras().getInt(PageTransitions.GROUPID);
        rightCursor = dbhelper.getUsersInGroup(chosenGroupId);

        //which fields we want to display
        String[] rightFrom = new String[]{DbHelper.User.NAME};
        //display item we want to bind our data to
        int[] rightTo = new int[]{android.R.id.text1};

        //create simple cursor adapter and set to right drawer
        SimpleCursorAdapter rightDrawerAdapter =
                new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, rightCursor, rightFrom, rightTo, 0);
        rightDrawerList.setAdapter(rightDrawerAdapter);

        //set the list's click listener to get the userId clicked on
        rightDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rightCursor.moveToPosition(position);
                chosenUserId = rightCursor.getInt(rightCursor.getColumnIndexOrThrow(DbHelper.User._ID));

                selectUser();
            }
        });
    }

    //open new user tasks activity with the group selected and the current user's tasks
    private void selectUser() {
        //close the drawer
        super.drawerLayout.closeDrawer(rightDrawerList);

        //go to group page
        String groupName = dbhelper.getGroupName(chosenGroupId);
        PageTransitions.goToGroupPage(chosenGroupId, groupName, chosenUserId, this, session);
    }
}
