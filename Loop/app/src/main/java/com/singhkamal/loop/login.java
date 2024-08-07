package com.singhkamal.loop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    Button button;
    EditText email, password;
    FirebaseAuth auth;
    TextView login_signupbtn;
    ImageView eyeImageView;  // Add this line for the eye icon

    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait..");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logbutton);
        email = findViewById(R.id.editTextlogEmail);
        password = findViewById(R.id.editTextlogPassword);
        login_signupbtn = findViewById(R.id.logSignup);
        eyeImageView = findViewById(R.id.eyeImageView);  // Initialize the eye icon





        auth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, redirect to MainActivity
            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        }




        // Set up eye icon click listener to toggle password visibility
        eyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeImageView.setImageResource(R.drawable.eye_on);  // Change to eye icon for visible
                } else {
                    password.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeImageView.setImageResource(R.drawable.eye_off);  // Change to eye icon for hidden
                }
                password.setSelection(password.getText().length());  // Move cursor to the end
            }
        });

        login_signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, registration.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Password)) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    email.setError("Please enter a valid email address");
                } else if (Password.length() < 8) {
                    progressDialog.dismiss();
                    password.setError("Passwords must be at least 8 characters");
                    Toast.makeText(login.this, "Passwords must be at least 8 characters", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.show();
                                try {
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
