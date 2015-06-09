package com.keybool.vkluchak.diplomskill;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by vkluc_000 on 28.05.2015.
 */
public class AddTask extends Activity  {

    DB db;
    final String LOG_TAG = "myLogs";
    int hour, minute;
    TimePicker timePicker1;
    DatePicker datePicker1;
    LinearLayout ll3;

    static final int TIME_DIALOG_ID = 999;
    ImageButton btnAddTask;
    int idChild;
    EditText etName, etAward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask);

        db = new DB(this);
        db.open();

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setIs24HourView(true);
        datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
        deleteYear();

        btnAddTask =(ImageButton) findViewById(R.id.btnAddTask);

        etName = (EditText) findViewById(R.id.etName);
        etAward = (EditText) findViewById(R.id.etAward);
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(etAward.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        idChild = getIntent().getIntExtra(Register.ID, 0);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

    }
    private Calendar createAsCalendar()
    {
        // Convert a DatePicker and TimePicker control in to a calendar object. The time is
        // stored in UTC minutes and unaffected by time zone
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, datePicker1.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, datePicker1.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, timePicker1.getCurrentHour());
        cal.set(Calendar.MINUTE, timePicker1.getCurrentMinute());
        cal.set(Calendar.SECOND, 0);

        return cal;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar date = createAsCalendar();
        return dateFormat.format(date.getTime());
    }
    public void onclick(View v) {
        String name = etName.getText().toString();
        int award = Integer.parseInt(etAward.getText().toString());

        String str = getDateTime();
        switch (v.getId()) {
            case R.id.btnAddTask:
                //time =timePickerListener;
                db.addTask(name, award, str, 0, 0, idChild);// id child need
                Log.d(LOG_TAG, "----Done----");
                //onDestroy();
                break;
        }
    }

    void  deleteYear(){
        DatePicker dp = findDatePicker(ll3);
        if (dp != null) {
            try {
                Field f[] = dp.getClass().getDeclaredFields();
                for (Field field : f) {
                    if (field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner")){
                        field.setAccessible(true);
                        ((View) field.get(dp)).setVisibility(View.GONE);
                    }
                }
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                Log.d("ERROR", e.getMessage());
            }
        }
    }

    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}

