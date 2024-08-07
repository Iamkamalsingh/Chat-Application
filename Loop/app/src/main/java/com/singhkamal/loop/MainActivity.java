package com.singhkamal.loop;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    user_Adapter adapter;
    FirebaseDatabase database;
    ArrayList<User> userArrayList;

    ImageView cameraBtn, settingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        cameraBtn = findViewById(R.id.camerabtn);
        settingBtn = findViewById(R.id.settingtbn);





        auth = FirebaseAuth.getInstance();

        // Check if the user is logged in
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, redirect to LoginActivity
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
        }





        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        DatabaseReference reference = database.getReference().child("user");

        userArrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear(); // Clear the list to avoid duplicate entries
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        Log.d("MainActivity", "User fetched: " + user.getUserName() + " - Profile Image: " + user.getProfileImage());
                        userArrayList.add(user);
                    }
                }
                // Sort the user list based on registrationTimestamp in descending order
                Collections.sort(userArrayList, new Comparator<User>() {
                    @Override
                    public int compare(User u1, User u2) {
                        return Long.compare(u2.getRegistrationTimestamp(), u1.getRegistrationTimestamp());
                    }
                });
                // Notify the adapter of data changes
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.e("MainActivity", "Error fetching data", error.toException());
            }
        });

        // Set click listeners
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 22);
            }
        });

        // Setup RecyclerView
        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new user_Adapter(MainActivity.this, userArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        // Redirect to login if user is not authenticated
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish(); // Call finish to close MainActivity
        }
    }
}
