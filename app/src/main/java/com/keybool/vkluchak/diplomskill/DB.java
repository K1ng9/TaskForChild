package com.keybool.vkluchak.diplomskill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by K1ng9 on 28.05.2015.
 */
public class DB {
    final String LOG_TAG = "myLogs";

    private static final String DB_NAME = "dbDiplom";
    private static final int DB_VERSION = 1;

    private static final String DB_PARENT = "parent";//_________P-A-R-E-N-T_____________

    public static final String C_IDP = "_id";
    public static final String C_LOGIN = "login";
    public static final String C_EMAIL = "email";
    public static final String C_PASSWORD = "password";

    private static final String DB_CREATEPARENT =
            "create table " + DB_PARENT + " ("
                    + C_IDP + " integer primary key autoincrement, " +
                    C_LOGIN + " text, " +
                    C_EMAIL + " text, " +
                    C_PASSWORD + " text" +
                    ");" ;


    //������ � ���� ��� ����������
    //INSERT INTO parent (login, email, password) VALUES ('Simba', 'gmail', 'ytrewq2');

    // �������� ������� ��� ������
    // CREATE UNIQUE INDEX IF NOT EXISTS index_login_parent ON parent( login )


    private static final String DB_CHILD = "child";//____________C-H-I-L-D______________

    public static final String C_IDC = "_id";
    public static final String C_CLOGIN = "login";
    public static final String C_CEMAIL = "email";
    public static final String C_CPASSWORD = "password";
    public static final String C_COINS = "coins";
    public static final String C_LEVL = "levl";
    public static final String C_PARENT = "parent";

    private static final String DB_CREATECHILD =
            "create table "     + DB_CHILD + " ("
                    + C_IDC     + " integer primary key autoincrement, " +
                    C_CLOGIN    + " text, " +
                    C_CEMAIL    + " text, " +
                    C_CPASSWORD + " text, " +
                    C_COINS     + " int, "  +
                    C_LEVL      + " int, "  +
                    C_PARENT    + " int, "  +
                    "FOREIGN KEY(" + C_PARENT + ") REFERENCES " + DB_PARENT +"(" + C_IDP + ")" +  ");";


    //������ � ���� ��� ����������
    //INSERT INTO child (login, email, password, coins, levl) VALUES ('Son2', 'ukr.net', 'shglksdgsdjkl', 200, 5);

    // �������� ������� ��� ������
    // CREATE UNIQUE INDEX IF NOT EXISTS index_login_parent ON child( login )

    private static final String DB_TASK = "task";//______________T-A-S-K___________

    private static final String C_ID = "_id";
    public static final String C_NAME = "name";
    public static final String C_AWARD = "award";
    public static final String C_TIME = "time";
    public static final String C_STATUS = "status";
    public static final String C_DONE = "done";
    public static final String C_CHILD = "child";

    private static final String DB_CREATETASK =
            "create table " + DB_TASK + " ("
                    + C_ID + " integer primary key autoincrement, " +
                    C_NAME   + " text, " +
                    C_AWARD  + " int, " +
                    C_TIME   + " text, " +
                    C_STATUS + " int, " +
                    C_DONE   + " int, " +
                    C_CHILD  + " int, " +
                    "FOREIGN KEY(" + C_CHILD + ") REFERENCES " + DB_CHILD +"(" + C_IDC + ")" + ");";


    // ������ � ����� �����
    // INSERT INTO task (name, award, time, status, done) VALUES ('clean hous', 100, '19:40|04.05', 0, 0);

    // ��������
    // UPDATE task SET status = 1, done = 1 WHERE id = 1

    // SELECT name, award, time, child FROM task GROUP BY time

    // SELECT name, award, time, status, child, login, email FROM task JOIN child GROUP BY time

