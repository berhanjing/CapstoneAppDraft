package com.example.capstoneappdraft;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

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
    }

    private void ReadScores() {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
    }
}

//        //get all documents in the "Maintenance Record" collection
//        mFirestore.collection("leaderboard").orderBy("score", Query.Direction.DESCENDING).get().
//                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
//                                Username = view.findViewById(R.id.user_name);
//                                TotalPoints = view.findViewById(R.id.points);
//                                NumberPlacing = view.findViewById(R.id.placing);
//
//                                NumberPlacing.setText(Integer.toString(i));
//                                TotalPoints.setText(Long.toString(document.getLong("score")) + " Points");
//                                Username.setText(document.getString("name"));
//                                if (i == 1){
//                                    HexagonColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD15A")));
//                                }
//                                if (i == 2){
//                                    HexagonColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DCDFE9")));
//                                }
//                                else if (i == 3){
//                                    HexagonColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#994343")));
//                                }
//
//                                AllScores.addView(view);
//                                i++;
//
//                            }
//                        } else {
//                            Log.d(TAG, "error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }

