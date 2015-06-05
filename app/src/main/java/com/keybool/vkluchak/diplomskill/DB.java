package com.keybool.vkluchak.diplomskill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vkluc_000 on 28.05.2015.
 */
public class DB {
    final String LOG_TAG = "myLogs";

    private static final String DB_NAME = "dbDiplom";
    private static final int DB_VERSION = 1;

    private static final String DB_TABLE2 = "parent";//_________P-A-R-E-N-T_____________

    public static final String C_IDP = "_id";
    public static final String C_LOGIN = "login";
    public static final String C_EMAIL = "email";
    public static final String C_PASSWORD = "password";

    private static final String DB_CREATE2 =
            "create table " + DB_TABLE2 + " ("
                    + C_IDP + " integer primary key autoincrement, " +
                    C_LOGIN + " text, " +
                    C_EMAIL + " text, " +
                    C_PASSWORD + " text, " +
                    ");" ;


    //запрос в базу для добавления
    //INSERT INTO parent (login, email, password) VALUES ('Simba', 'gmail', 'ytrewq2');


    private static final String DB_TABLE3 = "child";//____________C-H-I-L-D______________

    public static final String C_IDC = "_id";
    public static final String C_CLOGIN = "login";
    public static final String C_CEMAIL = "email";
    public static final String C_CPASSWORD = "password";
    public static final String C_COINS = "coins";
    public static final String C_LEVL = "levl";
    public static final String C_PARENT = "parent";

    private static final String DB_CREATE3 =
            "create table "     + DB_TABLE3 + " ("
                    + C_IDC     + " integer primary key autoincrement, " +
                    C_CLOGIN    + " text, " +
                    C_CEMAIL    + " text, " +
                    C_CPASSWORD + " text, " +
                    C_COINS     + " int, "  +
                    C_LEVL      + " int, "  +
                    C_PARENT    + " int, "  +
                    "FOREIGN KEY(" + C_PARENT + ") REFERENCES " + DB_TABLE2 +"(" + C_IDP + ")" +  ");";


    //запрос в базу для добавления
    //INSERT INTO child (login, email, password, coins, levl) VALUES ('Son2', 'ukr.net', 'shglksdgsdjkl', 200, 5);

    private static final String DB_TABLE = "task";//______________T-A-S-K___________

    private static final String C_ID = "_id";
    public static final String C_NAME = "name";
    public static final String C_AWARD = "award";
    public static final String C_TIME = "time";
    public static final String C_STATUS = "status";
    public static final String C_DONE = "done";
    public static final String C_CHILD = "child";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + " ("
                    + C_ID + " integer primary key autoincrement, " +
                    C_NAME + " text, " +
                    C_AWARD + " int, " +
                    C_TIME + " text, " +
                    C_STATUS + " int, " +
                    C_DONE + " int" +
                    C_CHILD + " int" +
                    "FOREIGN KEY(" + C_CHILD + ") REFERENCES " + DB_TABLE3 +"(" + C_IDC + ")" + ");";


    // запрос в будзу даннх
    // INSERT INTO task (name, award, time, status, done) VALUES ('clean hous', 100, '19:40|04.05', 0, 0);

    // обновить
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
    // открить подключение
    public void open(){
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }
    // закрить подлючение
    public void close(){
        if(mDBHelper!=null)
            mDBHelper.close();
    }
    //---------------------------- робота с таблицей task-------------------------------------------

    // C_ID + " integer primary key autoincrement, " +
    // C_NAME +   " text, " +
    // C_AWARD +  " int, " +
    // C_TIME +   " text, " +
    // C_STATUS + " int, " +
    // C_DONE +   " int" +
    // C_CHILD +  " int" +
    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData(){
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    // добавить запись
    public void addTask(String name, int award, String time, int status, int done, int child ) {
        ContentValues cv = new ContentValues();
        cv.put(C_NAME, name);
        cv.put(C_AWARD, award);
        cv.put(C_TIME, time);
        cv.put(C_STATUS, status);
        cv.put(C_DONE, done);
        cv.put(C_CHILD, child);
        long rowID = mDB.insert(DB_TABLE, null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        //mDB.insert(DB_TABLE, null, cv);
    }
    //обновить инфу
    public void updateTaskStatus(long id){
        ContentValues cv = new ContentValues();
        cv.put(C_STATUS, 1);
        //обновляем по id
        int updCount = mDB.update(DB_TABLE, cv, C_ID + " = " + id, null);
        Log.d(LOG_TAG, "updated rows count = " + updCount);
    }
    public void updateTaskDone(long id){
        ContentValues cv = new ContentValues();
        cv.put(C_DONE, 1);
        //обновляем по id
        int updCount = mDB.update(DB_TABLE, cv, C_ID + " = " + id, null);
        Log.d(LOG_TAG, "updated rows count = " + updCount);
    }
    public void updateTaskNotDone(long id){
        ContentValues cv = new ContentValues();
        cv.put(C_STATUS, 0);
        cv.put(C_DONE, 0);
        //обновляем по id
        int updCount = mDB.update(DB_TABLE, cv, C_ID + " = " + id, null);
        Log.d(LOG_TAG, "updated rows count = " + updCount);
    }

    public Cursor sortByTime() {
        return mDB.query("mytable", null, null, null, C_TIME, null, null);
    }


    // удалить запись из TASK
    public void delTask(long id) {
        int clearCount =  mDB.delete(DB_TABLE, C_ID + " = " + id, null);
        Log.d(LOG_TAG, "deleted rows count = " + clearCount);
    }
    // -----------------------------------робота с таблицей CHILD----------------------------------
    // C_IDC     + " integer primary key autoincrement, " +
    // C_CLOGIN    + " text, " +
    // C_CEMAIL    + " text, " +
    // C_CPASSWORD + " text, " +
    // C_COINS     + " int, "  +
    // C_LEVL      + " int, "  +
    // C_PARENT    + " int, "  +

    // добавить запись
    public void addChild(String login, String email, String password, int coins, int levl, int parent ) {
        ContentValues cv = new ContentValues();
        cv.put(C_CLOGIN, login);
        cv.put(C_CEMAIL, email);
        cv.put(C_CPASSWORD, password);
        cv.put(C_COINS, coins);
        cv.put(C_LEVL, levl);
        cv.put(C_PARENT, parent);
        long rowID = mDB.insert(DB_TABLE, null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        //mDB.insert(DB_TABLE, null, cv);
    }

    public Cursor getChild(String login, String password){
        String sqlQuery = "SELECT " + C_CLOGIN + ", " + C_CEMAIL +", " +
                C_CPASSWORD + ", " + C_COINS + ", " + C_LEVL +
                " FROM " +DB_TABLE3 +" WHERE " + C_CLOGIN + " = '" +
                login + "' AND " +  C_CPASSWORD + " = '" + password +"'";

        return mDB.rawQuery(sqlQuery, null );
    }




    // --------------------------------робота с таблицей PARENT ------------------------------------
    // C_IDP +      " integer primary key autoincrement, " +
    // C_LOGIN +    " text, " +
    // C_EMAIL +    " text, " +
    // C_PASSWORD + " text, " +
    // добавить запись
    public void addParent(String login, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put(C_LOGIN, login);
        cv.put(C_EMAIL, email);
        cv.put(C_PASSWORD, password);
        long rowID = mDB.insert(DB_TABLE, null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        //mDB.insert(DB_TABLE, null, cv);
    }
    public Cursor getParent(String login, String password){
        String sqlQuery = "SELECT " + C_LOGIN + ", " + C_EMAIL +", " +
                C_PASSWORD + " FROM " +DB_TABLE2 +" WHERE " + C_LOGIN + " = '" +
                login + "' AND " +  C_PASSWORD + " = '" + password +"'";

        return mDB.rawQuery(sqlQuery, null );
    }

    public void delAll(){
        Log.d(LOG_TAG, "--- Clear mytable: ---");
        // удвляем id записb
        mDB.delete(DB_TABLE, null, null);

    }


    // класс по созданию и управлению БД
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
        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE3);
            db.execSQL(DB_CREATE);
            db.execSQL(DB_CREATE2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String query = "DROP TABLE IF EXISTS " + DB_TABLE;
            String query2 = "DROP TABLE IF EXISTS " + DB_TABLE2;
            String query3 = "DROP TABLE IF EXISTS " + DB_TABLE3;
            // Executes the query provided as long as the query isn't a select
            // or if the query doesn't return any data
            db.execSQL(query);
            db.execSQL(query2);
            db.execSQL(query3);
            onCreate(db);

        }
    }
}
