package com.example.capstoneappdraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GoogleProfileActivity extends AppCompatActivity {

    Button mSubmitButton;
    EditText mCountry;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_profile);
        mSubmitButton= findViewById(R.id.country_button);
        mCountry= findViewById(R.id.google_country_field);
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final Map<String, Object> place = new HashMap<>();
        Log.i("ProfileActivity","before listener");
        db=FirebaseFirestore.getInstance();
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("ProfileActivity", "in the listener");
                if(currentUser!=null) {
                    //set country
                    Log.d("ProfileActivity", "CURRENTUSER NOT NULL");
                    place.put("country",mCountry.getText().toString());

                    db.collection("users").document(currentUser.getUid()).set(place)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("ProfileActivity", "DocumentSnapshot successfully written!");
                                    Intent i = new Intent(GoogleProfileActivity.this,HomepageActivity.class);
                                    startActivity(i);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("ProfileActivity", "Error writing document", e);
                                }
                            });
                }
            }
        });
    }

}