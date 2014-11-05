package katherinechen.squigglers.com.tasksplitter;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTaskFragment extends Fragment {
    private SessionManager session;
    private DbHelper dbhelper;
    private View view;

    //for saving info into the bundle onsavedinstancestate
    private final String GROUPPOSITION = "groupPosition";
    private final String USERPOSITION = "userPosition";
    private int groupPosition;
    private int userPosition;

    private Button createTaskButton;
    private Spinner groupSpinner;
    private Spinner userSpinner;
    private TextView groupTextView;
    private TextView assignToTextView;
    boolean groupEmpty; //keeps track if group spinner is empty or not

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_task, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //assign spinners and textviews
        groupSpinner = (Spinner) view.findViewById(R.id.groupSpinner);
        userSpinner = (Spinner) view.findViewById(R.id.assignToSpinner);
        groupTextView = (TextView) view.findViewById(R.id.groupTextView);
        assignToTextView = (TextView) view.findViewById(R.id.assignToTextView);

        //initially set userSpinner to unclickable and assignToTextView to grey
        userSpinner.setClickable(false);
        assignToTextView.setTextColor(Color.GRAY);

        //get old spinner positions
        if (savedInstanceState != null) {
            groupPosition = savedInstanceState.getInt(GROUPPOSITION, -1);
            userPosition = savedInstanceState.getInt(USERPOSITION, -1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setGroupSpinner();  //fills both groups and [users in group] drop down menus

        //"Create Task" button listener
        createTaskButton = (Button) view.findViewById(R.id.create_task_button);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTaskProcess();
            }
        });

        //MAYBE IMPLEMENT AN UNLISTED USER ONE DAY
        //POSSIBLY ADD DEADLINE HERE ONE DAY
    }

    //adds task to the database
    private void createTaskProcess() {
        //get input fields:
        //taskname and task description
        EditText taskName = (EditText) view.findViewById(R.id.taskName);
        String taskname = taskName.getText().toString().trim();
        EditText taskDescription = (EditText) view.findViewById(R.id.taskDescription);
        String taskdescription = taskDescription.getText().toString().trim();
        //groupId and userId to assign to
        int groupId = -1, userId = -1;
        if (groupEmpty == false) {   //only get info if group is not empty
            Cursor GroupSelection = (Cursor) groupSpinner.getSelectedItem();
            groupId = GroupSelection.getInt(GroupSelection.getColumnIndexOrThrow(DbHelper.Groupy._ID));

            Cursor UserSelection = (Cursor) userSpinner.getSelectedItem();
            userId = UserSelection.getInt(UserSelection.getColumnIndexOrThrow(DbHelper.User._ID));
        }

        //check that all input fields are correct
        boolean correctInput = inputCheck(taskname, taskdescription, groupId, userId);

        //if correctInput = true
        //insert task into database
        if (correctInput) {
            createTask(taskname, taskdescription, groupId, userId);

            //toast to show task is created
            Toast.makeText(getActivity(), getString(R.string.task_created), Toast.LENGTH_LONG).show();

            //SEND USER TO DIFFERENT PAGE IDK
        }
    }

    //insert task info into Task table
    private void createTask(String taskname, String taskdescription, int groupId, int toUserId) {
        dbhelper.addTask(groupId, session.getUserId(), toUserId, taskname, taskdescription, null, 0);
    }


    private boolean inputCheck(String taskname, String taskdescription, int groupId, int userId) {
        boolean correctInput = true;
        String error = "";

        //if taskname is empty
        if (taskname.equals("")) {
            correctInput = false;
            error = getString(R.string.taskname_empty);
        }

        //if user is not in any groups
        else if (groupEmpty) {
            correctInput = false;
            error = getString(R.string.not_in_any_groups);
        }

        //show error toast if any incorrect input
        if (!correctInput)
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

        return correctInput;
    }

    //fills the group drop down menu with the user's groups
    private void setGroupSpinner() {
        //get cursor storing groupId, groupname
        Cursor cursor = dbhelper.getUserGroupsCursor(session.getUserId());

        //user is not in a group - set spinner to unclickable (cursor size is 0)
        if (cursor.getCount() == 0) {
            groupSpinner.setClickable(false);
            groupTextView.setTextColor(Color.GRAY);
            groupEmpty = true;

            groupPosition = -1;
            userPosition = -1;
        } else {
            //make sure group spinner is clickable
            groupSpinner.setClickable(true);
            groupTextView.setTextColor(Color.BLACK);
            groupEmpty = false;

            //create an array to specify which fields we want to display
            String[] from = new String[]{DbHelper.Groupy.NAME};
            //create an array of the display item we want to bind our data to
            int[] to = new int[]{android.R.id.text1};

            //create simple cursor adapter
            SimpleCursorAdapter adapter =
                    new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, cursor, from, to, 0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //set spinner to adapter
            groupSpinner.setAdapter(adapter);

            //set original position of group spinner
            if (groupPosition != -1 && !groupEmpty)
                groupSpinner.setSelection(groupPosition);
            else
                groupPosition = 0;
        }

        //get group selected in order to fill other spinner with users in the group selected
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getItemAtPosition(position);
                int selectedGroupId = c.getInt(c.getColumnIndexOrThrow(DbHelper.Groupy._ID));

                //group/user positions
                int originalGroupPosition = groupPosition;
                groupPosition = parent.getSelectedItemPosition();   //save position in group drop down menu
                if (groupPosition != originalGroupPosition)
                    userPosition = 0;

                //fill user drop down menu
                setUsersSpinner(selectedGroupId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //fills the users drop down menu with the users in the group selected
    private void setUsersSpinner(int groupId) {
        userSpinner.setClickable(true);    //set userSpinner to clickable now that a group is selected
        assignToTextView.setTextColor(Color.BLACK);  //set text back to black

        //get cursor storing userId, name
        Cursor cursor = dbhelper.getUsersInGroup(groupId);

        //create an array to specify which fields we want to display
        String[] from = new String[]{DbHelper.User.NAME};
        //create an array of the display item we want to bind our data to
        int[] to = new int[]{android.R.id.text1};

        //create simple cursor adapter
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, cursor, from, to, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set spinner to adapter
        userSpinner.setAdapter(adapter);

        userSpinner.setSelection(userPosition); //set user position in spinner

        //saves position of the user drop down menu
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userPosition = parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(GROUPPOSITION, groupPosition);
        outState.putInt(USERPOSITION, userPosition);
    }

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }
}
