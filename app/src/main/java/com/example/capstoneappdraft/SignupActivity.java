package com.example.capstoneappdraft;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    EditText firstName;
    EditText email;
    EditText country;
    EditText password;
    EditText confirmPassword;
    Button signupButton;
    String emailText;
    String passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        firstName = findViewById(R.id.user_field);
        email = findViewById(R.id.email_field);
        country = findViewById(R.id.country_field);
        password = findViewById(R.id.passwd_field);
        confirmPassword = findViewById(R.id.confirmpwd_field);
        signupButton = findViewById(R.id.signup_button);


        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailText = email.getText().toString();
                passwordText = password.getText().toString();
                checkDataEntered();
                if (checkDataEntered()) {
                    createAccount(emailText, passwordText);
                }
                else{
                    Toast.makeText(getApplicationContext(), "You did not fill in form correctly!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                    // if the user created intent to login activity
                    Intent intent = new Intent(SignupActivity.this, HomepageActivity.class);
                    startActivity(intent);
                } else {
                    // Registration failed
                    Toast.makeText(getApplicationContext(), "Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    boolean checkDataEntered() {
        String passwordText = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();

        if (isEmpty(firstName)) {
            Toast t = Toast.makeText(this, "You must enter first name to register!", Toast.LENGTH_SHORT);
            t.show();
        }
        if (isEmail(email) == false) {
            email.setError("Enter valid email!");
        }
        if (isEmpty(password)) {
            password.setError("Password is required!");
        }
        if (isEmpty(confirmPassword)) {
            confirmPassword.setError("Confirm Your Password!");
        }
        else if (password.length() < 6 && password.length() > 0){
            password.setError("Password must be more than 6 characters!");
        }
        if(isEmpty(firstName) ||isEmail(email) == false||isEmpty(password)||isEmpty(confirmPassword)||password.length() < 6 && password.length() > 0){
            return false;
        }
        return true;
    }

    boolean isEmpty (EditText text) {
//        String str = text.getText().toString();
//        return (str. equals(""));
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
