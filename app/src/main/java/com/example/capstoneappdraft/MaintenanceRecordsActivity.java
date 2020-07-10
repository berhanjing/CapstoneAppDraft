package com.example.capstoneappdraft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MaintenanceRecordsActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomNavigationView;
    TextView titleRecord;
    TextView dateRecord;
    TextView timeRecord;
    String docID;
    private static final String TAG = "MaintenanceRecord";
    LinearLayout oneRecord = findViewById(R.id.record_box);



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenancerecords);


        bottomNavigationView = findViewById(R.id.navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_maintenance);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navigation_maintenance:
                        return true;
                    case R.id.navigation_home:
                        startActivity(new Intent(MaintenanceRecordsActivity.this, HomepageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_scoreboard:
                        startActivity(new Intent(MaintenanceRecordsActivity.this, LeaderBoardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(MaintenanceRecordsActivity.this, ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        ReadMaintenanceRecords();

       oneRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Intent intent = new Intent(MaintenanceRecordsActivity.this, RecordDetailActivity.class);
//            intent.putExtra("docID", docID);
//            startActivity(intent);

            }
        });

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaintenanceRecordsActivity.this, CreateMaintenanceRecordActivity.class));
            }
        });


    }

    private List<String> ReadMaintenanceRecords() {
        final ArrayList<String> docIDs = new ArrayList<String>();

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        //gets current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        //get all documents in the "Maintenance Record" collection
        mFirestore.collection("Users").document(userID).collection("Maintenance Records").orderBy("Time Stamp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {
                    for(final DocumentSnapshot doc: task.getResult()){
                        View view = getLayoutInflater().inflate(R.layout.each_maintenancerecord, oneRecord,false);
                        titleRecord = view.findViewById(R.id.title_field);
                        dateRecord = view.findViewById(R.id.date_record);
                        timeRecord = view.findViewById(R.id.time_field);
                        titleRecord.setText(doc.get("Title").toString());
                        dateRecord.setText(doc.get("Date").toString());
                        timeRecord.setText(doc.get("Time").toString());
                        docID = doc.getId();
                        docIDs.add(docID);

                        oneRecord.addView(view);
                        //ImageButton getRecordDetail = findViewById(R.id.expand_record);

                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        return docIDs;
    }
}