    // SELECT name, award, time, status, child, login, email FROM task JOIN child ON child.id = task.child GROUP BY time

    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx){
        mCtx = ctx;
    }
    // ������� �����������
    public void open(){
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }
    // ������� �����������
    public void openRead(){
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getReadableDatabase();
    }

    // ������� ����������
    public void close(){
        if(mDBHelper!=null)
            mDBHelper.close();
    }
    //---------------------------- ������ � �������� task-------------------------------------------

    // C_ID + " integer primary key autoincrement, " +
    // C_NAME +   " text, " +
    // C_AWARD +  " int, " +
    // C_TIME +   " text, " +
    // C_STATUS + " int, " +
    // C_DONE +   " int" +
    // C_CHILD +  " int" +
    // �������� ��� ������ �� ������� DB_TASK
    public Cursor getAllData(){
        return mDB.query(DB_TASK, null, null, null, null, null, null);
    }

    // �������� ������
    public void addTask(String name, int award, String time, int status, int done, int child ) {
        ContentValues cv = new ContentValues();
        cv.put(C_NAME, name);
        cv.put(C_AWARD, award);
        cv.put(C_TIME, time);
        cv.put(C_STATUS, status);
        cv.put(C_DONE, done);
        cv.put(C_CHILD, child);
        long rowID = mDB.insert(DB_TASK, null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);

    }
    //�������� ����
    public void updateTaskStatus(long id){
        ContentValues cv = new ContentValues();
        cv.put(C_STATUS, 1);
        //��������� �� id
        int updCount = mDB.update(DB_TASK, cv, C_ID + " = " + id, null);
        Log.d(LOG_TAG, "updated rows count = " + updCount);
    }
    public void updateTaskDone(long id){
        ContentValues cv = new ContentValues();
        cv.put(C_DONE, 1);
        //��������� �� id
        int updCount = mDB.update(DB_TASK, cv, C_ID + " = " + id, null);
        Log.d(LOG_TAG, "updated rows count = " + updCount);
    }
    public void updateTaskNotDone(long id){
        ContentValues cv = new ContentValues();
        cv.put(C_STATUS, 0);
        cv.put(C_DONE, 0);
        //��������� �� id
        int updCount = mDB.update(DB_TASK, cv, C_ID + " = " + id, null);
        Log.d(LOG_TAG, "updated rows count = " + updCount);
    }

    public Cursor sortByTime() {
        return mDB.query("mytable", null, null, null, C_TIME, null, null);
    }


    // ������� ������ �� TASK
    public void delTask(long id) {
        int clearCount =  mDB.delete(DB_TASK, C_ID + " = " + id, null);
        Log.d(LOG_TAG, "deleted rows count = " + clearCount);
    }
    // -----------------------------------������ � �������� CHILD----------------------------------
    // C_IDC     + " integer primary key autoincrement, " +
    // C_CLOGIN    + " text, " +
    // C_CEMAIL    + " text, " +
    // C_CPASSWORD + " text, " +
    // C_COINS     + " int, "  +
    // C_LEVL      + " int, "  +
    // C_PARENT    + " int, "  +

    // �������� ������
    public void addChild(String login, String email, String password, int parent ) {
        ContentValues cv = new ContentValues();
        cv.put(C_CLOGIN, login);
        cv.put(C_CEMAIL, email);
        cv.put(C_CPASSWORD, password);
        cv.put(C_COINS, 0);
        cv.put(C_LEVL, 0);
        cv.put(C_PARENT, parent);
        long rowID = mDB.insert(DB_CHILD, null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);

    }

    public Cursor getChild(String login, String password){
        String sqlQuery = "SELECT " + C_CLOGIN + ", " + C_CEMAIL +", " +
                C_CPASSWORD + ", " + C_COINS + ", " + C_LEVL +
                " FROM " + DB_CHILD +" WHERE " + C_CLOGIN + " = '" +
                login + "' AND " +  C_CPASSWORD + " = '" + password +"'";

        return mDB.rawQuery(sqlQuery, null );
    }

    public Cursor idintifyChild(String login, String password){
        String sqlQuery = "SELECT " + C_CLOGIN + ", " +
                C_CPASSWORD + ", " + C_COINS + ", " + C_LEVL + " FROM "
                + DB_CHILD + " WHERE " + C_CLOGIN + " = '" +
                login + "' AND " +  C_CPASSWORD + " = '" + password +"'";
        return mDB.rawQuery(sqlQuery, null );
    }
    public String getChildEntry(String userName)
    {
        Cursor cursor= mDB.query(DB_CHILD, null, " "+C_CLOGIN+"=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }




    // --------------------------------������ � �������� PARENT ------------------------------------
    // C_IDP +      " integer primary key autoincrement, " +
    // C_LOGIN +    " text, " +
    // C_EMAIL +    " text, " +
    // C_PASSWORD + " text, " +
    // �������� ������
    public void addParent(String login, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put(C_LOGIN, login);
        cv.put(C_EMAIL, email);
        cv.put(C_PASSWORD, password);
        long rowID = mDB.insert(DB_PARENT, null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);

        //return rowID;
    }
    public Cursor getParent(String login, String password){
        String sqlQuery = "SELECT " + C_IDP + ", "+ C_LOGIN + ", " + C_EMAIL +", " +
                C_PASSWORD + " FROM " + DB_PARENT +" WHERE " + C_LOGIN + " = '" +
                login + "' AND " +  C_PASSWORD + " = '" + password +"'";

        Cursor cursor = mDB.rawQuery(sqlQuery, null);

        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return null;
        }else {
            cursor.moveToFirst();
            Log.d(LOG_TAG, "cursor = " + cursor);
            return cursor;
        }
    }

    public Cursor idintifyParent(String login, String password){
        String sqlQuery = "SELECT " + C_LOGIN + ", " +
                C_PASSWORD +  " FROM "  + DB_PARENT + " WHERE " + C_LOGIN + " = '" +
                login + "' AND " +  C_PASSWORD + " = '" + password +"'";
        return mDB.rawQuery(sqlQuery, null );
    }

    public String getParentEntry(String userName)
    {
        Cursor cursor= mDB.query(DB_PARENT, null, C_LOGIN+"=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex(C_PASSWORD));
        cursor.close();
        return password;
    }



    public void delAll(){
        Log.d(LOG_TAG, "--- Clear mytable: ---");
        // ������� id �����b
        mDB.delete(DB_TASK, null, null);

    }


    // ����� �� �������� � ���������� ��
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            if (!db.isReadOnly()) {
                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON;");
            }
        }
        // ������� � ��������� ��
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATECHILD);
            db.execSQL(DB_CREATEPARENT);
            db.execSQL(DB_CREATETASK);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String query = "DROP TABLE IF EXISTS " + DB_TASK;
            String query2 = "DROP TABLE IF EXISTS " + DB_PARENT;
            String query3 = "DROP TABLE IF EXISTS " + DB_CHILD;
            // Executes the query provided as long as the query isn't a select
            // or if the query doesn't return any data
            db.execSQL(query);
            db.execSQL(query2);
            db.execSQL(query3);
            onCreate(db);

        }
    }
}
