package com.example.capstoneappdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static int TIME_OUT = 3000; //Time to launch the another activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = null;
//                if(FirebaseAuth.getInstance().getCurrentUser()!= null) {
//                    Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//                else{
                    i = new Intent(MainActivity.this, FirstPageActivity.class);
                    startActivity(i);
                    finish();
//                }
            }
        }, TIME_OUT);
    }
}
