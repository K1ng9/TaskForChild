package com.keybool.vkluchak.diplomskill;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by vkluc_000 on 05.06.2015.
 */
public class Child {
    Cursor cursor;
    DB db;
    private String clogin, cemail, cpassword;
    private int id, coins, levl, parent;
    Child(Context context, String login,String password) {

        db = new DB(context);
        db.open();
        cursor = db.getChild(login, password);

        cursor.moveToFirst();
        if(cursor.getCount() != 0){
            id = (int) cursor.getLong(cursor.getColumnIndex(DB.C_IDC));
            clogin = cursor.getString(cursor.getColumnIndex(DB.C_CLOGIN));
            cemail = cursor.getString(cursor.getColumnIndex(DB.C_CEMAIL));
            cpassword = cursor.getString(cursor.getColumnIndex(DB.C_CPASSWORD));
            coins = cursor.getInt(cursor.getColumnIndex(DB.C_COINS));
            levl = cursor.getInt(cursor.getColumnIndex(DB.C_LEVL));
        }
    }
    public void addChild(){
        if(clogin != null)
            db.addChild(clogin, cemail, cpassword, parent);

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

    public String getCpassword() {
        return cpassword;
    }
    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
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
