package com.example.capstoneappdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class TimePickerActivity extends AppCompatActivity {
    TimePicker picker;
    Button timeButton;
    private  static final String TAG = "TimePickerActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timepicker_view);

        picker = findViewById(R.id.timePicker1);
        timeButton = findViewById(R.id.time_button);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = picker.getHour();
                    minute = picker.getMinute();
                }
                else {
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                }

                else{
                    am_pm="AM";
                }
                if (minute < 10) {
                    String minuteString = "0" + Integer.toString(minute);
                    String time = + hour +":"+ minuteString +" "+ am_pm;
                    Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd: " + time);
                    Intent intent = new Intent();
                    intent.putExtra("setTime", time);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
                else {
                    String time = +hour + ":" + minute + " " + am_pm;
                    Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd: " + time);
                    Intent intent = new Intent();
                    intent.putExtra("setTime", time);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }

            }
        });

    }


}
