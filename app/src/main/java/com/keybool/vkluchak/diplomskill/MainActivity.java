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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    private static final int CM_DELETE_ID = 1;
    private static final int CM_DOTASK_ID = 2;
    private static final int CM_DONETASK_ID = 3;
    private static final int CM_NOT_DONETASK_ID = 4;


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

        cursor = null;


        tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
                .append(pad(minute)));

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();
        adapterListView();
        //cursor = db.sortByTime();
        //scAdapter.swapCursor(cursor);
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
    //----------------------Контекстное меню ------------------------------
    public boolean onContextItemSelected(MenuItem item) {
        // получаем из пункта контекстного меню данные по пункту списка
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case CM_DELETE_ID:
                // извлекаем id записи и удаляем соответствующую запись в БД
                db.delTask(acmi.id);
                break;
            case CM_DOTASK_ID:
                db.updateTaskStatus(acmi.id);
                break;
            case CM_DONETASK_ID:
                db.updateTaskDone(acmi.id);
                break;
            case CM_NOT_DONETASK_ID:
                db.updateTaskNotDone(acmi.id);
                break;
        }
        // получаем новый курсор с данными
        getSupportLoaderManager().getLoader(0).forceLoad();
        return super.onContextItemSelected(item);

    }
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
        menu.add(0, CM_DOTASK_ID, 0, "do task");
        menu.add(0, CM_DONETASK_ID, 0, "done task");
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().getLoader(0).forceLoad();
        //adapterListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }
}
