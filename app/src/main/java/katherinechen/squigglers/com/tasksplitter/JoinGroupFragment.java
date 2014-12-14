package katherinechen.squigglers.com.tasksplitter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinGroupFragment extends Fragment {
    private SessionManager session;
    private DbHelper dbhelper;
    private Button joinGroupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_group, container, false);

        //"Join Group" button listener
        joinGroupButton = (Button) view.findViewById(R.id.join_group_button);
        joinGroupButtonClick(view);

        return view;
    }

    //"Join Group" button listener
    private void joinGroupButtonClick(final View view) {
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroupProcess(view); //do this when "Join Group" button is clicked
            }
        });
    }

    //checks input fields and joins user to group in database
    private void joinGroupProcess(View view) {
        //get input fields (groupName and accessCode)
        EditText groupNameText = (EditText) view.findViewById(R.id.join_group_name_field);
        String groupName = groupNameText.getText().toString().trim().toLowerCase();
        EditText accessCodeText = (EditText) view.findViewById(R.id.join_group_access_code_field);
        String accessCode = accessCodeText.getText().toString();

        //check that all input fields are correct
        boolean correctInput = inputCheck(groupName, accessCode);

        //validate group login
        int groupId = -1;
        if (correctInput)
            groupId = validateGroupLogin(groupName, accessCode);

        //if group login is validated, insert user to group in database
        if (groupId != -1) {
            insertUserToGroup(groupId);

            //toast to show group is joined
            Toast.makeText(getActivity(), getString(R.string.group_joined_toast), Toast.LENGTH_LONG).show();

            //go to group page after this
            PageTransitions.goToGroupPage(groupId, groupName, session.getUserId(), getActivity());
        }
    }

    //check that all input fields are correct
    //returns true if correct, false if otherwise and shows error message toast
    private boolean inputCheck(String groupName, String accessCode) {
        boolean correctInput = true;
        String error = "";

        //if any fields are empty
        if (groupName.equals("") || accessCode.equals("")) {
            correctInput = false;
            error = getString(R.string.empty_input_fields);
        }

        //show error toast if any incorrect input
        if (!correctInput)
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

        return correctInput;
    }

    //validate group login; return groupId if validated, else return -1
    private int validateGroupLogin(String groupName, String accessCode) {
        //HASH ACCESS CODE

        int groupId = dbhelper.validateGroupLogin(groupName, accessCode);

        //show error toast if group login not validated
        if (groupId == -1)
            Toast.makeText(getActivity(), R.string.incorrect_group_login, Toast.LENGTH_LONG).show();

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

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }
}
