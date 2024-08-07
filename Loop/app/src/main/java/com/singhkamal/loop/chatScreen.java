package com.singhkamal.loop;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatScreen extends AppCompatActivity {

    private String reciverimg, reciverUid, reciverName, senderUID;
    private CircleImageView chat_profile;
    private CardView send_btn;
    private EditText txt_message;
    private TextView reciver_name;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    public static String senderImg;
    public static String receiverImg;

    private String senderRoom, receiverRoom;
    private RecyclerView messengerAdpt;
    private messagesAdapter messagesAdapter;
    private ArrayList<messageModelClass> messageModelClassArrayList;

    private boolean isSending = false;  // Track sending state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        // Initialize Firebase Auth and Database
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Get Intent Data
        reciverName = getIntent().getStringExtra("nameee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        // Check for missing data
        if (reciverName == null || reciverimg == null || reciverUid == null) {
            Toast.makeText(this, "Missing data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Sender and Receiver Rooms
        senderUID = firebaseAuth.getUid();
        senderRoom = senderUID + reciverUid;
        receiverRoom = reciverUid + senderUID;

        // Initialize ArrayList
        messageModelClassArrayList = new ArrayList<>();

        // Initialize RecyclerView and Adapter
        messengerAdpt = findViewById(R.id.msgadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messengerAdpt.setLayoutManager(linearLayoutManager);
        messagesAdapter = new messagesAdapter(chatScreen.this, messageModelClassArrayList);
        messengerAdpt.setAdapter(messagesAdapter);

        // Initialize Views
        send_btn = findViewById(R.id.sendbtn);
        txt_message = findViewById(R.id.txtmessage);
        chat_profile = findViewById(R.id.chatProfile);
        reciver_name = findViewById(R.id.reciverName);
        ImageView callButton = findViewById(R.id.callButton);
        ImageView videoCallButton = findViewById(R.id.videoCallButton);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        // Set Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Load Profile Image and Set Name
        Picasso.get().load(reciverimg).into(chat_profile);
        reciver_name.setText(reciverName);

        // Firebase References
        DatabaseReference reference = database.getReference().child("users").child(firebaseAuth.getUid());
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        // Load Messages
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModelClassArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messageModelClass messages = dataSnapshot.getValue(messageModelClass.class);
                    if (messages != null) {
                        messageModelClassArrayList.add(messages);
                    }
                }
                messagesAdapter.notifyDataSetChanged();
                messengerAdpt.scrollToPosition(messageModelClassArrayList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
                Toast.makeText(chatScreen.this, "Failed to load messages.", Toast.LENGTH_SHORT).show();
            }
        });

        // Load Sender Image
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("chatProfile").getValue(String.class);
                receiverImg = reciverimg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
                Toast.makeText(chatScreen.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });

        // Send Button Click Listener
        send_btn.setOnClickListener(v -> {
            if (isSending) {
                return;  // Avoid sending if already sending
            }
            isSending = true;  // Set sending state

            String message = txt_message.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(chatScreen.this, "Enter the Message First", Toast.LENGTH_SHORT).show();
                isSending = false;  // Reset sending state
                return;
            }
            txt_message.setText("");
            Date date = new Date();
            messageModelClass messages = new messageModelClass(message, senderUID, date.getTime());

            // Save Message to Firebase
            database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(messages)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            database.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(messages);
                        } else {
                            Toast.makeText(chatScreen.this, "Failed to send message.", Toast.LENGTH_SHORT).show();
                        }
                        // Reset sending state
                        isSending = false;
                    });
        });
    }
}
