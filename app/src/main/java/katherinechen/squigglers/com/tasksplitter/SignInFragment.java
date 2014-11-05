package katherinechen.squigglers.com.tasksplitter;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInFragment extends Fragment {
    private SessionManager session;
    private DbHelper dbhelper;
    private Button signInButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        //"Sign In" button listener
        signInButton = (Button) view.findViewById(R.id.sign_in_button);
        signInButtonClick(view);

        return view;
    }

    //"Sign In" button listener
    private void signInButtonClick(final View view) {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInProcess(view); //do this when "Sign In" button is clicked
            }
        });
    }

    private void signInProcess(View view) {
        //get input fields (username, password)
        EditText usernameText = (EditText) view.findViewById(R.id.sign_in_username_field);
        String username = usernameText.getText().toString().trim().toLowerCase();
        EditText passwordText = (EditText) view.findViewById(R.id.sign_in_password_field);
        String password = passwordText.getText().toString();

        //check that all input fields are correct
        boolean correctInput = inputCheck(username, password);

        //validate user login
        int userId = -1;
        if (correctInput)
            userId = validateUserLogin(username, password);

        //if user login is validated, login user
        if (userId != -1) {
            session.createLoginSession(userId);

            //toast to show user is logged in
            Toast.makeText(getActivity(), getString(R.string.sign_in_successful_toast), Toast.LENGTH_LONG).show();

            //SHOULD GO TO HOME PAGE AFTER THIS
        }
    }

    //check that all input fields are correct
    //returns true if correct, false if otherwise and shows error message toast
    private boolean inputCheck(String username, String password) {
        boolean correctInput = true;
        String error = "";

        //if any fields are empty
        if (username.equals("") || password.equals("")) {
            correctInput = false;
            error = getString(R.string.empty_input_fields);
        }

        //show error toast if any incorrect input
        if (!correctInput)
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

        return correctInput;
    }

    //validate user login; return userId if validated, else return -1
    private int validateUserLogin(String username, String password) {
        //HASH PASSWORD

        int userId = dbhelper.validatedUserLogin(username, password);

        //show error toast if user login not validated
        if (userId == -1)
            Toast.makeText(getActivity(), R.string.incorrect_user_login, Toast.LENGTH_LONG).show();

        return userId;
    }

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }
}
