package com.example.capstoneappdraft;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    BottomNavigationView bottomNavigationView;
    Button RebatesButton;
    TextView RebateValue;
    TextView Name;
    TextView Country;
    TextView MotorcycleModel;
    ImageView editProfile;
    ImageView ProfilePic;
    TextView currentPoints;
    TextView currentRanking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        bottomNavigationView = findViewById(R.id.navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        RebatesButton = findViewById(R.id.rebates_button);

        Name = findViewById(R.id.name);
        Country = findViewById(R.id.country_field);
        MotorcycleModel = findViewById(R.id.motorcycle_field);
        editProfile = findViewById(R.id.edit_profile);
        ProfilePic = findViewById(R.id.profile_pic);
        currentPoints = findViewById(R.id.points);
        currentRanking = findViewById(R.id.ranking);
        RebateValue = findViewById(R.id.rebate_value);

        RebatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PopupNotif.setVisibility(View.VISIBLE);
                SetRebateValue();
            }
        });

//        PopupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupNotif.setVisibility(View.GONE);
//            }
//        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navigation_home:
                        startActivity(new Intent(ProfileActivity.this, HomepageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_maintenance:
                        startActivity(new Intent(ProfileActivity.this, MaintenanceRecordsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_scoreboard:
                        startActivity(new Intent(ProfileActivity.this, LeaderBoardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_profile:
                        return true;
                }
                return false;
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        getNameAndCountry();
        ReadScores();
        getRanking();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getPhotoUrl() != null){
            Glide.with(this).load(user.getPhotoUrl()).into(ProfilePic);
        }
    }

    private void getNameAndCountry(){
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
                        if(document.getString("Country") != "") {
                            Country.setText(document.getString("Country"));
                        }else{
                            Country.setText("Not Set");
                        }
//                        if(document.getBoolean("Motorcycle Model") != null){
//                            MotorcycleModel.setText(document.getString("Country"));
//                        }
//                        else{
//                            MotorcycleModel.setText("Not Set");
//                        }
                    }
                } else {
                    Log.d(TAG, "error getting documents: ", task.getException());
                }
            }
        });
    }

    private void ReadScores() {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        mFirestore.collection("Users").document(userID).collection("Past trips").orderBy("Timestamp", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                        currentPoints.setText(document.get("Final Ride Score (Normalized)").toString());
                    }
                } else {
                    Log.d("TAG", "error getting documents: ", task.getException());
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

    private void SetRebateValue(){
        Log.e(TAG, "In Set Rebate Value Method");
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        ArrayList<Integer> normalizedRiderScore = new ArrayList<Integer>();
//        mFirestore.collection("Global_Parameters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for(final DocumentSnapshot doc: task.getResult()){
//                       if(doc.getId().equals("Lookup_table")){
//                           Log.e(TAG, "In Lookup Table");
//                           String NormalizedRiderScore = currentPoints.getText().toString();
//                           Log.e(TAG, "currentPoints Text: " + NormalizedRiderScore);
//                           ArrayList RebatesTable = (ArrayList<Object>) doc.get("Rebate_table");
//                           Double RebateVal = (Double)RebatesTable.get(Integer.parseInt(NormalizedRiderScore));
//                           Log.e(TAG, RebateVal.toString());
//                           RebateValue.setText("$" + RebateVal.toString());
//                       }
//
//                    }
//                }
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                });
        mFirestore.collection("Global_Parameters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(final DocumentSnapshot doc: task.getResult()){
                        if(doc.getId().equals("Lookup_table")){
                            Log.e(TAG, "In Lookup Table");
                            String NormalizedRiderScore = currentPoints.getText().toString();
                            Log.e(TAG, "currentPoints Text: " + NormalizedRiderScore);
                            ArrayList RebatesTable = (ArrayList<Object>) doc.get("Rebate_table");
                            Double RebateVal = (Double)RebatesTable.get(Integer.parseInt(NormalizedRiderScore));
                            Log.e(TAG, RebateVal.toString());
                            RebateValue.setText("$" + RebateVal.toString());
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

}
