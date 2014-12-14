package katherinechen.squigglers.com.tasksplitter;

import android.app.ListFragment;
import android.database.Cursor;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ReassignFragment extends ListFragment {

    private SessionManager session;
    private DbHelper dbhelper;
    private Cursor cursor;
    private Cursor groupCursor;

    private int taskId;
    private int groupId;
    private int userId;

    @Override
    public void onResume() {
        super.onResume();

        //get groupId from taskid
        cursor = dbhelper.getTaskInfo(taskId);
        cursor.moveToFirst();
        groupId = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.Task.GROUP_ID));

        //get cursor filled with all people in the group
        groupCursor = dbhelper.getUsersInGroup(groupId);

        //set groupCursor to the listfragment
        //which fields we want to display
        String[] from = new String[]{DbHelper.User.NAME};
        //display item we want to bind our data to
        int[] to = new int[]{android.R.id.text1};;
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, groupCursor, from, to, 0);
        setListAdapter(adapter);
    }

    //when clicked on, update the task with the new userid
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //get the user id clicked on
        groupCursor.moveToPosition(position);
        userId = groupCursor.getInt(groupCursor.getColumnIndexOrThrow(DbHelper.User._ID));

        //update the task with the new user
        dbhelper.changeTaskUser(taskId, userId);

        getActivity().finish();
    }

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

    public void setIDs(int taskId) {
        this.taskId = taskId;
    }
}
