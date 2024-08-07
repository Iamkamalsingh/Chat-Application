package com.singhkamal.loop;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Settings extends AppCompatActivity {

    private static final int REQUEST_CODE = 22;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private ImageView setProfile;
    private EditText setName, setStatus;
    private Button donebtn, logoutbtn;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private Dialog logoutDialog;

    private String email, password;
    private Uri setImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Handle the back button click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        setProfile = findViewById(R.id.settingprofile);
        setName = findViewById(R.id.settingName);
        setStatus = findViewById(R.id.settingStatus);
        donebtn = findViewById(R.id.donebtn);
        logoutbtn = findViewById(R.id.logoutButton);

        // Initialize the logout dialog
        logoutDialog = new Dialog(Settings.this);
        logoutDialog.setContentView(R.layout.dialoge_layout);

        Button noBtn = logoutDialog.findViewById(R.id.nobtn);
        Button yesBtn = logoutDialog.findViewById(R.id.yesbtn);

        noBtn.setOnClickListener(v -> logoutDialog.dismiss());

        yesBtn.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(Settings.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.this, login.class); // Updated to LoginActivity
            startActivity(intent);
            finish();
        });

        // Get user data from Firebase
        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("email").getValue(String.class);
                password = snapshot.child("password").getValue(String.class);
                String name = snapshot.child("userName").getValue(String.class);
                String profile = snapshot.child("profileImage").getValue(String.class);
                String status = snapshot.child("status").getValue(String.class);

                setName.setText(name);
                setStatus.setText(status);
                Picasso.get().load(profile).into(setProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Error loading user data", Toast.LENGTH_SHORT).show();
            }
        });

        setProfile.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Settings.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openImageSelector();
            }
        });

        donebtn.setOnClickListener(v -> {
            String name = setName.getText().toString();
            String status = setStatus.getText().toString();

            if (setImageUri != null) {
                StorageReference fileReference = storage.getReference().child("upload").child(auth.getUid());

                fileReference.putFile(setImageUri).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String finalImageUri = uri.toString();
                            User user = new User(auth.getUid(), name, email, password, finalImageUri, status, System.currentTimeMillis());

                            reference.setValue(user).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(Settings.this, "Data Saved", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Settings.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(Settings.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    } else {
                        Toast.makeText(Settings.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profile = snapshot.child("profileImage").getValue(String.class);
                        User user = new User(auth.getUid(), name, email, password, profile, status, System.currentTimeMillis());

                        reference.setValue(user).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Settings.this, "Data Saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Settings.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(Settings.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Settings.this, "Error fetching profile image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        logoutbtn.setOnClickListener(v -> logoutDialog.show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            setImageUri = data.getData();
            setProfile.setImageURI(setImageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
    }
}
