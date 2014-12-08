package katherinechen.squigglers.com.tasksplitter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
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


public class CreateGroupActivity extends LoggedInBaseActivity implements SessionInterface {

    SessionManager session;
    DbHelper dbhelper;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mGroupTitles;
    private int[] mGroupIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

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
        private String[] mUserTasks;
        private int[] mUserIds;



        public GroupFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            session = new SessionManager(getApplicationContext());
            dbhelper = new DbHelper(getApplicationContext());



            if(session.isLoggedIn()) {
                mTitle = mDrawerTitle = getTitle();
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

                ArrayList<Task> tasks = dbhelper.getTasksforUserInGroup(session.getUserId());
                mUserTasks = new String[tasks.size()];
                for(int x=0; x<tasks.size(); x++) {
                    mUserTasks[x] = tasks.get(x).getGroupname();
                }

                mUserIds = new int[taks.size()];
                for(int x=0; x<tasks.size(); x++) {
                    mUserIds[x] = tasks.get(x).getGroupId();
                }

                mDrawerList = (ListView) findViewById(R.id.right_drawer);


                mDrawerList.setAdapter(
                        new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.drawer_list_item,
                                mUserTasks));

                mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

            String groupName = mGroupTitles[i];
            TextView groupNameTextView = (TextView) rootView.findViewById(R.id.groupName);
            groupNameTextView.setText(groupName);

            int groupId = mGroupIds[i];
            TextView groupAccessCodeTextView = (TextView) rootView.findViewById(R.id.groupAccessCode);
            groupAccessCodeTextView.setText(groupId+"");
            // int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
            //        "drawable", getActivity().getPackageName());
            // ((TextView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(groupName);
            return rootView;
        }
    }


    @Override
    public void setFragmentInfo() {
        FragmentManager fm = getFragmentManager();
        //CreateGroupFragment fragment = (CreateGroupFragment) fm.findFragmentById(R.id.create_group_fragment);
        CreateGroupFragment fragment = new CreateGroupFragment();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();

        fragment.setSession(super.session);
        fragment.setDbhelper(super.dbhelper);
    }
}
