package com.example.foobarpart2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarpart2.R;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    private List<String> friendRequests; // Assuming each request is identified by the friend's username
    private Context context;

    public interface FriendRequestListener {
        void onAccept(String username);
        void onDecline(String username);
    }

    private FriendRequestListener listener;

    public FriendRequestAdapter(Context context, List<String> friendRequests, FriendRequestListener listener) {
        this.context = context;
        this.friendRequests = friendRequests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_friend_request, parent, false);
        return new FriendRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        String username = friendRequests.get(position);
        holder.tvFriendName.setText(username);
        holder.btnAccept.setOnClickListener(v -> listener.onAccept(username));
        holder.btnDecline.setOnClickListener(v -> listener.onDecline(username));
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    static class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName;
        ImageButton btnAccept, btnDecline;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }
    }
    public void removeRequest(String username) {
        int position = friendRequests.indexOf(username);
        if (position > -1) {
            friendRequests.remove(position);
            notifyItemRemoved(position);
        }
    }
}
