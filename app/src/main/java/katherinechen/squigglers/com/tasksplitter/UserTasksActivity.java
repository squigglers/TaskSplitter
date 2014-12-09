package katherinechen.squigglers.com.tasksplitter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class UserTasksActivity extends LoggedInBaseActivity implements SessionInterface {
    public int userId = -1;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mGroupTitles;
    private int[] mGroupIds;

    public int chosenGroupId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tasks);

//        userId = getIntent().getExtras().getInt("userId");
        //if(userId == -1)
        //    userId = session.getUserId();

        session = new SessionManager(getApplicationContext());
        dbhelper = new DbHelper(this);

        setFragmentInfo();



        if(session.isLoggedIn()) {
            mTitle = mDrawerTitle = getTitle();
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            ArrayList<Group> groups = dbhelper.getUserGroups(session.getUserId());
            mGroupTitles = new String[groups.size()];
            for(int x=0; x<groups.size(); x++) {
                mGroupTitles[x] = groups.get(x).getGroupname();
            }

            mGroupIds = new int[groups.size()];
            for(int x=0; x<groups.size(); x++) {
                mGroupIds[x] = groups.get(x).getGroupId();
            }
            mDrawerList = (ListView) findViewById(R.id.left_drawer);


            mDrawerList.setAdapter(

                    new ArrayAdapter<String>(
                            this,
                            R.layout.drawer_list_item,
                            mGroupTitles));

            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);

            mDrawerToggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, R.drawable.ic_drawer,
                    R.string.drawer_open, R.string.drawer_close
            ) {
                public void onDrawerClosed(View view) {
                    getActionBar().setTitle(mTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                public void onDrawerOpened(View drawerView) {
                    getActionBar().setTitle(mDrawerTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            if(savedInstanceState == null) {
                //selectItem(0);
            }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
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
        //return super.onCreateOptionsMenu(menu);
    }

    //opens a new activity when a menu option is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = true;
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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
        Intent intent = new Intent(this, MyGroupsActivity.class);
        startActivity(intent);
    }

    private void onClickMenuSignOut(MenuItem item) {
        session.logoutUser();
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            selectItem(position);
        }
    }

    private class RightDrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            sendUser(position);
        }
    }

    private void sendUser(int position) {
        Cursor c = dbhelper.getUsersInGroup(chosenGroupId);

        ArrayList<User> users = new ArrayList<User>(c.getCount());
        if (c.moveToFirst()) {
            do {
                User user = new User(c.getInt(0), c.getString(1));
                users.add(user);
            } while (c.moveToNext());
        }

        int [] mUserIds = new int[users.size()];
        for(int x=0; x<users.size(); x++) {
            mUserIds[x] = users.get(x).getUserId();
        }

        session.setViewOther(true);
        session.setUserToViewId(mUserIds[position]);
        Log.e("userId", "" + mUserIds[position]);

        Intent userTasksActivity = new Intent(this, UserTasksActivity.class);
        //userTasksActivity.putExtra("userId", mUserIds[position]);
        startActivity(userTasksActivity);

    }

    private void selectItem(int position) {
        Fragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(GroupFragment.ARG_GROUP_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mGroupTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public class GroupFragment extends Fragment {
        public static final String ARG_GROUP_NUMBER = "group_number";

        SessionManager session;
        DbHelper dbhelper;

        private DrawerLayout mDrawerLayout;
        private ListView mDrawerList;
        private ActionBarDrawerToggle mDrawerToggle;

        private CharSequence mDrawerTitle;
        private CharSequence mTitle;
        private String[] mUserNames;
        private int[] mUserIds;



        public GroupFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            session = new SessionManager(getApplicationContext());
            dbhelper = new DbHelper(getApplicationContext());

            // View rootView = inflater.inflate(R.layout.fragment_group, container);
            View rootView = inflater.inflate(R.layout.fragment_group, container, false);
            int i = getArguments().getInt(ARG_GROUP_NUMBER);
            ArrayList<Group> groups = dbhelper.getUserGroups(session.getUserId());
            String mGroupTitles []= new String[groups.size()];
            for(int x=0; x<groups.size(); x++) {
                mGroupTitles[x] = groups.get(x).getGroupname();
            }

            int[] mGroupIds = new int[groups.size()];
            for(int x=0; x<groups.size(); x++) {
                mGroupIds[x] = groups.get(x).getGroupId();
            }

            String mAccessCodes [] = new String[groups.size()];
            for(int x=0; x<groups.size(); x++) {
                mAccessCodes[x] = groups.get(x).getAccessCode();
            }

            String accessCode = mAccessCodes[i];
            Log.e("myAccess", accessCode);

            String groupName = mGroupTitles[i];
            TextView groupNameTextView = (TextView) rootView.findViewById(R.id.groupName);
            groupNameTextView.setText(accessCode);

            int groupId = mGroupIds[i];
            chosenGroupId = groupId;

            TextView groupAccessCodeTextView = (TextView) rootView.findViewById(R.id.groupAccessCode);
            groupAccessCodeTextView.setText(accessCode);
            // int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
            //        "drawable", getActivity().getPackageName());
            // ((TextView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(groupName);

            if(session.isLoggedIn()) {
                mTitle = mDrawerTitle = getTitle();
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

                //ArrayList<User> users
                Cursor c = dbhelper.getUsersInGroup(groupId);

                ArrayList<User> users = new ArrayList<User>(c.getCount());
                if (c.moveToFirst()) {
                    do {
                        User user = new User(c.getInt(0), c.getString(1));
                        users.add(user);
                    } while (c.moveToNext());
                }

                mUserNames = new String[users.size()];
                for(int x=0; x<users.size(); x++) {
                    mUserNames[x] = users.get(x).getUserName();
                }

                mUserIds = new int[users.size()];
                for(int x=0; x<users.size(); x++) {
                    mUserIds[x] = users.get(x).getUserId();
                }

                mDrawerList = (ListView) findViewById(R.id.right_drawer);


                mDrawerList.setAdapter(
                        new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.drawer_list_item,
                                mUserNames));

                mDrawerList.setOnItemClickListener(new RightDrawerItemClickListener());

                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeButtonEnabled(true);

                mDrawerToggle = new ActionBarDrawerToggle(
                        getActivity(), mDrawerLayout, R.drawable.ic_drawer,
                        R.string.drawer_open, R.string.drawer_close
                ) {
                    public void onDrawerClosed(View view) {
                        getActionBar().setTitle(mTitle);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }

                    public void onDrawerOpened(View drawerView) {
                        getActionBar().setTitle(mDrawerTitle);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                };
                mDrawerLayout.setDrawerListener(mDrawerToggle);

                if(savedInstanceState == null) {
                    //selectItem(0);
                }
            }

            return rootView;
        }
    }

    @Override
    public void setFragmentInfo() {
        FragmentManager fm = getFragmentManager();
        //Bundle bundle = new Bundle();
        //bundle.putInt("userId", userId);
        UserTasksFragment fragment = (UserTasksFragment) fm.findFragmentById(R.id.user_tasks_fragment);

        //fragment.setArguments(bundle);
        fragment.setSession(session);
        fragment.setDbhelper(dbhelper);
    }
}