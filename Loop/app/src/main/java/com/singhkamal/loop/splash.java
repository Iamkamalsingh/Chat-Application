package com.singhkamal.loop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class splash extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is logged in, redirect to MainActivity without showing splash screen
            Intent intent = new Intent(splash.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close SplashActivity so the user cannot navigate back to it
        } else {
            // User is not logged in, show the splash screen
            setContentView(R.layout.activity_splash);

            // Delay transition to next activity to show splash screen
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(splash.this, login.class);
                startActivity(intent);
                finish(); // Close SplashActivity so the user cannot navigate back to it
            }, SPLASH_SCREEN_DELAY);
        }
    }
}
