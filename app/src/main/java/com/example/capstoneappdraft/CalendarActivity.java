package com.example.capstoneappdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CalendarActivity extends AppCompatActivity {
    private  static final String TAG = "CalendarActivity";
    Long getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);
        CalendarView calendar = findViewById(R.id.calendar);
        //getDate = calendar.getDate();
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                //set notification reminder for that day
                //the month in calendar is indexed from 0 - 11 so need to + 1
                String correctMonth = Integer.toString(Integer.valueOf(month) + 1);
                String date = dayOfMonth + "/" + correctMonth + "/" + year ;
                String formattedDate = checkDateFormat(date);

                //set date in maintenance record
                Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd:" + formattedDate);
                Intent intent = new Intent();
                intent.putExtra("dateFromCalendar", formattedDate);
                setResult(Activity.RESULT_OK,intent);
                finish();

            }
        });
    }

    public String checkDateFormat(String date){
        String[] splitdate = date.split("/");
        String splitday = splitdate[0];
        String splitmonth = splitdate[1];
        String splityear = splitdate[2];
        if (splitday.length() == 1){
            splitday = "0" + splitday;
        }
        if (splitmonth.length() == 1){
            splitmonth = "0" + splitmonth;
        }
        String formattedDate = splitday + "/" + splitmonth + "/" + splityear;
        return formattedDate;
    }
}
