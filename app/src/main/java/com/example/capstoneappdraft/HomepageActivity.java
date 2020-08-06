package com.example.capstoneappdraft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.time.LocalDate;
import java.time.Month;


public class HomepageActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView dateAndTime;
    TextView titleOfAlert;
    String date;
    String time;
    RelativeLayout PopupNotif;
    ImageButton PopupButton;
    Button StartTripButton;
    TextView TotalDist;
    TextView MaxSpeed;
    TextView NumHarshBrakings;
    TextView DateofTrip;
    TextView TripScore;
    TextView LastTripScore;
    private static final String TAG = "HOMEPAGEACTIVITY";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        bottomNavigationView = findViewById(R.id.navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        PopupNotif = findViewById(R.id.popup_notif);
        PopupButton = findViewById(R.id.popup_button);
        StartTripButton = findViewById(R.id.start_trip_button);

        TotalDist = findViewById(R.id.total_dist);
        MaxSpeed = findViewById(R.id.max_speed);
        NumHarshBrakings = findViewById(R.id.harsh_brakings_num);
        LastTripScore = findViewById(R.id.lasttrip_score);


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

        StartTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, RangeBarActivity.class);
                startActivity(intent);
            }
        });

        LastTrip();
        CheckPastTripsInCurrentMonth();

    }

    public void LastTrip(){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        mFirestore.collection("Users").document(userID).collection("Past trips").orderBy("Timestamp", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                        NumHarshBrakings.setText(document.getLong("Number of Harsh Braking").toString());
                        MaxSpeed.setText(Long.toString(Math.round(document.getLong("Max Speed(KMH)"))));
                        TotalDist.setText(document.getDouble("Total Distance(KM)").toString());
                        LastTripScore.setText(document.getLong("Final Ride Score (Normalized)").toString());
                    }
                } else {
                    Log.d("TAG", "error getting documents: ", task.getException());
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CheckPastTripsInCurrentMonth(){
        //Getting the current month
        LocalDate currentdate = LocalDate.now();
        Month currentMonth = currentdate.getMonth();
        System.out.println("Current month: "+ currentMonth);
        String stringMonth = currentMonth.toString();

        //Change month to First Letter Uppercase, the rest Lowercase
        String CurrentMonthLC = stringMonth.toLowerCase();
        final String CurrentMonthFin = CurrentMonthLC.substring(0, 1).toUpperCase() + CurrentMonthLC.substring(1);
        //get first 3 letters of month
        final String month = CurrentMonthFin.substring(0, 3);
        Log.e(TAG, month);

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();


        mFirestore.collection("Users").document(userID).collection("Past trips").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                        String date = document.getString("Date");
                        Log.e(TAG, date);
                        if (date.contains(month)){
//                            String[] splitString = date.split("\\s+");
//                            String DateString = splitString[2] + " " + splitString[1] + " , " + splitString[0];
                            String tripScore = document.getLong("Final Ride Score (Normalized)").toString();
                            LinearLayout onePastTrip = findViewById(R.id.pasttrip_content);
                            View view = getLayoutInflater().inflate(R.layout.past_trip, onePastTrip,false);
                            DateofTrip = view.findViewById(R.id.date_of_trip);
                            TripScore = view.findViewById(R.id.trip_score);
                            TripScore.setText(tripScore);
                            DateofTrip.setText(date);

                            onePastTrip.addView(view);

                        }
                    }
                } else {
                    Log.d("TAG", "error getting documents: ", task.getException());
                }
            }
        });

//        mFirestore.collection("Users").document(userID).collection("Past trips").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
//                        Date date = document.getTimestamp("Date").toDate();
//                        Log.e(TAG, date.toString());
//                        if (date.toString().contains(month)){
//                            //change this line according to field name
//                            String tripScore = document.getLong("Final Ride Score (Normalized)").toString();
//                            LinearLayout onePastTrip = findViewById(R.id.pasttrip_content);
//                            View view = getLayoutInflater().inflate(R.layout.past_trip, onePastTrip,false);
//                            TripScore = view.findViewById(R.id.trip_score);
//                            TripScore.setText(tripScore);
//
//                            onePastTrip.addView(view);
//
//                        }
//                    }
//                } else {
//                    Log.d("TAG", "error getting documents: ", task.getException());
//                }
//            }
//        });



    }

    public void checkForMaintenanceNotif(){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

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
