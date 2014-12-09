package katherinechen.squigglers.com.tasksplitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

//keeps user logged in to the application
public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "TaskSplitterPref";
    private static final String IS_LOGGEDIN = "IsLoggedIn";
    private static final String VIEW_OTHER = "ViewOther";
    private static final String KEY_USERID = "userID";
    private static final String USER_TO_VIEW_ID = "UserToViewId";

    int userToViewId;
    boolean viewOther;

    private static final Class LOGOUTACTIVITY = StartPageActivity.class; //activity that user redirects to when logging out
    private static final Class LOGINACTIVITY = MainActivity.class;  //activity that user redirects to when logging in

    //constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //sign user in and store userID and redirect user to LOGINACTIVITY
    public void createLoginSession(int userID) {

        editor.putBoolean(IS_LOGGEDIN, true);
        editor.putInt(KEY_USERID, userID);
        editor.commit();

        redirectActivity(LOGINACTIVITY);
    }

    //call this in every logged in activity
    //check if user is signed in, otherwise redirect user to LOGOUTACTIVITY
    public void checkLogin() {
        if (!this.isLoggedIn()) {
            redirectActivity(LOGOUTACTIVITY);

            //toast to show user that they are not logged in
            //Toast.makeText(context, context.getString(R.string.not_logged_in_toast), Toast.LENGTH_LONG).show();
        }
    }

    //log user out by clearing all session details and redirect user to LOGOUTACTIVITY
    public void logoutUser() {
        editor.clear();
        editor.commit();

        redirectActivity(LOGOUTACTIVITY);

        //toast to show that the user has been signed out
        Toast.makeText(context, context.getString(R.string.sign_out_toast), Toast.LENGTH_LONG).show();
    }

    //call this to redirect to new activity while clearing the backstack
    private void redirectActivity(Class redirectClass) {
        //redirect user to Sign In Activity
        Intent i = new Intent(context, redirectClass);

        //flag to clear activity backstack
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Sign In Activity
        context.startActivity(i);
    }

    //return login state
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGEDIN, false);
    }

    //return userId
    public int getUserId() {
        int userID = pref.getInt(KEY_USERID, -1);
        return userID;
    }

    public boolean getViewOther() {
        return pref.getBoolean(VIEW_OTHER, false);
        //return viewOther;
    }

    public void setViewOther(boolean view) {
        editor.putBoolean(VIEW_OTHER, view);
        editor.commit();
        //viewOther = view;
    }

    public int getUserToViewId() {
        return pref.getInt(USER_TO_VIEW_ID, -1);
        //return userToViewId;
    }

    public void setUserToViewId(int id) {
        editor.putInt(USER_TO_VIEW_ID, id);
        editor.commit();
        userToViewId = id;
    }
}