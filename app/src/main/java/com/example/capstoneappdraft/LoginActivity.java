package com.example.capstoneappdraft;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        backButton = findViewById(R.id.backButton);
        loginButton = findViewById(R.id.login_button);
        emailInput = findViewById(R.id.email_input);
        passwdInput = findViewById(R.id.password_input);

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
                LoginToAccount();
            }
        });
    }

    void checkDataEntered() {
        if (isEmpty(emailInput) || isEmpty(passwdInput) || (isEmpty(emailInput) && isEmpty(passwdInput))) {
            Toast t = Toast.makeText(this, "You must enter relevant fields!", Toast.LENGTH_SHORT);
            t.show();
        }
    }
//            var email: String = emailEt.text.toString()
//            var password: String = passwordEt.text.toString()
//
//            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                Toast.makeText(this@LoginActivity, "Please fill all the fields", Toast.LENGTH_LONG).show()
//            } else{
//                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
//                    if(task.isSuccessful) {
//                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }else {
//                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
//                    }
//                })
//            }
//        }

    boolean isEmpty (EditText text) {
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
                    // if sign-in is successful
                    // intent to home activity
                    Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                    startActivity(intent);
                }
                else {
                    // sign-in failed
                    Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        // super.onBackPressed();
//        Toast.makeText(LoginActivity.this,"There is no back action",Toast.LENGTH_LONG).show();
//        return;
//    }
}

