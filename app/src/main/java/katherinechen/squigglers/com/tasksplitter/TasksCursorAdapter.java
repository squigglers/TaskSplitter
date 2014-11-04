package katherinechen.squigglers.com.tasksplitter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TasksCursorAdapter extends SimpleCursorAdapter {
    public TasksCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);

        //attach listener to checkbox
        CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkBox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView taskname = (TextView) view.findViewById(R.id.list_task_name);
                TextView taskdescription = (TextView) view.findViewById(R.id.list_task_description);

                if (isChecked) {
                    taskname.setPaintFlags(taskname.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    taskdescription.setPaintFlags(taskname.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    taskname.setPaintFlags(0);
                    taskdescription.setPaintFlags(0);
                }
            }
        });

        return view;
    }
}
