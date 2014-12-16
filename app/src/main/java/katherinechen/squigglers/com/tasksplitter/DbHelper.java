package katherinechen.squigglers.com.tasksplitter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//interacts with database to update database entries
//note: groupy is the table for group
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "TaskSplitter.db";
    SQLiteDatabase db;
    Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(User.SQL_CREATE_ENTRIES);
        db.execSQL(Groupy.SQL_CREATE_ENTRIES);
        db.execSQL(UserGroup.SQL_CREATE_ENTRIES);
        db.execSQL(Task.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(User.SQL_DELETE_ENTRIES);
        db.execSQL(Groupy.SQL_DELETE_ENTRIES);
        db.execSQL(UserGroup.SQL_DELETE_ENTRIES);
        db.execSQL(Task.SQL_DELETE_ENTRIES);

        onCreate(db);
    }

    //do not use unless we want to clear the database
    public void clearDatabase() {
        db.execSQL(User.SQL_DELETE_ENTRIES);
        db.execSQL(Groupy.SQL_DELETE_ENTRIES);
        db.execSQL(UserGroup.SQL_DELETE_ENTRIES);
        db.execSQL(Task.SQL_DELETE_ENTRIES);

        onCreate(db);
    }

    //gets all the groups (id, name) a user is in and returns it in a cursor
    public Cursor getUserGroupsCursor(int userId) {

        //select groupId, groupname from Group, UserGroup where Group.Id = UserGroup.groupId and UserGroup.userId = userId
        String query = "SELECT " + Groupy._ID + ", " + Groupy.NAME +
                " FROM " + Groupy.TABLE_NAME + ", " + UserGroup.TABLE_NAME
                + " WHERE " + Groupy._ID + " = " + UserGroup.GROUP_ID + " AND " + UserGroup.USER_ID + " = " + userId;
        Cursor c = db.rawQuery(query, null);

        return c;
    }

    //gets all the users (id, real name) in a group and returns it in a cursor
    public Cursor getUsersInGroup(int groupId) {

        //select userId, name from User, UserGroup where User.Id = UserGroup.userId and UserGroup.groupId = groupId
        String query = "SELECT " + User._ID + ", " + User.NAME +
                " FROM " + User.TABLE_NAME + ", " + UserGroup.TABLE_NAME
                + " WHERE " + User._ID + " = " + UserGroup.USER_ID + " AND " + UserGroup.GROUP_ID + " = " + groupId;
        Cursor c = db.rawQuery(query, null);

        return c;
    }

    //gets the access code based on id
    public String getGroupAccessCode(int groupId) {
        //select accesscode from Groupy where Groupy.groupId = groupId
        final String[] projection = {Groupy.ACCESSCODE};
        final String selection = Groupy._ID + "=?";
        final String[] selectionArgs = {String.valueOf(groupId)};

        Cursor c = db.query(Groupy.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        c.moveToFirst();
        String accesscode = c.getString(c.getColumnIndexOrThrow(DbHelper.Groupy.ACCESSCODE));

        return accesscode;
    }

    //gets the group name based on id
    public String getGroupName(int groupId) {
        //select groupname from Groupy where Groupy.groupId = groupId
        final String[] projection = {Groupy.NAME};
        final String selection = Groupy._ID + "=?";
        final String[] selectionArgs = {String.valueOf(groupId)};

        Cursor c = db.query(Groupy.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        c.moveToFirst();
        String groupName = c.getString(c.getColumnIndexOrThrow(DbHelper.Groupy.NAME));

        return groupName;
    }

    //gets the user name based on userId
    public String getUserName(int userId) {
        //select name from User where User.userId = userId
        final String[] projection = {User.NAME};
        final String selection = User._ID + "=?";
        final String[] selectionArgs = {String.valueOf(userId)};

        Cursor c = db.query(User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        c.moveToFirst();
        String name = c.getString(c.getColumnIndexOrThrow(User.NAME));

        return name;
    }

    //get the task description based on task id
    public Cursor getTaskInfo(int taskId) {
        //select taskId, taskname, taskdescription, assignerId, completed, groupId, archived from Task
        //where Task.ID = taskId
        final String[] projection = {Task._ID, Task.TASK_NAME, Task.DESCRIPTION, Task.ASSIGNER_ID, Task.COMPLETED, Task.GROUP_ID, Task.ARCHIVED};
        final String selection = Task._ID + "=?";
        final String[] selectionArgs = {String.valueOf(taskId)};

        Cursor c = db.query(Task.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        return c;
    }

    //gets all the tasks based on user and group and returns it in a cursor
    public Cursor getUserTasksInGroup(int userId, int groupId) {
        //select taskId, taskname, taskdescription, User.name, completed from Task
        //where Task.userId = userId and Task.groupId = groupId and Task.assignerId = User.id and Task.archived = 0
        //order by completed
        String taskId = Task.TABLE_NAME + "." + Task._ID;
        String taskUserId = Task.TABLE_NAME + "." + Task.USER_ID;
        String userUserId = User.TABLE_NAME + "." + User._ID;

        String query = "SELECT " + taskId + ", " + Task.TASK_NAME + ", " + Task.DESCRIPTION + ", " + User.NAME + ", " + Task.COMPLETED +
                " FROM " + Task.TABLE_NAME + ", " + User.TABLE_NAME +
                " WHERE " + taskUserId + " = " + userId + " AND " + Task.GROUP_ID + " = " + groupId +
                    " AND " + Task.ASSIGNER_ID + " = " + userUserId + " AND " + Task.ARCHIVED + " = 0" +
                " ORDER BY " + Task.COMPLETED;

        Cursor c = db.rawQuery(query, null);
        return c;
    }

    //get all the archived tasks in a group
    public Cursor getArchivedTasksInGroup(int groupId) {
        //select taskId, taskname, taskdescription, User.name, completed from Task
        //where Task.groupId = groupId and Task.assignerId = User.id and Task.archived = 1
        //order by completed
        String taskId = Task.TABLE_NAME + "." + Task._ID;
        String userUserId = User.TABLE_NAME + "." + User._ID;

        String query = "SELECT " + taskId + ", " + Task.TASK_NAME + ", " + Task.DESCRIPTION + ", " + User.NAME + ", " + Task.COMPLETED +
                " FROM " + Task.TABLE_NAME + ", " + User.TABLE_NAME +
                " WHERE " + Task.GROUP_ID + " = " + groupId + " AND " + Task.ASSIGNER_ID + " = " + userUserId + " AND " + Task.ARCHIVED + " = 1" +
                " ORDER BY " + Task.COMPLETED;

        Cursor c = db.rawQuery(query, null);
        return c;
    }

    //update a task to be archived
    public void archiveTask(int taskId) {
        // new value for one column
        ContentValues values = new ContentValues();
        values.put(Task.ARCHIVED, 1);

        //which row to update, based on the ID
        String selection = Task._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        db.update(Task.TABLE_NAME, values, selection, selectionArgs);
    }

    //update a task to be archived
    public void unarchiveTask(int taskId) {
        // new value for one column
        ContentValues values = new ContentValues();
        values.put(Task.ARCHIVED, 0);

        //which row to update, based on the ID
        String selection = Task._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        db.update(Task.TABLE_NAME, values, selection, selectionArgs);
    }

    //update a task with a different user
    public void changeTaskUser(int taskId, int userId) {
        // new value for one column
        ContentValues values = new ContentValues();
        values.put(Task.USER_ID, userId);

        //which row to update, based on the ID
        String selection = Task._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        db.update(Task.TABLE_NAME, values, selection, selectionArgs);
    }

    //updates whether a user has completed a task
    public void updateTaskCompletion(int taskId, int completed) {
        ContentValues values = new ContentValues();
        values.put(Task.COMPLETED, completed);

        String selection = Task._ID + "=?";
        String[] selectionArgs = {String.valueOf(taskId)};

        db.update(Task.TABLE_NAME, values, selection, selectionArgs);
    }

    //checks to see if the username is already taken in the user table
    //return true if username already exists, else return false
    public boolean checkUsernameExists(String username) {
        boolean exists = false;

        String[] projection = {User.USERNAME};
        String selection = User.USERNAME + "=?";
        String[] selectionArgs = {username};
        Cursor c = db.query(User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (c.getCount() > 0)
            exists = true;

        return exists;
    }

    //check to see if access code already exists in the group table
    //return true if access code already exists, else return false
    public boolean checkAccessCodeExists(String accessCode) {
        boolean exists = false;

        String[] projection = {Groupy.ACCESSCODE};
        String selection = Groupy.ACCESSCODE + "=?";
        String[] selectionArgs = {accessCode};
        Cursor c = db.query(Groupy.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (c.getCount() > 0)
            exists = true;

        return exists;
    }

    //validates group login
    //return groupid if validated, else return -1
    public int validateGroupLogin(String groupName, String accessCode) {
        //get hashed accessCode
        //String hashedAccessCode = hashPassword(accessCode);

        int groupId = -1;

        String[] projection = {Groupy._ID, Groupy.NAME, Groupy.ACCESSCODE};
        String selection = Groupy.NAME + "=?" + " AND " + Groupy.ACCESSCODE + "=?";
        String[] selectionArgs = {groupName, accessCode};
        Cursor c = db.query(Groupy.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            groupId = c.getInt(c.getColumnIndexOrThrow(Groupy._ID));
        }

        return groupId;
    }

    //validates user login
    //return userid if validated, else return -1
    public int validatedUserLogin(String username, String password) {
        //get hashed password
        String hashedPassword = hashPassword(password);

        int userId = -1;

        String[] projection = {User._ID, User.USERNAME, User.PASSWORD};
        String selection = User.USERNAME + "=?" + " AND " + User.PASSWORD + "=?";
        String[] selectionArgs = {username, hashedPassword};
        Cursor c = db.query(User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            userId = c.getInt(c.getColumnIndexOrThrow(User._ID));
        }

        return userId;
    }

    //hashes a password and uses SHA-256
    //apparently bcrypt would be better for this assuming it's not too slow
    public String hashPassword(String password) {
        String encoding = "UTF-8";
        String hashAlgorithm = "SHA-256";

        //turn salt and password to bytes
        String salt = "aiw23784dskjhfh4jekw";
        byte[] bSalt = null;
        byte[] bPassword = null;
        try {
            bSalt = salt.getBytes(encoding);
            bPassword = password.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //create message digester with hash algorithm
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance(hashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            Log.e("hashing", "no hash function");
        }

        //hash password into byte form
        digester.update(bPassword);
        byte[] digest = digester.digest(bSalt);

        //convert byte form to String form
        String hashedPassword = Base64.encodeToString(digest, 0, digest.length, 0);

        //return hashed password
        //Log.e("hashing", hashedPassword);
        return hashedPassword;
    }

    //<editor-fold desc="add tuple to tables">
    public int addUser(String name, String username, String password) {
        //hash password
        String hashedPassword = hashPassword(password);

        ContentValues values = new ContentValues();
        values.put(User.NAME, name);
        values.put(User.USERNAME, username);
        values.put(User.PASSWORD, hashedPassword);

        int userID = (int) db.insert(User.TABLE_NAME, null, values);

        return userID;
    }

    public int addGroup(String name, String accessCode) {
        //hash accessCode
        //String hashedAccessCode = hashPassword(accessCode);

        ContentValues values = new ContentValues();
        values.put(Groupy.NAME, name);
        values.put(Groupy.ACCESSCODE, accessCode);

        int groupID = (int) db.insert(Groupy.TABLE_NAME, null, values);

        return groupID;
    }

    public void addUserGroup(int groupID, int userID) {
        ContentValues values = new ContentValues();
        values.put(UserGroup.GROUP_ID, groupID);
        values.put(UserGroup.USER_ID, userID);

        db.insert(UserGroup.TABLE_NAME, null, values);
    }

    public void addTask(int groupId, int toUserId, int assignerId,
                        String taskName, String description, String dueDate, int completed, int archived) {
        ContentValues values = new ContentValues();
        values.put(Task.GROUP_ID, groupId);
        values.put(Task.USER_ID, toUserId);
        values.put(Task.ASSIGNER_ID, assignerId);
        values.put(Task.TASK_NAME, taskName);
        values.put(Task.DESCRIPTION, description);
        values.put(Task.DUE_DATE, dueDate);
        values.put(Task.COMPLETED, completed);
        values.put(Task.ARCHIVED, archived);

        db.insert(Task.TABLE_NAME, null, values);
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
    public static abstract class Groupy implements BaseColumns {
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

    //task table - stores information about a task
    public static abstract class Task implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String GROUP_ID = "groupId";        //group that the task is in
        public static final String ASSIGNER_ID = "fromUserId";  //user that assigned the task
        public static final String USER_ID = "toUserId";     //user that the task is assigned to
        public static final String TASK_NAME = "taskname";
        public static final String DESCRIPTION = "description";
        public static final String DUE_DATE = "dueDate";
        public static final String COMPLETED = "completed";
        public static final String ARCHIVED = "archived";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        GROUP_ID + " INTEGER, " +
                        ASSIGNER_ID + " INTEGER, " +
                        USER_ID + " INTEGER, " +
                        TASK_NAME + " TEXT, " +
                        DESCRIPTION + " TEXT, " +
                        DUE_DATE + " TEXT, " +
                        COMPLETED + " INTEGER, " +
                        ARCHIVED + " INTEGER)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    //</editor-fold>
}
