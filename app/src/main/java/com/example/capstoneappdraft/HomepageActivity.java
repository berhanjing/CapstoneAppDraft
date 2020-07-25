package com.example.capstoneappdraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class HomepageActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView dateAndTime;
    TextView titleOfAlert;
    String date;
    String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        bottomNavigationView = findViewById(R.id.navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        LinearLayout onePastTrip = findViewById(R.id.pasttrip_content);
        View view = getLayoutInflater().inflate(R.layout.past_trip, onePastTrip,false);
        onePastTrip.addView(view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navigation_maintenance:
                        startActivity(new Intent(HomepageActivity.this, MaintenanceRecordsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_home:
                        return true;
                    case R.id.navigation_scoreboard:
                        startActivity(new Intent(HomepageActivity.this, ScoreboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(HomepageActivity.this, ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        checkForMaintenanceNotif();

    }

    public void checkForMaintenanceNotif(){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        //get all documents in the "Maintenance Record" collection
        mFirestore.collection("Users").document(userID).collection("Maintenance Records").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {
                    for(final DocumentSnapshot doc: task.getResult()){
                        //View view = getLayoutInflater().inflate(R.layout.each_maintenancerecord, oneRecord,false);
                        titleOfAlert = findViewById(R.id.title_of_record);
                        dateAndTime = findViewById(R.id.date_time);
                       // titleRecord.setText(doc.get("Title").toString());
                        date = doc.get("Date").toString();
                        time = doc.get("Time").toString();
                        dateAndTime.setText(date + ", " + time);
                        //timeRecord.setText(doc.get("Time").toString());

                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public void checkMonth(){

    }

}
