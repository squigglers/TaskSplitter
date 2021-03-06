package katherinechen.squigglers.com.tasksplitter;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserTasksFragment extends ListFragment {
    private SessionManager session;
    private DbHelper dbhelper;
    private Cursor cursor;
    TasksCursorAdapter taskAdapter;

    private int userId;
    private int groupId;
    private int taskId;
    private boolean archived;

    @Override
    public void onResume() {
        super.onResume();

        refreshList();
    }

    //refresh list
    private void refreshList() {
        //cursor filled with taskId, task name, task description, assignerId
        //get archived tasks if archived
        if(archived)
            cursor = dbhelper.getArchivedTasksInGroup(groupId);

        //else get non-archived tasks
        else
            cursor = dbhelper.getUserTasksInGroup(userId, groupId);

        //which fields we want to display
        String[] from = new String[]{DbHelper.Task.TASK_NAME, DbHelper.Task.DESCRIPTION, DbHelper.User.NAME};
        //display item we want to bind our data to
        int[] to = new int[]{R.id.list_task_name, R.id.list_task_description, R.id.list_assigner};

        //create simple cursor adapter
        taskAdapter = new TasksCursorAdapter(getActivity(), R.layout.list_tasks, cursor, from, to, 0);
        setListAdapter(taskAdapter);

        //disable seeing the scroll bar in the listview
        getListView().setVerticalScrollBarEnabled(false);

        //long click brings up menu with options (reassign, archive)
        if(userId == session.getUserId())
            registerForContextMenu(getListView());

        //if no tasks make toast
        if(cursor.getCount() == 0)
            Toast.makeText(getActivity(), getString(R.string.no_user_tasks), Toast.LENGTH_LONG).show();
    }

    //<editor-fold desc="long press menu">
    //long press inflates floating context menu with options (reassign, archive)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //get taskId of clicked on thing
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Cursor c = (Cursor) getListAdapter().getItem(info.position);
        taskId = c.getInt(c.getColumnIndexOrThrow(DbHelper.Task._ID));

        //inflate menu
        MenuInflater inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.taskmenu, menu);
    }

    //context menu options
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();

        if(id == R.id.menu_reassign_task)
            reassignTask();
        else if(id == R.id.menu_archive_task)
            archiveTask();

        return super.onContextItemSelected(item);
    }

    //reassigntask to someone else
    private void reassignTask() {
        //start reassignactivity to reassign task
        Intent intent = new Intent(getActivity(), ReassignActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PageTransitions.TASKID, taskId);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    //archive task
    private void archiveTask() {
        dbhelper.archiveTask(taskId);
        Toast.makeText(getActivity(), getString(R.string.task_archived), Toast.LENGTH_LONG).show();

        refreshList();
    }
    //</editor-fold>

    //expands the task to be able to see the description when clicked on
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //get textviews to show
        TextView description = (TextView) v.findViewById(R.id.list_task_description);
        TextView assigner = (TextView) v.findViewById(R.id.list_assigner);
        TextView assignedBy = (TextView) v.findViewById(R.id.list_assigned_by_text);
        ArrayList<TextView> textviews = new ArrayList<TextView>();    //store TextViews in array to be lazy
        textviews.add(description);
        textviews.add(assigner);
        textviews.add(assignedBy);

        if (assignedBy.getVisibility() == View.VISIBLE) {
            for (TextView a : textviews)
                a.setVisibility(View.GONE);
        } else {
            for(TextView a : textviews) {
                if(!a.getText().toString().equals(""))
                    a.setVisibility(View.VISIBLE);
            }
        }
    }

    //<editor-fold desc="communication between activity and fragment">
    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

    //set userId and groupId for which we want to get tasks
    public void setIDs(int groupId, int userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    //</editor-fold>

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
            textviews.add(assigner);
            textviews.add(assignedBy);

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

        @Override
        public int getViewTypeCount() {
            if(getCount() < 1)
                return super.getViewTypeCount();
            else
                return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
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