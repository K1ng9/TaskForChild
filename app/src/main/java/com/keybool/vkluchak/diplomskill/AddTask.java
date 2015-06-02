package com.keybool.vkluchak.diplomskill;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Calendar;

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
    EditText etName, etAward, etTime, etStar, etEndTime, etStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask);

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setIs24HourView(true);
        datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
        deleteYear();

        btnAddTask =(ImageButton) findViewById(R.id.btnAddTask);

        etName = (EditText) findViewById(R.id.etName);
        etAward = (EditText) findViewById(R.id.etAward);
        etTime = (EditText) findViewById(R.id.etTime);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

    }

    public void onclick(View v) {
        String name = etName.getText().toString();
        int award = Integer.parseInt(etAward.getText().toString());
        //String time = etTime.getText().toString();
        String time = pad(timePicker1.getCurrentHour())+":" + pad(timePicker1.getCurrentMinute()) + " | "
                    + pad(datePicker1.getDayOfMonth()) + "." +pad(datePicker1.getMonth()) + "";


        switch (v.getId()) {
            case R.id.btnAddTask:
                //time =timePickerListener;
                db.addTask(name, award, time, 0, 0);
                Log.d(LOG_TAG, "----Done----");
                //onDestroy();
                break;
        }
    }

    void  deleteYear(){//пашет
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
        // закрываем подключение при выходе
        db.close();
    }
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}

