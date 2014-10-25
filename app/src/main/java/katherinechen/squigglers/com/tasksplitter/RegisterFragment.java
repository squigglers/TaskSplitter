package katherinechen.squigglers.com.tasksplitter;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {
    private SessionManager session;
    private DbHelper dbhelper;
    private Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //"Register" button listener
        registerButton = (Button) view.findViewById(R.id.register_button);
        registerButtonClick(view);

        return view;
    }

    //"Register" button listener
    private void registerButtonClick(final View view) {
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerProcess(view); //do this when "Register" button is clicked
            }
        });
    }

    private void registerProcess(View view) {
        //get input fields (name, username, password, confirmPassword)
        EditText nameText = (EditText) view.findViewById(R.id.register_name_field);
        String name = nameText.getText().toString().trim();
        EditText usernameText = (EditText) view.findViewById(R.id.register_username_field);
        String username = usernameText.getText().toString().trim().toLowerCase();
        EditText passwordText = (EditText) view.findViewById(R.id.register_password_field);
        String password = passwordText.getText().toString();
        EditText confirmPasswordText = (EditText) view.findViewById(R.id.register_confirm_password_field);
        String confirmPassword = confirmPasswordText.getText().toString();

        //check that all input fields are correct
        boolean correctInput = inputCheck(name, username, password, confirmPassword);

        //if correctInput = true
        //insert name, username, and password to database and login user
        if(correctInput) {
            int userId = createUser(name, username, password);
            session.createLoginSession(userId);

            //toast to show user is created
            Toast.makeText(getActivity(), getString(R.string.user_registration_completed_toast), Toast.LENGTH_LONG).show();

            //SEND USER TO HOME PAGE
        }

    }

    //check that all input fields are correct
    //returns true if correct, false if otherwise and shows error message toast
    private boolean inputCheck(String name, String username, String password, String confirmPassword) {
        Resources res = getResources();
        int minPasswordLength = res.getInteger(R.integer.min_length_password);
        int maxPasswordLength = res.getInteger(R.integer.max_length_password);
        int minUsernameLength = res.getInteger(R.integer.min_length_username);
        int maxUsernameLength = res.getInteger(R.integer.max_length_username);

        boolean correctInput = true;
        String error = "";

        //if any fields are empty
        if (name.equals("") || username.equals("") ||
            password.equals("") || confirmPassword.equals("")) {
            correctInput = false;
            error = getString(R.string.empty_input_fields);
        }

        //if name is not letters and spaces
        else if(!name.matches(getString(R.string.name_regex))) {
            correctInput = false;
            error = getString(R.string.name_incorrect_input);
        }

        //if username is too short
        else if(username.length() < minUsernameLength)
        {
            correctInput = false;
            error = String.format(getString(R.string.username_too_short),
                    minUsernameLength, maxUsernameLength);
        }

        //if username is not alphanumeric characters
        else if(!username.matches(getString(R.string.username_regex))) {
            correctInput = false;
            error = getString(R.string.username_incorrect_input);
        }

        //if username is already taken
        else if(checkUsernameExists(username))
        {
            correctInput = false;
            error = getString(R.string.username_taken);
        }

        //if passwords don't match
        else if(!passwordMatches(password, confirmPassword))
        {
            correctInput = false;
            error = getString(R.string.mismatched_passwords);
        }

        //if password is too short
        else if(password.length() < minPasswordLength)
        {
            correctInput = false;
            error = String.format(getString(R.string.password_too_short),
                    minPasswordLength, maxPasswordLength);
        }

        //show error toast if any incorrect input
        if(!correctInput)
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

        return correctInput;
    }

    //insert name, username, and password to user table
    //returns userId of newly created user
    private int createUser(String name, String username, String password) {
        //HASH PASSWORD

        //add name, username, and password to user table in database
        int userId = dbhelper.addUser(name, username, password);

        return userId;
    }

    //check if username is already taken
    private boolean checkUsernameExists(String username)
    {
        return dbhelper.checkUsernameExists(username);
    }

    //checks to make sure user entered the same input for password and confirm password
    private boolean passwordMatches(String password1, String password2) {
        if(password1.equals(password2))
            return true;
        else
            return false;
    }

    public void setSession(SessionManager session)
    {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }
}
