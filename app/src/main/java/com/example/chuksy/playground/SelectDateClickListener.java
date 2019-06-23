package com.example.chuksy.playground;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chuksy on 5/27/19.
 */

public class SelectDateClickListener implements View.OnClickListener {

    public static interface SelectDateClickInterface {

        void retrieveSelectedDate(long selectedDate);

    }

    final Calendar calendar = Calendar.getInstance();

    private Activity theActivity;

    private int year = calendar.get(Calendar.YEAR);

    private int month = calendar.get(Calendar.MONTH);;

    private int day  = calendar.get(Calendar.DAY_OF_MONTH);;

    private int hour = calendar.get(Calendar.HOUR_OF_DAY);

    private int minute = calendar.get(Calendar.MINUTE);

    public DatePickerDialog datePicker;

    public TimePickerDialog timePickerDialog;


    public SelectDateClickListener(Activity activity){

        theActivity = activity;

    }

    public void setDate(int theYear, int theMonth, int theDay){

        year = theYear;

        month = theMonth;

        day = theDay;


    }

    private void setTime(int theHour, int theMinute){

        hour = theHour;

        minute = theMinute;

    }

    public int[] getDate(){

        int[] date = {year, month, day};

        return date;

    }

    public int[] getTime(){

        int[] time = {hour, minute};

        return time;

    }

    public long getTimeInMilliSeconds(){

        calendar.set(year, month, day, hour, minute);

        long dateInMilliSeconds = calendar.getTimeInMillis();

        return dateInMilliSeconds;

    }


    @Override
    public void onClick(View v) {

        Log.d("PLAY", "Select Date Clicked");

        datePicker();


    }

    private void datePicker(){

        // date picker dialog
        datePicker = new DatePickerDialog(theActivity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int theYear, int monthOfYear, int dayOfMonth) {

                        setDate(theYear, monthOfYear, dayOfMonth);

                        Log.d("PLAY", "Date Set - Year: " + year + " Month: " + month + " Day: " + day);

                        timePicker();

                    }
                }, year, month, day);

        datePicker.show();

    }

    private void timePicker(){

        // Launch Time Picker Dialog
        timePickerDialog = new TimePickerDialog(theActivity,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int theMinute) {

                        setTime(hourOfDay, theMinute);

                        Log.d("PLAY", "Time Set - Hour: " + hour + " Minute: " + minute);

                        SelectDateClickInterface retrievable = (SelectDateClickInterface) theActivity;

                        retrievable.retrieveSelectedDate(getTimeInMilliSeconds());


                    }
                }, hour, minute, false);
        timePickerDialog.show();

    }

}
