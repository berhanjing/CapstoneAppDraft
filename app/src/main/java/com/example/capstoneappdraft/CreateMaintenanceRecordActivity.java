package com.example.capstoneappdraft;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateMaintenanceRecordActivity extends AppCompatActivity {
    Button submitButton;
    EditText titleInput;
    EditText dateInput;
    EditText timeInput;
    LinearLayout MaintenanceRecordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_form);
        submitButton = findViewById(R.id.submit_button);
        final String titleString = titleInput.getText().toString();
        String dateString = dateInput.getText().toString();
        String timeString = timeInput.getText().toString();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDataEntered();
                Intent intent = new Intent(CreateMaintenanceRecordActivity.this, MaintenanceRecordsActivity.class);
                intent.putExtra("title",titleString);
                startActivity(intent);
                finish();
            }
        });

    }

    void checkDataEntered() {

        if (isEmpty(titleInput)) {
            Toast t = Toast.makeText(this, "You must enter title!", Toast.LENGTH_SHORT);
            t.show();
        }

        if (isEmpty(dateInput)) {
            dateInput.setError("Set date!");
        }
        if (isEmpty(timeInput)) {
            timeInput.setError("Set a time!");
        }

    }

    boolean isEmpty (EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

}
