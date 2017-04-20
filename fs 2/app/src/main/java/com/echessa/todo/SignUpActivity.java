package com.echessa.todo;

import classes.*; //Package

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private String[] grades, schools;

    private Profile profile;
    private EditText firstName, lastName, month, day, year;
    private CheckBox foodAllergies;
    private Spinner schoolYear, school;
    private Button nextButton;
    //False by default
    Boolean foodAllergiesBool = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        foodAllergiesBool = false;
        profile = Profile.getInstance();

        //Other values
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        school = (Spinner) findViewById(R.id.school);
        month = (EditText) findViewById(R.id.month);
        day = (EditText) findViewById(R.id.day);
        year = (EditText) findViewById(R.id.year);
        schoolYear = (Spinner) findViewById(R.id.school_year);
        //foodAllergies = ((CheckBox) view).isChecked();

        grades = new String[]{"Freshman", "Sophomore", "Junior", "Senior"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, grades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        schoolYear.setAdapter(adapter);

        schools = new String[]{"University of Maryland", "Other"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, schools);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        school.setAdapter(adapter2);

        nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //New values
                String firstNameStr = firstName.getText().toString().trim();
                String lastNameStr = lastName.getText().toString().trim();

                if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || month.getText().toString().isEmpty()
                        || day.getText().toString().isEmpty() || year.getText().toString().isEmpty()) {

                    AlertDialog alert = new AlertDialog.Builder(SignUpActivity.this).create();

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

                    int monthInt = Integer.parseInt(month.getText().toString());
                    int dayInt = Integer.parseInt(day.getText().toString());
                    int yearInt = Integer.parseInt(year.getText().toString());
                    String schoolStr = school.getSelectedItem().toString();
                    String grade = schoolYear.getSelectedItem().toString();

                    Log.d("MAIN", schoolStr);

                    //For food allergies, if 1 true, if 0 false
                    //int foodAllergiesInt = Integer.parseInt(foodAllergies.getText().toString());
                    //Boolean foodAllergiesBool = foodAllergiesInt == 1 ? true : false;


                    if (monthInt < 1 || monthInt > 12 || dayInt < 1 || dayInt > 31) {
                        AlertDialog alert = new AlertDialog.Builder(SignUpActivity.this).create();

                        alert.setTitle("Error");
                        alert.setMessage("Invalid birthday");
                        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        alert.show();
                    } else {
                        profile.setFirstName(firstNameStr);
                        profile.setLastName(lastNameStr);
                        profile.setDOB(monthInt, dayInt, yearInt);
                        profile.setFoodAllergies(foodAllergiesBool);
                        profile.setSchool(schoolStr);
                        profile.setSchoolYear(grade);

                        Toast.makeText(SignUpActivity.this, profile.getFirstName() + " " + profile.getLastName() + " " + profile.getDOB(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUpActivity.this, SignUp2Activity.class);
                        Log.d("DATE", monthInt + " " + dayInt + " " + yearInt);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void foodAllergiesCheckBox(View view) {
        foodAllergiesBool = ((CheckBox) view).isChecked();
    }

}