package com.example.capstoneappdraft;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CreateMaintenanceRecordActivity extends AppCompatActivity {
    Button submitButton;
    EditText titleInput;
    EditText dateInput;
    EditText timeInput;
    EditText descriptionInput;
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "MaintenanceRecord";

    private static final String TITLE_KEY = "Title";
    private static final String DATE_KEY = "Date";
    private static final String TIME_KEY = "Time";
    private static final String DESC_KEY = "Description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_form);
        submitButton = findViewById(R.id.submit_button);
        titleInput = findViewById(R.id.title_field);
        dateInput = findViewById(R.id.date_field);
        timeInput = findViewById(R.id.time_field);
        descriptionInput = findViewById(R.id.description_input);

//        bottomNavigationView = findViewById(R.id.navigator);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_maintenance);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch(menuItem.getItemId()){
//                    case R.id.navigation_maintenance:
//                        return true;
//                    case R.id.navigation_home:
//                        startActivity(new Intent(CreateMaintenanceRecordActivity.this, HomepageActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.navigation_scoreboard:
//                        startActivity(new Intent(CreateMaintenanceRecordActivity.this, ScoreboardActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.navigation_profile:
//                        startActivity(new Intent(CreateMaintenanceRecordActivity.this, ProfileActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                }
//                return false;
//            }
//        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleString = titleInput.getText().toString();
                String dateString = dateInput.getText().toString();
                String timeString = timeInput.getText().toString();
                String descriptionString = descriptionInput.getText().toString();

                checkDataEntered();
                writeNewRecord(titleString, dateString, timeString, descriptionString);
                Intent intent = new Intent(CreateMaintenanceRecordActivity.this, MaintenanceRecordsActivity.class);
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

    private void writeNewRecord(String title, String date, String time, String description){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        Map < String, Object > newRecord = new HashMap < > ();
        newRecord.put(TITLE_KEY, title);
        newRecord.put(DATE_KEY, date);
        newRecord.put(TIME_KEY, time);
        newRecord.put(DESC_KEY,description);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        mFirestore.collection("Users").document(userID).collection("Maintenance Records").document().set(newRecord)
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateMaintenanceRecordActivity.this, "Record added",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateMaintenanceRecordActivity.this, "ERROR" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }


}
