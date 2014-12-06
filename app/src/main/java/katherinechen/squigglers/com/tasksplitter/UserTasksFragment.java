package katherinechen.squigglers.com.tasksplitter;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserTasksFragment extends ListFragment {
    private SessionManager session;
    private DbHelper dbhelper;
    private Cursor cursor;

    private int userId;
    private int groupId;

    @Override
    public void onResume() {
        super.onResume();

        //set userId and groupId for which we want to get tasks
        userId = session.getUserId();
        //groupId = 1;

        //get cursor filled with taskId, task name, task description, assignerId
        cursor = dbhelper.getUserTasksInGroup(userId, groupId);

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

        //get textviews to show
        TextView description = (TextView) v.findViewById(R.id.list_task_description);
        TextView assigner = (TextView) v.findViewById(R.id.list_assigner);
        TextView assignedBy = (TextView) v.findViewById(R.id.list_assigned_by_text);
        final ArrayList<TextView> textviews = new ArrayList<TextView>();    //store TextViews in array to be lazy
        textviews.add(description);
        //textviews.add(assigner);
        //textviews.add(assignedBy);

        if (description.getVisibility() == View.VISIBLE) {
            for (TextView a : textviews)
                a.setVisibility(View.GONE);
        } else {
            if (!description.getText().toString().equals("")) {   //only expand if not empty description
                for (TextView a : textviews)
                    a.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

    //custom cursor adapter for the tasks
    private class TasksCursorAdapter extends SimpleCursorAdapter {

        public TasksCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            final int pos = position;

            //get textviews from the layout and store in array
            final TextView taskname = (TextView) view.findViewById(R.id.list_task_name);
            final TextView taskdescription = (TextView) view.findViewById(R.id.list_task_description);
            final TextView assigner = (TextView) view.findViewById(R.id.list_assigner);
            final TextView assignedBy = (TextView) view.findViewById(R.id.list_assigned_by_text);
            final ArrayList<TextView> textviews = new ArrayList<TextView>();    //store TextViews in array to be lazy
            textviews.add(taskname);
            textviews.add(taskdescription);
            //textviews.add(assigner);
            //textviews.add(assignedBy);

            //disable checkbox if not user
            CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkBox);
            if (userId != session.getUserId())
                checkbox.setVisibility(View.GONE);

            //if task is completed, strike line through it and checkmark the box
            cursor.moveToPosition(pos);
            if (cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.Task.COMPLETED)) == 1) {
                strikeTask(textviews);
                checkbox.setChecked(true);
            }

            //attach listener to checkbox
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int completed;  //is 1 when checkmarked (completed), 0 when not checkmarked (not completed)
                    //strike line through task when checkmarked
                    if (isChecked) {
                        strikeTask(textviews);
                        completed = 1;
                    } else {
                        noStrikeTask(textviews);
                        completed = 0;
                    }

                    //update database with completed task
                    cursor.moveToPosition(pos);
                    int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.Task._ID));
                    dbhelper.updateTaskCompletion(taskId, completed);
                }
            });

            return view;
        }

        //strikes a line through the task
        private void strikeTask(ArrayList<TextView> textviews) {
            for (TextView a : textviews)
                a.setPaintFlags(a.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        //removes line through the task
        private void noStrikeTask(ArrayList<TextView> textviews) {
            for (TextView a : textviews)
                a.setPaintFlags(0);
        }
    }
}