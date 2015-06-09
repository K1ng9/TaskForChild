package com.keybool.vkluchak.diplomskill;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;


public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 1;
    private static final int CM_DOTASK_ID = 2;
    private static final int CM_DONETASK_ID = 3;
    private static final int CM_NOT_DONETASK_ID = 4;
    DB db;
    SimpleCursorAdapter scAdapter;
    ListView lvList;
    //private TextView tvDisplayTime;
    Button btnAdd;
    Cursor cursor;
    Child child;
    Parent parent;
    boolean flag;
    String password, userName;
    TextView tvLogin, tvCoins, tvDisplayTime, tvChildName;
    ProgressBar pbLevl;
    LinearLayout llmain2;
    private int hour;
    private int minute;

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        llmain2 = (LinearLayout) findViewById(R.id.llmain2);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        lvList = (ListView) findViewById(R.id.lvList);
        tvDisplayTime = (TextView) findViewById(R.id.tvTime);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvCoins = (TextView) findViewById(R.id.tvCoins);
        tvChildName = (TextView) findViewById(R.id.tvChildName);
        pbLevl = (ProgressBar) findViewById(R.id.pbLevl);

        cursor = null;

        flag = getIntent().getBooleanExtra(Register.FLAG, false);
        userName = getIntent().getStringExtra(SiginInActivity.USERNAME);
        password = getIntent().getStringExtra(SiginInActivity.PASSWORD);

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();

        if (flag) {
            child = new Child(this, userName, password);
            tvLogin.setText(child.getClogin());
            tvCoins.setText(" " + child.getCoins() + " ");
            llmain2.setVisibility(View.GONE);
        }
        else {
            parent = new Parent(this, userName, password);
            tvLogin.setText(parent.getPlogin());
            pbLevl.setVisibility(View.GONE);
            tvChildName.setText(findChildOfParent());

        }

        tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
                .append(pad(minute)));

        //cursor.close();
        cursor = null;
        //cursor = db.sortByTime();
        adapterListView();
        //scAdapter.swapCursor(cursor);
    }
    public String findChildOfParent(){
        cursor = db.getChildByParent(parent.getId());
        if(!(cursor.getCount() < 1) ){
            cursor.moveToFirst();
            child = new Child(this, cursor.getString(cursor.getColumnIndex(DB.C_CLOGIN)),
                                    cursor.getString(cursor.getColumnIndex(DB.C_CPASSWORD)));
            return cursor.getString(cursor.getColumnIndex(DB.C_CLOGIN));
        }else
            return "No Child";
    }

     public void onclick(View v){
        switch(v.getId()) {
            case R.id.btnAdd:
                Intent intent = new Intent(this, AddTask.class);
                intent.putExtra(Register.ID, child.getId());
                startActivity(intent);
                break;
            case R.id.btnAddChild:
                intent = new Intent(this, Register.class);
                intent.putExtra(Register.FLAG, true);
                intent.putExtra(Register.ID, parent.getId());
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
        scAdapter = new SimpleCursorAdapter(this, R.layout.item_of_list, cursor, from, to, 0);// null-cursor
        lvList.setAdapter(scAdapter);
        // добавляем контекстное меню к списку
        registerForContextMenu(lvList);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);
        //------------------------------------------------------------------------------
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

    //----------------------Контекстное меню ------------------------------
    public boolean onContextItemSelected(MenuItem item) {
        // получаем из пункта контекстного меню данные по пункту списка
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
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
        //user = getIntent().getIntExtra(SiginInActivity.USER, 0);
        getSupportLoaderManager().getLoader(0).forceLoad();
        //adapterListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        cursor.close();
        db.close();
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
}
