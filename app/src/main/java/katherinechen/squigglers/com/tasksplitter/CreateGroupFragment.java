package katherinechen.squigglers.com.tasksplitter;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.Random;

public class CreateGroupFragment extends Fragment {
    private SessionManager session;
    private DbHelper dbhelper;
    private Button createGroupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        //"Create Group" button listener
        createGroupButton = (Button) view.findViewById(R.id.create_group_button);
        createGroupButtonClick(view);

        return view;
    }

    //"Create Group" button listener
    private void createGroupButtonClick(final View view) {
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroupProcess(view); //do this when "Create Group" button is clicked
            }
        });
    }

    //checks input fields and registers group to database
    private void createGroupProcess(View view) {
        //get input fields (groupName)
        EditText groupNameText = (EditText) view.findViewById(R.id.create_group_name_field);
        String groupName = groupNameText.getText().toString().trim().toLowerCase();

        //check that all input fields are correct
        boolean correctInput = inputCheck(groupName);

        //if correctInput = true
        //insert group and generated access code to database and add user to group to database
        if (correctInput) {
            int groupId = createGroup(groupName);
            insertUserToGroup(groupId);

            //toast to show group is created
            Toast.makeText(getActivity(), getString(R.string.group_created_toast), Toast.LENGTH_LONG).show();

            //go to group page
            PageTransitions.goToGroupPage(groupId, groupName, session.getUserId(), getActivity());
        }
    }

    //check that all input fields are correct
    //returns true if correct, false if otherwise and shows error message toast
    private boolean inputCheck(String groupName) {
        boolean correctInput = true;
        String error = "";

        //if any fields are empty
        if (groupName.equals("")) {
            correctInput = false;
            error = getString(R.string.empty_input_fields);
        }

        //if group name is not alphanumeric characters
        else if (!groupName.matches(getString(R.string.groupname_regex))) {
            correctInput = false;
            error = getString(R.string.groupname_incorrect_input);
        }

        //show error toast if any incorrect input
        if (!correctInput)
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

        return correctInput;
    }

    //insert group and generated access code to group table
    //returns groupId of newly created group
    private int createGroup(String groupName) {
        //create access code (random 10 chars) as a group "password"
        String accessCode = createAccessCode();
        Log.i("ACCESS CODE", accessCode);

        //MAYBE HASH ACCESSCODE?

        //add groupName and accessCode to group table in database
        int groupId = dbhelper.addGroup(groupName, accessCode);

        return groupId;
    }

    //insert user to group in userGroup table
    private void insertUserToGroup(int groupId) {
        //get current user's userid
        int userId = session.getUserId();

        //add user to group in userGroup table
        if (userId != -1)
            dbhelper.addUserGroup(groupId, userId);
    }

    //create a unique random 10 length alphanumeric access code
    private String createAccessCode() {
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        int accessCodeLength = 10;
        String accessCode;

        do {
            Random random = new SecureRandom();
            accessCode = "";
            for (int i = 0; i < accessCodeLength; i++) {
                int index = (int) (random.nextDouble() * letters.length());
                accessCode += letters.substring(index, index + 1);
            }
        } while (checkAccessCodeExists(accessCode)); //if access code is already taken

        return accessCode;
    }

    //check if access code is already taken
    private boolean checkAccessCodeExists(String accessCode) {
        DbHelper dbhelper = new DbHelper(getActivity());
        return dbhelper.checkAccessCodeExists(accessCode);
    }

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }
}
