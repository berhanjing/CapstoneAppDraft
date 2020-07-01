package com.example.capstoneappdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.CalendarView;



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
                String date = dayOfMonth + "/" + month + "/"+ year ;
                Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd:" + date);
                Intent intent = new Intent();
                intent.putExtra("dateFromCalendar", date);
                setResult(Activity.RESULT_OK,intent);
                finish();

            }
        });
    }
}
