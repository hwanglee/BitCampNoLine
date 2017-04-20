package com.echessa.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import classes.Profile;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp2Activity extends AppCompatActivity {

    private Profile profile;
    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        profile = Profile.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog alert = new AlertDialog.Builder(SignUp2Activity.this).create();

                    alert.setTitle("Error");
                    alert.setMessage("Please fill out all fields");
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alert.show();
                } else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp2Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUp2Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d("TEST2",  mDatabase.child("users").child(task.getResult().getUser().getUid()).toString());
                                        mDatabase.child("users").child(task.getResult().getUser().getUid()).child("firstname").setValue(profile.getFirstName());
                                        mDatabase.child("users").child(task.getResult().getUser().getUid()).child("lastname").setValue(profile.getLastName());
                                        mDatabase.child("users").child(task.getResult().getUser().getUid()).child("university").setValue(profile.getSchool());
                                        mDatabase.child("users").child(task.getResult().getUser().getUid()).child("grade").setValue(profile.getSchoolYear());
                                        mDatabase.child("users").child(task.getResult().getUser().getUid()).child("birthday").setValue(profile.getDOB());
                                        mDatabase.child("users").child(task.getResult().getUser().getUid()).child("dietary-restrictions").setValue(profile.hasFoodAllergies());
                                        mDatabase.child("users").child(task.getResult().getUser().getUid()).child("checked-in").setValue(false);

                                        Toast.makeText(SignUp2Activity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUp2Activity.this, LogInActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                }
            }
        });
    }

}
