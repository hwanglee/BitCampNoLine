package com.echessa.todo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Map<String, Object> map;
    private DatabaseReference mDatabase;
    private String mUserId;
    private float rad;
    private float distance;
    private Location location;
    private LocationManager locationManager;
    private Button checkInButton;
    private TextView name, university, marylandCount, otherCount;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location1) {
            location = location1;
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadWelcomeView();
        } else {
            mUserId = mFirebaseUser.getUid();
            checkInButton = (Button) findViewById(R.id.checkInButton);
            university = (TextView) findViewById(R.id.university);
            name = (TextView) findViewById(R.id.name);
            marylandCount = (TextView) findViewById(R.id.marylandCount);
            otherCount = (TextView) findViewById(R.id.otherCount);
            DatabaseReference user = mDatabase.child("users");

            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int mdCount = 0, oCount = 0;

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        map = (Map) child.getValue();
                        if (map.containsKey("university") && map.get("university").equals("University of Maryland")) {
                            mdCount++;
                        } else {
                            oCount++;
                        }

                    }
                    Toast.makeText(MainActivity.this, "testing", Toast.LENGTH_SHORT).show();

                    marylandCount.setText("Number of Maryland students: " + mdCount);
                    otherCount.setText("Number of Other students: " + oCount);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            user.child(mUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String firstName = "", lastName = "";

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("firstname")) {
                            firstName = child.getValue().toString();
                        }

                        if (child.getKey().equals("lastname")) {
                            lastName = child.getValue().toString();
                        }

                        if (child.getKey().equals("university")) {
                            university.setText("University: " + child.getValue().toString());
                        }
                    }

                    name.setText("Name: " + firstName + " " + lastName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //Permission is not granted Coordinates through GPS
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(MainActivity.this, "Comment...", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("NULL")
                        .setTitle("Error!")
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, mLocationListener);


            checkInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    rad = 150;
                    Location targetLocation = new Location("network");
                    targetLocation.setLatitude(38.995536d);
                    targetLocation.setLongitude(-76.940529d);
                    if (location == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("NULL")
                                .setTitle("Error!")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        distance = targetLocation.distanceTo(location);
                    }


                    //If too far from the address
                    if (distance < rad) {
                        mDatabase.child("users").child(mUserId).child("checked-in").setValue(true);
                        Toast.makeText(MainActivity.this, "You have successfully checked in!", Toast.LENGTH_LONG).show();
                    } else {
                        mDatabase.child("users").child(mUserId).child("checked-in").setValue(false);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Not in correct location!")
                                .setTitle("Error!")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }
    }


    private void loadWelcomeView() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mFirebaseAuth.signOut();
            loadWelcomeView();
        }

        return super.onOptionsItemSelected(item);
    }


}
