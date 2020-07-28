package com.example.capstoneappdraft;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

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
    String firstNameText;
    String countryText;
    FirebaseFirestore mFirestore;
    //Button addProfilePicButton;
    ImageView profilePic;

    private static final String ID_KEY = "User ID";
    private static final String NAME_KEY = "Name";
    private static final String COUNTRY_KEY = "Country";
    private static final String TIME_STAMP = "Time Stamp";

    //boolean wasCalled = false;
    //boolean availuserID = false;


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
        //addProfilePicButton = findViewById(R.id.add_profile_pic);
        profilePic = findViewById(R.id.profile_pic);

//        addProfilePicButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                wasCalled = selectImage();
//            }
//        });

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailText = email.getText().toString();
                passwordText = password.getText().toString();
                firstNameText = firstName.getText().toString();
                countryText = country.getText().toString();
                checkDataEntered();
                if (checkDataEntered()) {
                    createAccount(emailText, passwordText, firstNameText, countryText);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic);
                    UploadProfilePic(bitmap);
                }
                else{
                    Toast.makeText(getApplicationContext(), "You did not fill in form correctly!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void createAccount(String email, String password, final String firstNameText, final String countryText) {
        Log.d(TAG, "createAccount:" + email);

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                    // set user info
                    FirebaseUser user=mAuth.getCurrentUser();
                    //set user's info into Firestore
                    // get current user ID of user who just signed up
                    String userID = user.getUid();
                    Map < String, Object > newUser = new HashMap < > ();
                    newUser.put(ID_KEY, userID);
                    newUser.put(NAME_KEY, firstNameText);
                    newUser.put(COUNTRY_KEY, countryText);
                    newUser.put(TIME_STAMP, FieldValue.serverTimestamp());

                    mFirestore.collection("Users").document(userID).collection("User Info").document().set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
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
//        String passwordText = password.getText().toString();
//        String confirmPasswordText = confirmPassword.getText().toString();

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
//        if (!wasCalled){
//            Toast t = Toast.makeText(this, "You set a profile picture!", Toast.LENGTH_SHORT);
//            t.show();
//        }
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


    private void UploadProfilePic(Bitmap bitmap){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, BAOS);
        final StorageReference reference = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(userID + ".jpeg");

        reference.putBytes(BAOS.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(reference);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: " + e.getCause());
                    }
                });

    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("TAG", "OnSuccess: " + uri);
                SetUserProfileUrl(uri);
            }
        });
    }

    private void SetUserProfileUrl(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();

        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SignupActivity.this, "Updated successfully!", Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Profile Image Failed.", Toast.LENGTH_SHORT);
            }
        });
    }
}
