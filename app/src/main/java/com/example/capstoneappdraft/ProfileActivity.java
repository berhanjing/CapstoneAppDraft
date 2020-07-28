package com.example.capstoneappdraft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    BottomNavigationView bottomNavigationView;
    Button RebatesButton;
    TextView Name;
    TextView Country;
    TextView MotorcycleModel;
    ImageView editProfile;
    ImageView ProfilePic;


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

        RebatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

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


}
