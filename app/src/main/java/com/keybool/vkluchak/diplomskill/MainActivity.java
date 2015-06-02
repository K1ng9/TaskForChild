package com.keybool.vkluchak.diplomskill;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;


public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    DB db;
    SimpleCursorAdapter scAdapter;
    ListView lvList;
    private TextView tvDisplayTime;
    Button btnAdd;
    Cursor cursor;

    private TimePicker timePicker1;

    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        lvList = (ListView) findViewById(R.id.lvList);
        tvDisplayTime = (TextView) findViewById(R.id.tvTime);


        tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
                .append(pad(minute)));

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();
        adapterListView();
    }

     public void onclick(View v){
        switch(v.getId()) {
            case R.id.btnAdd:
                Intent intent = new Intent(this, AddTask.class);
                startActivity(intent);
                break;
        }
    }


    public void adapterListView(){

        //----------------------------------------------------------------------------
        // формируем столбцы сопоставления
        String[] from = new String[]{DB.C_NAME, DB.C_AWARD, DB.C_TIME };
        int[] to = new int[]{R.id.tvText1, R.id.tvText2, R.id.tvText3};

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.item_of_list, null, from, to, 0);// null-cursor
        lvList.setAdapter(scAdapter);
        // добавляем контекстное меню к списку
        registerForContextMenu(lvList);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);
        //------------------------------------------------------------------------------
    }


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    // -------------------------LOADER  -----------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        scAdapter.swapCursor(null);
    }

    static class MyCursorLoader extends CursorLoader {
        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();

            return cursor;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //adapterListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }
}
