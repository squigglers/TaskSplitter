package katherinechen.squigglers.com.tasksplitter;

import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
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

    //inflates a menu that includes archived tasks
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groupmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = true;
        int id = item.getItemId();

        if (id == R.id.menu_archived_tasks)
            onClickMenuArchivedTasks(item);
        else
            handled = super.onOptionsItemSelected(item);

        return handled;
    }

    //open archived tasks
    private void onClickMenuArchivedTasks(MenuItem item) {
        Intent intent = new Intent(this, UserTasksActivity.class);
        String groupName = dbhelper.getGroupName(chosenGroupId);
        PageTransitions.archivedTasks(chosenGroupId, groupName, chosenUserId, this);
    }

    //open new user tasks activity with the group selected and the current user's tasks
    private void selectUser() {
        //close the drawer
        super.drawerLayout.closeDrawer(rightDrawerList);

        //go to group page
        String groupName = dbhelper.getGroupName(chosenGroupId);
        PageTransitions.goToGroupPage(chosenGroupId, groupName, chosenUserId, this);
    }
}
