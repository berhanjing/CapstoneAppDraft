package com.example.capstoneappdraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderBoardActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ListView mListView;
    String[] ListElements = new String[0];
    final String TAG="LeaderBoardActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        mListView=findViewById(R.id.listView);
        final Map<String, Object> place = new HashMap<>();

        final List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (LeaderBoardActivity.this, android.R.layout.simple_list_item_1, ListElementsArrayList);
        mListView.setAdapter(adapter);

        mAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser!=null) {
            //set country
            Log.d(TAG, "CURRENTUSER NOT NULL");
            db.collection("leaderboard")
                    .orderBy("score", Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                String profile="";
                                Log.d("LeaderBoardActivity", "task success");
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("LeaderBoardActivity", "in loop");
                                    ListElementsArrayList.add(document.getData().get("name")+"\n"+document.getData().get("score"));
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                                return;
                            }
                        }
                    });
        }
        else{
            Intent i = new Intent(LeaderBoardActivity.this,FirstPageActivity.class);
            startActivity(i);
        }
    }
}