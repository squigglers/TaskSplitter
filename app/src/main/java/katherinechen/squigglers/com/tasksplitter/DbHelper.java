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
import java.util.ArrayList;

//interacts with database to update database entries
//note: groupy is the table for group
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
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

    //gets all the groups a user is in and returns it in a cursor
    public Cursor getUserGroupsCursor(int userId) {

        //select groupId, groupname from Group, UserGroup where Group.Id = UserGroup.groupId and UserGroup.userId = userId
        String query = "SELECT " + Groupy._ID + ", " + Groupy.NAME +
                " FROM " + Groupy.TABLE_NAME + ", " + UserGroup.TABLE_NAME
                + " WHERE " + Groupy._ID + " = " + UserGroup.GROUP_ID + " AND " + UserGroup.USER_ID + " = " + userId;
        Cursor c = db.rawQuery(query, null);

        return c;
    }

    //gets all the users (real name) in a group and returns it in a cursor
    public Cursor getUsersInGroup(int groupId) {

        //select userId, name from User, UserGroup where User.Id = UserGroup.userId and UserGroup.groupId = groupId
        String query = "SELECT " + User._ID + ", " + User.NAME +
                " FROM " + User.TABLE_NAME + ", " + UserGroup.TABLE_NAME
                + " WHERE " + User._ID + " = " + UserGroup.USER_ID + " AND " + UserGroup.GROUP_ID + " = " + groupId;
        Cursor c = db.rawQuery(query, null);

        return c;
    }

    //gets all the groups a user is in and returns it in an array
    public ArrayList<Group> getUserGroups(int userId) {

        //select groupId from Group, UserGroup where Group.Id = UserGroup.groupId and UserGroup.userId = userId
        String query = "SELECT " + Groupy._ID + ", " + Groupy.NAME +
                " FROM " + Groupy.TABLE_NAME + ", " + UserGroup.TABLE_NAME
                + " WHERE " + Groupy._ID + " = " + UserGroup.GROUP_ID + " AND " + UserGroup.USER_ID + " = " + userId;
        Cursor c = db.rawQuery(query, null);

        //store all of a user's groupId and groupname into userGroups
        ArrayList<Group> userGroups = new ArrayList<Group>(c.getCount());
        if(c.moveToFirst())
        {
            do{
                Group group = new Group(c.getInt(0), c.getString(1));
                userGroups.add(group);
            }while (c.moveToNext());
        }

        return userGroups;
    }

    //checks to see if the username is already taken in the user table
    //return true if username already exists, else return false
    public boolean checkUsernameExists(String username)
    {
        boolean exists = false;

        String[] projection = {User.USERNAME};
        String selection = User.USERNAME + "=?";
        String[] selectionArgs = {username};
        Cursor c = db.query(User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(c.getCount() > 0)
            exists = true;

        return exists;
    }

    //check to see if access code already exists in the group table
    //return true if access code already exists, else return false
    public boolean checkAccessCodeExists(String accessCode)
    {
        boolean exists = false;

        String[] projection = {Groupy.ACCESSCODE};
        String selection = Groupy.ACCESSCODE + "=?";
        String[] selectionArgs = {accessCode};
        Cursor c = db.query(Groupy.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(c.getCount() > 0)
            exists = true;

        return exists;
    }

    //validates group login
    //return groupid if validated, else return -1
    public int validateGroupLogin(String groupName, String accessCode)
    {
        //get hashed accessCode
        String hashedAccessCode = hashPassword(accessCode);

        int groupId = -1;

        String[] projection = {Groupy._ID, Groupy.NAME, Groupy.ACCESSCODE,};
        String selection = Groupy.NAME + "=?" + " AND " + Groupy.ACCESSCODE + "=?";
        String[] selectionArgs = {groupName, hashedAccessCode};
        Cursor c = db.query(Groupy.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(c.getCount() > 0)
        {
            c.moveToFirst();
            groupId = c.getInt(c.getColumnIndexOrThrow(Groupy._ID));
        }

        return groupId;
    }

    //validates user login
    //return userid if validated, else return -1
    public int validatedUserLogin(String username, String password)
    {
        //get hashed password
        String hashedPassword = hashPassword(password);

        int userId = -1;

        String[] projection = {User._ID, User.USERNAME, User.PASSWORD};
        String selection = User.USERNAME + "=?" + " AND " + User.PASSWORD + "=?";
        String[] selectionArgs = {username, hashedPassword};
        Cursor c = db.query(User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(c.getCount() > 0)
        {
            c.moveToFirst();
            userId = c.getInt(c.getColumnIndexOrThrow(User._ID));
        }

        return userId;
    }

    //hashes a password and uses SHA-256
    //apparently bcrypt would be better for this assuming it's not too slow
    public String hashPassword(String password)
    {
        String encoding = "UTF-8";
        String hashAlgorithm = "SHA-256";

        //turn salt and password to bytes
        String salt = "aiw23784dskjhfh4jekw";
        byte[] bSalt = null;
        byte [] bPassword = null;
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
        byte [] digest = digester.digest(bSalt);

        //convert byte form to String form
        String hashedPassword = Base64.encodeToString(digest, 0, digest.length, 0);

        //return hashed password
        //Log.e("hashing", hashedPassword);
        return hashedPassword;
    }

    //<editor-fold desc="add tuple to tables">
    public int addUser(String name, String username, String password)
    {
        //hash password
        String hashedPassword = hashPassword(password);

        ContentValues values = new ContentValues();
        values.put(User.NAME, name);
        values.put(User.USERNAME, username);
        values.put(User.PASSWORD, hashedPassword);

        int userID = (int) db.insert(User.TABLE_NAME, null, values);

        return userID;
    }

    public int addGroup(String name, String accessCode)
    {
        //hash accessCode
        String hashedAccessCode = hashPassword(accessCode);

        ContentValues values = new ContentValues();
        values.put(Groupy.NAME, name);
        values.put(Groupy.ACCESSCODE, hashedAccessCode);

        int groupID = (int) db.insert(Groupy.TABLE_NAME, null, values);

        return groupID;
    }

    public void addUserGroup(int groupID, int userID)
    {
        ContentValues values = new ContentValues();
        values.put(UserGroup.GROUP_ID, groupID);
        values.put(UserGroup.USER_ID, userID);

        db.insert(UserGroup.TABLE_NAME, null, values);
    }

    public void addTask(int groupId, int assignerId, int toUserId,
                        String taskName, String description, String dueDate)
    {
        ContentValues values = new ContentValues();
        values.put(Task.GROUP_ID, groupId);
        values.put(Task.ASSIGNER_ID, assignerId);
        values.put(Task.TO_USER_ID, toUserId);
        values.put(Task.TASK_NAME, taskName);
        values.put(Task.DESCRIPTION, description);
        values.put(Task.DUE_DATE, dueDate);

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
        public static final String TO_USER_ID = "toUserId";     //user that the task is assigned to
        public static final String TASK_NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String DUE_DATE = "dueDate";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        GROUP_ID + " INTEGER, " +
                        ASSIGNER_ID + " INTEGER, " +
                        TO_USER_ID + " INTEGER, " +
                        TASK_NAME + " TEXT, " +
                        DESCRIPTION + " TEXT, " +
                        DUE_DATE + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
/*
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
*/
    //</editor-fold>
}
