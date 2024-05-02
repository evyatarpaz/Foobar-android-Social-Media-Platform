package com.example.foobarpart2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarpart2.R;
import com.example.foobarpart2.db.entity.User;
import com.example.foobarpart2.models.LoggedInUser;
import com.example.foobarpart2.ui.adapter.FriendRequestAdapter;
import com.example.foobarpart2.ui.viewmodels.UserViewModel;

import java.util.List;

public class FriendRequestListFragment extends Fragment {

    private RecyclerView rvFriendRequests;
    private TextView tvNoRequests;
    private FriendRequestAdapter adapter;
    private List<String> friendRequests;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_request_list, container, false);
        rvFriendRequests = view.findViewById(R.id.rvFriendRequests);
        tvNoRequests = view.findViewById(R.id.tvNoRequests);
        ImageButton exitButton = view.findViewById(R.id.btnExitFragment);
        exitButton.setOnClickListener(v -> {
            // Action to close the fragment
            closeFragment();
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        User loggedInUser = LoggedInUser.getInstance().getUser();

        friendRequests = loggedInUser.getFriendsRequest();

        adapter = new FriendRequestAdapter(getContext(), friendRequests, new FriendRequestAdapter.FriendRequestListener() {
            @Override
            public void onAccept(String username) {
                userViewModel.acceptFriendRequest(username);
                adapter.removeRequest(username);
                updateUI();
            }

            @Override
            public void onDecline(String username) {
                userViewModel.declineFriendRequest(username);
                adapter.removeRequest(username);
                updateUI();
            }
        });

        rvFriendRequests.setAdapter(adapter);
        rvFriendRequests.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUI();

        return view;
    }

    private void updateUI() {
        if (friendRequests.isEmpty()) {
            tvNoRequests.setVisibility(View.VISIBLE);
            rvFriendRequests.setVisibility(View.GONE);
        } else {
            tvNoRequests.setVisibility(View.GONE);
            rvFriendRequests.setVisibility(View.VISIBLE);
        }
    }

    private void closeFragment() {
        // Check if the fragment is added to an activity and if so, perform actions to exit
        if (isAdded() && getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

            getActivity().findViewById(R.id.fragment_container).setVisibility(View.GONE);
        }
    }
}
