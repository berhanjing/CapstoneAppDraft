package com.example.capstoneappdraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LeaderBoardActivity extends AppCompatActivity {
    final String TAG = "LeaderBoardActivity";
    BottomNavigationView bottomNavigationView;
    LinearLayout AllScores;
    TextView Username;
    TextView TotalPoints;
    TextView NumberPlacing;
    ImageView HexagonColor;
    int i = 1;
    Button ScoreboardButton;
    ImageView profilePic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        AllScores = findViewById(R.id.leaderboard_scores);
        ScoreboardButton = findViewById(R.id.score_board_button);

        bottomNavigationView = findViewById(R.id.navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_scoreboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(LeaderBoardActivity.this, HomepageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_maintenance:
                        startActivity(new Intent(LeaderBoardActivity.this, MaintenanceRecordsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_scoreboard:
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(LeaderBoardActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        ReadLeaderboardScores();

        ScoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LeaderBoardActivity.this, ScoreboardActivity.class));
            }
        });



    }

    private void ReadLeaderboardScores() {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID



        //get all documents in the "Maintenance Record" collection
        mFirestore.collection("leaderboard").orderBy("Score", Direction.DESCENDING).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                final View view = getLayoutInflater().inflate(R.layout.each_leaderboardscore, AllScores, false);
                                HexagonColor = view.findViewById(R.id.hexagon);
                                Username = view.findViewById(R.id.user_name);
                                TotalPoints = view.findViewById(R.id.points);
                                NumberPlacing = view.findViewById(R.id.placing);
                                profilePic = view.findViewById(R.id.profile_pic);


                                NumberPlacing.setText(Integer.toString(i));
                                TotalPoints.setText(Long.toString(document.getLong("Score")) + " Points");
                                Username.setText(document.getString("Name"));
                                if (i == 1) {
                                    HexagonColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD15A")));
                                }
                                if (i == 2) {
                                    HexagonColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DCDFE9")));
                                } else if (i == 3) {
                                    HexagonColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#994343")));
                                }
                                i++;

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                assert user != null;
                                String currentUserID = user.getUid();
                                String userID = document.getString("User ID");
                                if (currentUserID.equals(userID)) {
                                    if (user.getPhotoUrl() != null){
                                        GlideApp.with(LeaderBoardActivity.this).load(user.getPhotoUrl()).into(profilePic);
                                    }
                                }
                                else{
                                    StorageReference mStorageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(userID + ".jpeg");
                                    Log.e("TAG TAG TAG", mStorageReference.toString());
                                    GlideApp.with(LeaderBoardActivity.this).load(mStorageReference).into(profilePic);
                                }
                                AllScores.addView(view);


                            }
                        } else {
                            Log.d(TAG, "error getting documents: ", task.getException());
                        }
                    }
                });

    }


}