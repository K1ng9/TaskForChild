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

    private static final String DB_NAME = "db";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "task";

    private static final String C_ID = "_id";
    public static final String C_NAME = "name";
    public static final String C_AWARD = "award";
    public static final String C_TIME = "time";
    public static final String C_STATUS = "status";
    public static final String C_DONE = "done";




    private static final String DB_CREATE =
            "create table " + DB_TABLE + " ("
                    + C_ID     + " integer primary key autoincrement, " +
                    C_NAME     + " text, " +
                    C_AWARD    + " int, " +
                    C_TIME     + " text, " +
                    C_STATUS   + " int, " +
                    C_DONE     + " int" + ");";

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
    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData(){
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    // добавить запись
    public void addTask(String name, int award, String time, int status, int done ) {
        ContentValues cv = new ContentValues();
        cv.put(C_NAME, name);
        cv.put(C_AWARD, award);
        cv.put(C_TIME, time);
        cv.put(C_STATUS, status);
        cv.put(C_DONE, done);
        long rowID = mDB.insert(DB_TABLE, null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        //mDB.insert(DB_TABLE, null, cv);
    }

    //обновить инфу

    // удалить запись из DB_TABLE
    public void delTask(long id) {
        int clearCount =  mDB.delete(DB_TABLE, C_ID + " = " + id, null);
        Log.d(LOG_TAG, "deleted rows count = " + clearCount);
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

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String query = "DROP TABLE IF EXISTS " + DB_TABLE;
            // Executes the query provided as long as the query isn't a select
            // or if the query doesn't return any data

            db.execSQL(query);
            onCreate(db);

        }
    }
}
