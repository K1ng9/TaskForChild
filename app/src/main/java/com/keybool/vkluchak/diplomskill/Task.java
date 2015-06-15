package com.keybool.vkluchak.diplomskill;

import android.content.Context;
import android.database.Cursor;

import java.util.Calendar;

/**
 * Created by K1ng9 on 12.06.2015.
 */
public class Task {
    Cursor cursor;
    DB db;
    private String name;
    private int id, done, status, award;
    private Calendar time;
    Child child;

    Task(Context context, String login, String password) {
        db = new DB(context);
        db.open();
        //cursor = db.getTask();

    }
}
