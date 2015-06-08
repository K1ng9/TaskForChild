package com.keybool.vkluchak.diplomskill;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by vkluc_000 on 05.06.2015.
 */
public class Parent {
    Cursor cursor;
    DB db;
    private String plogin, pemail, ppassword;
    private int id;

    Parent(Context context, String login,String password) {

        db = new DB(context);
        db.open();
        cursor = db.getParent(login, password);

        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            id = (int)cursor.getLong(cursor.getColumnIndex(DB.C_IDP));
            //id = cursor.getColumnIndex(DB.C_IDP);
            plogin = cursor.getString(cursor.getColumnIndex(DB.C_LOGIN));
            pemail = cursor.getString(cursor.getColumnIndex(DB.C_EMAIL));
            ppassword = cursor.getString(cursor.getColumnIndex(DB.C_PASSWORD));
        }
    }

    public String getPlogin() {
        return plogin;
    }

    public void setPlogin(String plogin) {
        this.plogin = plogin;
    }

    public String getPemail() {
        return pemail;
    }

    public void setPemail(String pemail) {
        this.pemail = pemail;
    }


    public String getPpassword() {
        return ppassword;
    }
    public void setPpassword(String ppassword) {
        this.ppassword = ppassword;
    }

    public void addParent(){
        if(plogin != null)
        db.addParent(plogin, pemail, ppassword);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
