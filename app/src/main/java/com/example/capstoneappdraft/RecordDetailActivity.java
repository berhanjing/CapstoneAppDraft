package com.example.capstoneappdraft;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RecordDetailActivity extends AppCompatActivity {
    TextView descriptionText;
    TextView recordTitle;
    TextView dueDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_details);


        ReadMaintenanceRecords();
    }

    private void ReadMaintenanceRecords() {
        Intent intent = getIntent();
        String docID = intent.getStringExtra("docID");
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        DocumentReference document = mFirestore.collection("Users").document(userID).collection("Maintenance Records").document(docID);

        //get all documents in the "Maintenance Record" collection
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                LinearLayout oneRecord = findViewById(R.id.record_box);

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("TAG", document.getId() + " => " + document.getData());
                    recordTitle = findViewById(R.id.record_title);
                    dueDate = findViewById(R.id.due_date);
                    descriptionText = findViewById(R.id.description_text);
                    recordTitle.setText(document.getString("Title"));
                    dueDate.setText(document.getString("Date"));
                    descriptionText.setText(document.getString("Description"));



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
