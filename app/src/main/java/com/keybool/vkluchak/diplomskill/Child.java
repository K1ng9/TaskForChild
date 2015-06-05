package com.keybool.vkluchak.diplomskill;

import android.database.Cursor;
import android.util.Log;

/**
 * Created by vkluc_000 on 05.06.2015.
 */
public class Child {
    private String clogin, cemail, cpassword,  parent;
    private int id, coins, levl;

    Cursor cursor;

    DB db;

    Child(String login,String password) {

        cursor = db.getChild(login, password);

        id = cursor.getInt(cursor.getColumnIndex(DB.C_IDC));
        clogin = cursor.getString(cursor.getColumnIndex(DB.C_CLOGIN));
        cemail = cursor.getString(cursor.getColumnIndex(DB.C_CEMAIL));
        cpassword = cursor.getString(cursor.getColumnIndex(DB.C_CPASSWORD));
        coins = cursor.getInt(cursor.getColumnIndex(DB.C_COINS));
        levl = cursor.getInt(cursor.getColumnIndex(DB.C_LEVL));
    }

    public String getClogin() {
        return clogin;
    }

    public void setClogin(String clogin) {
        this.clogin = clogin;
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getCpassword() {
        return cpassword;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getLevl() {
        return levl;
    }

    public void setLevl(int levl) {
        this.levl = levl;
    }
}
