package com.example.capstoneappdraft;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class FirstPageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

//        if(FirebaseAuth.getInstance().getCurrentUser()== null){
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }

    @Override
    public void onStart() {
        super.onStart();

        Button signupbtn = findViewById(R.id.signup_button);
        Button loginbtn = findViewById(R.id.login_button);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstPageActivity.this, SignupActivity.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstPageActivity.this, LoginActivity.class));
            }
        });
    }

}
