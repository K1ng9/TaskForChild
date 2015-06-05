package com.keybool.vkluchak.diplomskill;

import android.database.Cursor;

/**
 * Created by vkluc_000 on 05.06.2015.
 */
public class Parent {
    private String plogin, pemail, ppassword;
    private int id;

    Cursor cursor;

    DB db;

    Parent(String login,String password) {

        cursor = db.getParent(login, password);

        id = cursor.getInt(cursor.getColumnIndex(DB.C_IDP));
        plogin = cursor.getString(cursor.getColumnIndex(DB.C_LOGIN));
        pemail = cursor.getString(cursor.getColumnIndex(DB.C_EMAIL));
        ppassword = cursor.getString(cursor.getColumnIndex(DB.C_PASSWORD));

    }

    public String getPlogin() {
        return plogin;
    }

    public String getPemail() {
        return pemail;
    }

    public String getPpassword() {
        return ppassword;
    }

    public int getId() {
        return id;
    }
}
