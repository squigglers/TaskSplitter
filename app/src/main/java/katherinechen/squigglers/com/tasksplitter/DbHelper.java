package katherinechen.squigglers.com.tasksplitter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

//interacts with database to update database entries
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TaskSplitter.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.SQL_CREATE_ENTRIES);
        db.execSQL(Group.SQL_CREATE_ENTRIES);
        db.execSQL(UserGroup.SQL_CREATE_ENTRIES);
        db.execSQL(Task.SQL_CREATE_ENTRIES);
        db.execSQL(GroupTask.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(User.SQL_DELETE_ENTRIES);
        db.execSQL(Group.SQL_DELETE_ENTRIES);
        db.execSQL(UserGroup.SQL_DELETE_ENTRIES);
        db.execSQL(Task.SQL_DELETE_ENTRIES);
        db.execSQL(GroupTask.SQL_DELETE_ENTRIES);

        onCreate(db);
    }

    //do not use unless we want to clear the database
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(User.SQL_DELETE_ENTRIES);
        db.execSQL(Group.SQL_DELETE_ENTRIES);
        db.execSQL(UserGroup.SQL_DELETE_ENTRIES);
        db.execSQL(Task.SQL_DELETE_ENTRIES);
        db.execSQL(GroupTask.SQL_DELETE_ENTRIES);

        onCreate(db);
    }

    //checks to see if the username is already taken in the user table
    //return true if username already exists, else return false
    public boolean checkUsernameExists(String username)
    {
        boolean exists = false;
        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {User.USERNAME};
        String selection = User.USERNAME + "=?";
        String[] selectionArgs = {username};
        Cursor c = db.query(User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(c.getCount() > 0)
            exists = true;

        db.close();
        return exists;
    }

    //check to see if access code already exists in the group table
    //return true if access code already exists, else return false
    public boolean checkAccessCodeExists(String accessCode)
    {
        boolean exists = false;
        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {Group.ACCESSCODE};
        String selection = Group.ACCESSCODE + "=?";
        String[] selectionArgs = {accessCode};
        Cursor c = db.query(Group.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(c.getCount() > 0)
            exists = true;

        db.close();
        return exists;
    }

    //validates group login
    //return groupid if validated, else return -1
    public int validateGroupLogin(String groupName, String accessCode)
    {
        int groupId = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {Group._ID, Group.NAME, Group.ACCESSCODE,};
        String selection = Group.NAME + "=?" + " AND " + Group.ACCESSCODE + "=?";
        String[] selectionArgs = {groupName, accessCode};
        Cursor c = db.query(Group.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(c.getCount() > 0)
        {
            c.moveToFirst();
            groupId = c.getInt(c.getColumnIndexOrThrow(Group._ID));
        }

        return groupId;
    }

    //validates user login
    //return userid if validated, else return -1
    public int validatedUserLogin(String username, String password)
    {
        int userId = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {User._ID, User.USERNAME, User.PASSWORD};
        String selection = User.USERNAME + "=?" + " AND " + User.PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor c = db.query(User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(c.getCount() > 0)
        {
            c.moveToFirst();
            userId = c.getInt(c.getColumnIndexOrThrow(User._ID));
        }

        return userId;
    }

    //<editor-fold desc="add tuple to tables">
    public int addUser(String name, String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.NAME, name);
        values.put(User.USERNAME, username);
        values.put(User.PASSWORD, password);

        int userID = (int) db.insert(User.TABLE_NAME, null, values);
        db.close();

        return userID;
    }

    public int addGroup(String name, String accessCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Group.NAME, name);
        values.put(Group.ACCESSCODE, accessCode);

        int groupID = (int) db.insert(Group.TABLE_NAME, null, values);
        db.close();

        return groupID;
    }

    public void addUserGroup(int groupID, int userID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserGroup.GROUP_ID, groupID);
        values.put(UserGroup.USER_ID, userID);

        db.insert(UserGroup.TABLE_NAME, null, values);
        db.close();
    }

    public void addTask(String name, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Task.NAME, name);
        values.put(Task.DESCRIPTION, description);

        db.insert(Task.TABLE_NAME, null, values);
        db.close();
    }

    public void addGroupTask(int taskId, int groupId, int userId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GroupTask.TASK_ID, taskId);
        values.put(GroupTask.GROUP_ID, groupId);
        values.put(GroupTask.USER_ID, userId);

        db.insert(GroupTask.TABLE_NAME, null, values);
        db.close();
    }
    //</editor-fold>

    //<editor-fold desc="table metadata">

    //user table - stores information about the user (id, name, email, password)
    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String NAME = "name";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        NAME + " TEXT, " +
                        USERNAME + " TEXT, " +
                        PASSWORD + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //group table - stores information about the group (id, name, accessCode)
    public static abstract class Group implements BaseColumns {
        public static final String TABLE_NAME = "groupy";
        public static final String NAME = "name";
        public static final String ACCESSCODE = "accessCode";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        NAME + " TEXT, " +
                        ACCESSCODE + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //userGroup table - stores which groups a user is in
    public static abstract class UserGroup {
        public static final String TABLE_NAME = "userGroup";
        public static final String GROUP_ID = "groupId";
        public static final String USER_ID = "userId";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        GROUP_ID + " INTEGER, " +
                        USER_ID + " INTEGER)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //task table - stores information about a task (id, name, description, deadline)
    public static abstract class Task implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        NAME + " TEXT, " +
                        DESCRIPTION + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //groupTask table - stores which group and user to which a task is assigned
    public static abstract class GroupTask implements BaseColumns {
        public static final String TABLE_NAME = "groupTask";
        public static final String TASK_ID = "taskId";
        public static final String GROUP_ID = "groupId";
        public static final String USER_ID = "userId";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        TASK_ID + " INTEGER, " +
                        GROUP_ID + " INTEGER, " +
                        USER_ID + " INTEGER)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    //</editor-fold>
}
