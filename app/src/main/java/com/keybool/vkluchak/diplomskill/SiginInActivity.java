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

    public static final String USER = "user";
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    EditText etUserName, etPassword;
    View mSubmit;
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

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();

    }

    void signIn(String userName, String password){

        String storedPassword=db.getParentEntry(userName);
        String storedChildPassword=db.getChildEntry(userName);

        if(password.equals(storedPassword)) {
            //cursor = db.idintifyChild(userName, password);
            //if(cursor.getCount() != 0){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("flag", false);
            intent.putExtra(USERNAME, userName);
            intent.putExtra(PASSWORD, password);
            startActivity(intent);

        } else if (password.equals(storedChildPassword)) {
        //}else if((db.idintifyParent(userName, password)).getCount() != 0){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("flag", true);
            intent.putExtra(USERNAME, userName);
            intent.putExtra(PASSWORD, password);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
        //cursor.close();
    }

    public void onclick(View view){
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("flag", false);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}
