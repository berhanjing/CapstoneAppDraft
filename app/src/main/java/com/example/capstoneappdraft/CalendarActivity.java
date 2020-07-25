package com.example.capstoneappdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.CalendarView;

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
//                Calendar c = Calendar.getInstance();
//                c.set(year, month, dayOfMonth);

                String date = dayOfMonth + "/" + month + "/"+ year ;
                //set date in maintenance record
                Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd:" + date);
                Intent intent = new Intent();
                intent.putExtra("dateFromCalendar", date);
                setResult(Activity.RESULT_OK,intent);
                finish();

            }
        });
    }
}
