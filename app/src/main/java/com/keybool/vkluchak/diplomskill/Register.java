package com.keybool.vkluchak.diplomskill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by vkluc_000 on 05.06.2015.
 */
public class Register extends Activity{
    public static final String FLAG = "flag";
    public static final String ID = "id";
    DB db;
    boolean flag;
    LinearLayout ll1,ll2,ll3,ll4,ll5,ll6;
    int idParent;
    EditText etLogin, etEmail, etPassword, etCLogin, etCEmail, etCPassword;
    //Button btnAddParent, btnAddChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        flag = getIntent().getBooleanExtra(FLAG, false);
        idParent = getIntent().getIntExtra(ID, 0);

        ll1 =(LinearLayout) findViewById(R.id.ll1);
        ll2 =(LinearLayout) findViewById(R.id.ll2);
        ll3 =(LinearLayout) findViewById(R.id.ll3);
        ll4 =(LinearLayout) findViewById(R.id.ll4);
        ll5 =(LinearLayout) findViewById(R.id.ll5);
        ll6 =(LinearLayout) findViewById(R.id.ll6);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCLogin = (EditText) findViewById(R.id.etCLogin);
        etCEmail = (EditText) findViewById(R.id.etCEmail);
        etCPassword = (EditText) findViewById(R.id.etCPassword);

        if (flag) {
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
        }else{
            ll4.setVisibility(View.GONE);
            ll5.setVisibility(View.GONE);
            ll6.setVisibility(View.GONE);
        }
        //btnAddChild = (ImageButton) findViewById(R.id.btnAddChild);
        //btnAddParent = (Button) findViewById(R.id.btnAddParent);

        // откриваем подлючение к ДБ
        //db = new DB(this);
        //db.open();



    }
    public void onclick(View v) {
        String login = etLogin.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String clogin = etCLogin.getText().toString();
        String cemail = etCEmail.getText().toString();
        String cpassword = etCPassword.getText().toString();
        switch (v.getId()) {
            case R.id.btnAddParent:
                if (!flag) {
                    Parent parent = new Parent(this, login, password);
                    parent.setPlogin(login);
                    parent.setPemail(email);
                    parent.setPpassword(password);
                    parent.addParent();
                }else{
                        Child child = new Child(this, clogin, cpassword);
                        child.setClogin(clogin);
                        child.setCemail(cemail);
                        child.setCpassword(cpassword);
                        child.setParent(idParent);
                        child.addChild();
                    }

                Intent intent = new Intent(this, SiginInActivity.class);
                startActivity(intent);
                break;

        }
    }

}
