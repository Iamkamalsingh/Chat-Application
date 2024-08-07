package com.singhkamal.loop;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_Adapter extends RecyclerView.Adapter<user_Adapter.viewholder> {
    MainActivity mainActivity;
    ArrayList<User> userArrayList;

    public user_Adapter(MainActivity mainActivity, ArrayList<User> userArrayList) {
        this.mainActivity = mainActivity;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public user_Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_details, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull user_Adapter.viewholder holder, int position) {
        User user = userArrayList.get(position);
        holder.user_name.setText(user.getUserName());
        holder.user_status.setText(user.getStatus());

        // Log the profileImage URL to debug
        Log.d("UserAdapter", "Profile Image URL for " + user.getUserName() + ": " + user.getProfileImage());

        // Load user profile image with placeholder and error handling
        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
            Picasso.get()
                    .load(user.getProfileImage())
                    .placeholder(R.drawable.man) // Add a placeholder image
                    .error(R.drawable.man) // Add an error image
                    .into(holder.user_img);
        } else {
            // If no profile image URL is provided, set a default image resource
            holder.user_img.setImageResource(R.drawable.man);
        }

        // Chat Screen
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, chatScreen.class);
                intent.putExtra("nameee", user.getUserName());
                intent.putExtra("reciverImg", user.getProfileImage());
                intent.putExtra("uid", user.getUserId());
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView user_img;
        TextView user_name, user_status;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            user_img = itemView.findViewById(R.id.userImg);
            user_name = itemView.findViewById(R.id.userName);
            user_status = itemView.findViewById(R.id.userStatus);
        }
    }
}
