package katherinechen.squigglers.com.tasksplitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//base activity class that defines common behaviors for all activities
public class BaseActivity extends Activity {
    SessionManager session;
    DbHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        dbhelper = new DbHelper(this);
    }

    //inflates different menus based on whether user is logged in or logged out
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflates loggedinMenu if user is logged in
        if (session.isLoggedIn())
            getMenuInflater().inflate(R.menu.loggedinmenu, menu);

            //inflates loggedoutMenu if user is logged out
        else
            getMenuInflater().inflate(R.menu.loggedoutmenu, menu);

        setMenuBackground();
        return true;
    }

    protected void setMenuBackground(){
        // Log.d(TAG, "Enterting setMenuBackGround");
        getLayoutInflater().setFactory( new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
                    try { // Ask our inflater to create the view
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView( name, null, attrs );
                        /* The background gets refreshed each time a new item is added the options menu.
                        * So each time Android applies the default background we need to set our own
                        * background. This is done using a thread giving the background change as runnable
                        * object */
                        new Handler().post( new Runnable() {
                            public void run () {
                                // sets the background color
                                view.setBackgroundResource( R.color.grey);
                                // sets the text color
                                ((TextView) view).setTextColor(Color.BLACK);
                                // sets the text size
                                ((TextView) view).setTextSize(18);
                            }
                        } );
                        return view;
                    }
                    catch ( InflateException e ) {}
                    catch ( ClassNotFoundException e ) {}
                }
                return null;
            }});
    }

    //opens a new activity when a menu option is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = true;
        int id = item.getItemId();

        if (id == R.id.menu_create_group)
            onClickMenuCreateGroup(item);
        else if (id == R.id.menu_join_group)
            onClickMenuJoinGroup(item);
        else if (id == R.id.menu_sign_in)
            onClickMenuSignIn(item);
        else if (id == R.id.menu_register)
            onClickMenuRegister(item);
        else if (id == R.id.menu_sign_out)
            onClickMenuSignOut(item);
        else if (id == R.id.menu_my_groups)
            onClickMenuMyGroups(item);
        else if (id == R.id.menu_create_task)
            onClickMenuCreateTask(item);
        else if (id == R.id.menu_view_user_tasks) {
            onClickMenuViewUserTasks(item);
            session.setViewOther(false);
        }
        else
            handled = super.onOptionsItemSelected(item);

        return handled;
    }

    private void onClickMenuViewUserTasks(MenuItem item) {
        Intent intent = new Intent(this, UserTasksActivity.class);
        startActivity(intent);
    }

    private void onClickMenuCreateGroup(MenuItem item) {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }

    private void onClickMenuJoinGroup(MenuItem item) {
        Intent intent = new Intent(this, JoinGroupActivity.class);
        startActivity(intent);
    }

    private void onClickMenuSignIn(MenuItem item) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void onClickMenuRegister(MenuItem item) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void onClickMenuMyGroups(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void onClickMenuCreateTask(MenuItem item) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        startActivity(intent);
    }

    private void onClickMenuSignOut(MenuItem item) {
        session.logoutUser();
    }
}
