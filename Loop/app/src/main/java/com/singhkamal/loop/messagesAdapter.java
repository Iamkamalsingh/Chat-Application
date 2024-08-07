package com.singhkamal.loop;

//import static com.singhkamal.loop.chatScreen.receiverIImg;
import static com.singhkamal.loop.chatScreen.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class messagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<messageModelClass> messageAdapterArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public messagesAdapter(Context context, ArrayList<messageModelClass> messageAdapterArrayList) {
        this.context = context;
        this.messageAdapterArrayList = messageAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layou, parent, false);
            return new senderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receivera_layout, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        messageModelClass message = messageAdapterArrayList.get(position);

        if (holder instanceof senderViewHolder) {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtxt.setText(message.getMessage());
            // Removed Picasso call for CircleImageView
        } else {
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.msgtxt.setText(message.getMessage());
            // Removed Picasso call for CircleImageView
        }
    }

    @Override
    public int getItemCount() {
        return messageAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        messageModelClass message = messageAdapterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        // CircleImageView removed
        TextView msgtxt;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            // circleImageView = itemView.findViewById(R.id.profilerggg); // Removed
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    class receiverViewHolder extends RecyclerView.ViewHolder {
        // CircleImageView removed
        TextView msgtxt;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            // circleImageView = itemView.findViewById(R.id.pro); // Removed
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}
