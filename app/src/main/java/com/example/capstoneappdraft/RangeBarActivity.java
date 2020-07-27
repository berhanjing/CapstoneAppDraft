package com.example.capstoneappdraft;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class RangeBarActivity extends AppCompatActivity {
    Button button;
    //RangeBar
    SeekBar SeekBar;
    TextView SeekBarNum;
    int min = 0, max = 100, current = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        button = findViewById(R.id.button);
        //RangeBar code here
        SeekBar = findViewById(R.id.seekbar);
        SeekBarNum = findViewById(R.id.num);

        SeekBar.setProgress(max - min);
        SeekBar.setProgress(current - min);
        SeekBarNum.setText("" + current);

        SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                current = progress + min;
                SeekBarNum.setText("" + current);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        //Start Button code here
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeekBar.setVisibility(View.GONE);
                SeekBarNum.setVisibility(View.GONE);

            }
        });

    }
}
