package com.example.capstoneappdraft;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "ScoreboardActivity";
    TextView currentPoints;
    TextView currentRanking;
    TextView Name;
    //Button leaderboardButton;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        currentPoints = findViewById(R.id.points);
        currentRanking = findViewById(R.id.ranking);
        Name = findViewById(R.id.name);
        //leaderboardButton = findViewById(R.id.leader_board_button);
        profilePic = findViewById(R.id.profile_pic);

//        leaderboardButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ScoreboardActivity.this, LeaderBoardActivity.class);
//                startActivity(intent);
//            }
//        });

        bottomNavigationView = findViewById(R.id.navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_scoreboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navigation_home:
                        startActivity(new Intent(ScoreboardActivity.this, HomepageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_maintenance:
                        startActivity(new Intent(ScoreboardActivity.this, MaintenanceRecordsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_scoreboard:
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(ScoreboardActivity.this, ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getPhotoUrl() != null){
            Glide.with(this).load(user.getPhotoUrl()).into(profilePic);
        }

        ReadScores();
        getRanking();
        getName();

    }

    private static final String RIDERSCORE_KEY = "Singular Rider Score (NN)";
    private static final String TIMESTAMP_KEY = "Date";
    private static final String MAXSPEED_KEY = "Max Speed(KMH)";
    private static final String NUMHARSHBRAKING_KEY = "Number of Harsh Braking";
    private static final String TOTALDIST_KEY = "Total Distance(KM)";
    int maxSpeed;
    int numOfHarshBrakings;
    int totalDist;
    private int SingularRiderScore(Map histogramride){ //histogramride is the map for each ride
        //Mean Function
        float total = 0;
        float count = 0;
        float Mavg = 0;
        float Iavg = 0;
        float Eavg = 0;
        // Get keys and values
        for ( Object key : histogramride.keySet() ) {
            if (key == ("M")) {
                Integer[] MArray;
                MArray = (Integer[]) histogramride.get(key);
                // iterating over an array
                for (int index = 0; index < MArray.length; index++) {
                    // accessing each element of array
                    Integer i = MArray[index];
                    System.out.print("Element: " + i + " , Index: " + index );
                    total += (float) i*index;
                    count += index;
                }
                Mavg = total/count;
            }
            else if (key == ("I")){
                Integer[] IArray;
                IArray = (Integer[]) histogramride.get(key);
                // iterating over an array
                for (int index = 0; index < IArray.length; index++) {
                    // accessing each element of array
                    Integer i = IArray[index];
                    System.out.print("Element: " + i + " , Index: " + index );
                    total += (float) i*index;
                    count += index;
                }
                Iavg = total/count;
            }
            else if (key == ("E")){
                Integer[] EArray;
                EArray = (Integer[]) histogramride.get(key);
                // iterating over an array
                for (int index = 0; index < EArray.length; index++) {
                    // accessing each element of array
                    Integer i = EArray[index];
                    System.out.print("Element: " + i + " , Index: " + index );
                    total += (float) i*index;
                    count += index;
                }
                Eavg = total/count;
            }
        }

        //Call on Lookup Table
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        final int finalMavg = Math.round(Mavg);
        final int finalIavg = Math.round(Iavg);
        final int finalEavg = Math.round(Eavg);

        final Float[] Percentile = new Float[2];
        final Float[] Weights = new Float[2];

        final int[] SingularRiderScore = new int[0];

        //Get Lookup Table and Weights
        mFirestore.collection("Global_Parameters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()){
                   for (DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                       if (document.getId().equals("MIE_Scores")){
                           //Get Lookup Table for M, I and E
                           Array MTable = (Array) document.get("M_table");
                           Array ITable = (Array) document.get("I_table");
                           Array ETable = (Array) document.get("E_table");

                           Percentile[0] = (Float) Array.get(MTable, finalMavg); //Percentile[0] is Mavg
                           Percentile[1] = (Float) Array.get(ITable, finalIavg); //Percentile[1] is Iavg
                           Percentile[2] = (Float) Array.get(ETable, finalEavg); //Percentile[2] is Eavg
                       }
                       if (document.getId().equals("MIE_Weights")){
                           //Get M,I and E weights
                           Weights[0] = (Float) document.get("M_weight");
                           Weights[1] = (Float) document.get("I_weight");
                           Weights[2] = (Float) document.get("E_weight");
                       }
                        SingularRiderScore[0] = Math.round(100 - ((Weights[0]*Percentile[0]) - (Weights[1]*Percentile[1]) - (Weights[0]*Percentile[0])));
                   }
               } else {
                   Log.d(TAG, "error getting documents: ", task.getException());
               }
           }
       });
        int SingularRiderScoreVal = (int)Array.get(SingularRiderScore, 0);
        return SingularRiderScoreVal;

    }

    private void WriteTriptoDatabase(int SingularRiderScore, int maxSpeed, int totalDist, int numOfHarshBrakings){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();

        //getDate
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
        String strDate = formatter.format(date);
        Log.e(TAG, "Date Format with E, dd MMM yyyy HH:mm "+ strDate);

        //Write Singular Rider Score (non-normalized) as a Integer Value into Firestore
        Map < String, Object > newTrip = new HashMap< >();
        newTrip.put(RIDERSCORE_KEY, SingularRiderScore);
        newTrip.put(TIMESTAMP_KEY, strDate);
        newTrip.put(MAXSPEED_KEY, maxSpeed);
        newTrip.put(NUMHARSHBRAKING_KEY,numOfHarshBrakings);
        newTrip.put(TOTALDIST_KEY,totalDist);


        mFirestore.collection("Users").document(userID).collection("Past trips").document().set(newTrip)
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"Non-normalized Rider Score added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }


    private void ReadScores() {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        mFirestore.collection("Users").document(userID).collection("Scores").orderBy("Score", Query.Direction.DESCENDING)
                .limit(1).get().
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                        currentPoints.setText("Current Points: " + Long.toString(document.getLong("Score")));
                    }
                } else {
                    Log.d(TAG, "error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getRanking(){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        final String userID = user.getUid();
        DocumentReference docRef = mFirestore.collection("leaderboard").document(userID);
        mFirestore.collection("leaderboard").orderBy("Score", Query.Direction.DESCENDING).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<String> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                list.add(document.getId());
                            }
                            if(list.contains(userID)){
                                String rankingNumber = Integer.toString(list.indexOf(userID) + 1);
                                if (rankingNumber.contains("1")){
                                    currentRanking.setText("Current Ranking: " + rankingNumber + "st");
                                }
                                else if (rankingNumber.contains("2")){
                                    currentRanking.setText("Current Ranking: " + rankingNumber + "nd");
                                }
                                else if (rankingNumber.contains("3")){
                                    currentRanking.setText("Current Ranking: " + rankingNumber + "rd");
                                }
                                else{
                                    currentRanking.setText("Current Ranking: " + rankingNumber + "th");
                                }
                            }
                        } else {
                            Log.d(TAG, "error getting documents: ", task.getException());
                        }

                    }
                });

    }

    private void getName(){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        mFirestore.collection("Users").document(userID).collection("User Info").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                                Name.setText(document.getString("Name"));
                            }
                        } else {
                            Log.d(TAG, "error getting documents: ", task.getException());
                        }
                    }
                });
    }

}




