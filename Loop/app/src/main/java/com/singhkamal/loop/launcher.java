package com.singhkamal.loop;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class launcher extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in, redirect to MainActivity
            startActivity(new Intent( launcher.this, MainActivity.class));
        } else {
            // User is not signed in, show login activity
            startActivity(new Intent(launcher.this, login.class));
        }
        finish();
    }
}
