package com.keybool.vkluchak.diplomskill;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by vkluc_000 on 05.06.2015.
 */
public class SiginInActivity extends Activity {

    EditText etUserName, etPassword;
    View mSubmit;

    public static final String USER = "user";
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    DB db;

    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.siginin);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        mSubmit = findViewById(R.id.mSubmit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String userName = etUserName.getText().toString();
                final String password = etPassword.getText().toString();

                signIn(userName, password);
            }
        });


    }

    void signIn(String userName, String password){

        cursor = db.idintifyChild(userName, password);
        if(cursor != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USER, 1);
            intent.putExtra(USERNAME, userName);
            intent.putExtra(PASSWORD, password);
            startActivity(intent);

        }else if((cursor = db.idintifyParent(userName, password))!= null){

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USER, 0);
            intent.putExtra(USERNAME, userName);
            intent.putExtra(PASSWORD, password);
            startActivity(intent);
        }else
            Toast.makeText(this, "нету такого пользователя", Toast.LENGTH_LONG).show();
        cursor.close();
    }
    public void onclick(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}
