package com.example.capstoneappdraft;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "ScoreboardActivity";
    TextView currentPoints;
    TextView currentRanking;
    TextView Name;
    Button leaderboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        currentPoints = findViewById(R.id.points);
        currentRanking = findViewById(R.id.ranking);
        Name = findViewById(R.id.name);
        leaderboardButton = findViewById(R.id.leader_board_button);

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreboardActivity.this, LeaderBoardActivity.class);
                startActivity(intent);
            }
        });

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

        ReadScores();
        getRanking();
        getName();
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




