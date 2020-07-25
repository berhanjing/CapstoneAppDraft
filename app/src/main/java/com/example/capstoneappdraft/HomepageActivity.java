package com.example.capstoneappdraft;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HomepageActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView dateAndTime;
    TextView titleOfAlert;
    String date;
    String time;
    RelativeLayout PopupNotif;
    ImageButton PopupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        bottomNavigationView = findViewById(R.id.navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        PopupNotif = findViewById(R.id.popup_notif);
        PopupButton = findViewById(R.id.popup_button);


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
                        startActivity(new Intent(HomepageActivity.this, LeaderBoardActivity.class));
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
        PopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               PopupNotif.setVisibility(View.GONE);
            }
        });


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
                        if (checkTodayDate().equals(doc.get("Date").toString())) {
                            PopupNotif.setVisibility(View.VISIBLE);
                            titleOfAlert = findViewById(R.id.title_of_record);
                            dateAndTime = findViewById(R.id.date_time);
                            date = doc.get("Date").toString();
                            time = doc.get("Time").toString();
                            String fullDate = getDateInFull(date);
                            dateAndTime.setText(fullDate + ", " + time);
                            titleOfAlert.setText(doc.get("Title").toString());
                        }

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

    public String getDateInFull(String date){
        String[] splitdate = date.split("/");
        String day = splitdate[0];
        Integer month = Integer.valueOf(splitdate[1]);
        String year = splitdate[2];
        String monthString = new DateFormatSymbols().getMonths()[month - 1];
        String FullDateName = day + " " + monthString + " " + year;
        return FullDateName;
    }

    public String checkTodayDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

}
