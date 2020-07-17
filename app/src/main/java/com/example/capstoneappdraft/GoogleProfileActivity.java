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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GoogleProfileActivity extends AppCompatActivity {

    Button mSubmitButton;
    EditText mCountry;
    EditText mFirstName;
    String firstNameText;
    String countryText;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;

    private static final String ID_KEY = "User ID";
    private static final String NAME_KEY = "Name";
    private static final String COUNTRY_KEY = "Country";
    private static final String TIME_STAMP = "Time Stamp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_profile);
        mSubmitButton= findViewById(R.id.country_button);
        mCountry= findViewById(R.id.google_country_field);
        mFirstName = findViewById(R.id.google_name_field);


        Log.i("ProfileActivity","before listener");
        db = FirebaseFirestore.getInstance();
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstNameText = mFirstName.getText().toString();
                countryText = mCountry.getText().toString();


                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    final Map<String, Object> newUser = new HashMap<>();

                    String userID = user.getUid();
                    newUser.put(ID_KEY, userID);
                    newUser.put(NAME_KEY, firstNameText);
                    newUser.put(COUNTRY_KEY, countryText);
                    newUser.put(TIME_STAMP, FieldValue.serverTimestamp());

                    db.collection("Users").document(userID).collection("User Info").document().set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("ProfileActivity", "DocumentSnapshot successfully written!");
                            Intent i = new Intent(GoogleProfileActivity.this, HomepageActivity.class);
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