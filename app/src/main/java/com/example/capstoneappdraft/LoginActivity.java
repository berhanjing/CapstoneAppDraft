package com.example.capstoneappdraft;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    ImageButton backButton;
    Button loginButton;
    EditText emailInput;
    EditText passwdInput;
    String emailText;
    String passwordText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private static final String TAG = "LOGINACTIVITY";
    private static final String ID_KEY = "User ID";
    private static final String TIME_STAMP = "Time Stamp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        backButton = findViewById(R.id.backButton);
        loginButton = findViewById(R.id.login_button);
        emailInput = findViewById(R.id.email_input);
        passwdInput = findViewById(R.id.password_input);

        mFirestore = FirebaseFirestore.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, FirstPageActivity.class));
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDataEntered();
                if (checkDataEntered()) {
                    LoginToAccount();
                }
            }
        });
    }

    boolean checkDataEntered() {
        if (isEmpty(emailInput) || isEmpty(passwdInput) || (isEmpty(emailInput) && isEmpty(passwdInput))) {
            Toast t = Toast.makeText(this, "You must enter relevant fields!", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        return true;
    }


    boolean isEmpty(EditText text) {
//        String str = text.getText().toString();
//        return (str. equals(""));
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private void LoginToAccount() {
        // Take the value of two edit texts in Strings
        String email, password;
        email = emailInput.getText().toString();
        password = passwdInput.getText().toString();

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();

                    mFirestore.collection("Users").document().set(ID_KEY).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error writing document", e);
                            }
                        });
                    Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                    startActivity(intent);
                } else {
                    // sign-in failed
                    Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}

