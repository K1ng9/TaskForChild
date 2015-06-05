package com.keybool.vkluchak.diplomskill;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by vkluc_000 on 05.06.2015.
 */
public class Register extends Activity{
    DB db;

    EditText etLogin, etEmail, etPassword, etCLogin, etCEmail, etCPassword;
    //Button btnAddParent, btnAddChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCLogin = (EditText) findViewById(R.id.etCLogin);
        etCEmail = (EditText) findViewById(R.id.etCEmail);
        etCPassword = (EditText) findViewById(R.id.etCPassword);

        //btnAddChild = (ImageButton) findViewById(R.id.btnAddChild);
        //btnAddParent = (Button) findViewById(R.id.btnAddParent);

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();



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
                db.addParent(login, email, password);
                break;
            case R.id.btnAddChild:
                db.addChild(clogin, cemail, cpassword, 0, 0, 0);

        }
    }
}
