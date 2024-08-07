package com.singhkamal.loop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {

    TextView reg_Login;
    EditText reg_UserName, reg_Password, reg_EmailAddress, reg_RePassword;
    Button signup_button;
    CircleImageView circle_ImageView;

    FirebaseAuth auth;
    Uri imageURI;
    String imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Account Created.");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        signup_button = findViewById(R.id.regSignupButton);
        reg_UserName = findViewById(R.id.regUserName);
        reg_EmailAddress = findViewById(R.id.regEmailAddress);
        reg_Password = findViewById(R.id.regPassword);
        reg_RePassword = findViewById(R.id.regRePassword);
        reg_Login = findViewById(R.id.regLogin);
        circle_ImageView = findViewById(R.id.profileImg);

        reg_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = reg_UserName.getText().toString();
                String email = reg_EmailAddress.getText().toString();
                String password = reg_Password.getText().toString();
                String rePassword = reg_RePassword.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(password) || TextUtils.isEmpty(rePassword)) {
                    Toast.makeText(registration.this, "Please Enter all the Details", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    reg_EmailAddress.setError("Please enter a valid email address");
                } else if (password.length() < 8) {
                    reg_Password.setError("The Password must be at least 8 characters long");
                } else if (!password.equals(rePassword)) {
                    reg_RePassword.setError("Passwords do NOT match");
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if (imageURI != null) {
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageUri = uri.toString();

                                                        User user = new User(id, name, email, password, imageUri, "Hey, I'm using this app",System.currentTimeMillis());
                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.show();
                                                                    Intent intent = new Intent(registration.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(registration.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    imageUri = "https://firebasestorage.googleapis.com/v0/b/loop-a846a.appspot.com/o/man.png?alt=media&token=7770e20b-5907-45d0-971b-866dd78334fc";
                                    User user = new User(id, name, email, password, imageUri, "Hey, I'm using this app",System.currentTimeMillis());
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.show();
                                                Intent intent = new Intent(registration.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(registration.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        circle_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 22);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            circle_ImageView.setImageURI(imageURI);
        }
    }
}
