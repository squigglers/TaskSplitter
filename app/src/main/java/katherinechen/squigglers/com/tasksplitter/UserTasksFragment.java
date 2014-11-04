package katherinechen.squigglers.com.tasksplitter;

import android.app.ListFragment;
import android.database.Cursor;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class UserTasksFragment extends ListFragment {
    private SessionManager session;
    private DbHelper dbhelper;

    private int userId;
    private int groupId;

    @Override
    public void onResume() {
        super.onResume();

        //set userId and groupId for which we want to get tasks
        userId = session.getUserId();
        groupId = 1;

        //get cursor filled with taskId, task name, task description, assignerId
        Cursor cursor = dbhelper.getUserTasksInGroup(userId, groupId);

        //which fields we want to display
        String[] from = new String[]{DbHelper.Task.TASK_NAME, DbHelper.Task.DESCRIPTION};
        //display item we want to bind our data to
        int[] to = new int[]{R.id.list_task_name, R.id.list_task_description};

        //create simple cursor adapter
        TasksCursorAdapter taskAdapter =
                new TasksCursorAdapter(getActivity(), R.layout.list_tasks, cursor, from, to, 0);
        setListAdapter(taskAdapter);

        //disable seeing the scroll bar in the listview
        getListView().setVerticalScrollBarEnabled(false);
    }

    //expands the task to be able to see the description when clicked on
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        TextView description = (TextView) v.findViewById(R.id.list_task_description);
        if (description.getVisibility() == View.VISIBLE)
            description.setVisibility(View.GONE);
        else {
            if (!description.getText().toString().equals(""))    //only expand if not empty description
                description.setVisibility(View.VISIBLE);
        }
    }

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

}