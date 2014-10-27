package katherinechen.squigglers.com.tasksplitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//base activity class that defines common behaviors for all activities
public class BaseActivity extends Activity{
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
        if(session.isLoggedIn())
            getMenuInflater().inflate(R.menu.loggedinmenu, menu);

        //inflates loggedoutMenu if user is logged out
        else
            getMenuInflater().inflate(R.menu.loggedoutmenu, menu);
        return true;
    }

    //opens a new activity when a menu option is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = true;
        int id = item.getItemId();

        if(id == R.id.menu_create_group)
            onClickMenuCreateGroup(item);
        else if(id == R.id.menu_join_group)
            onClickMenuJoinGroup(item);
        else if(id == R.id.menu_sign_in)
            onClickMenuSignIn(item);
        else if(id == R.id.menu_register)
            onClickMenuRegister(item);
        else if(id == R.id.menu_sign_out)
            onClickMenuSignOut(item);
        else if(id == R.id.menu_my_groups)
            onClickMenuMyGroups(item);
        else
            handled = super.onOptionsItemSelected(item);

        return handled;
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

    private void onClickMenuSignOut(MenuItem item) {
        session.logoutUser();
    }
}
