package com.example.foobarpart2.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foobarpart2.R;
import com.example.foobarpart2.db.entity.User;
import com.example.foobarpart2.models.LoggedInUser;
import com.example.foobarpart2.ui.adapter.PostListAdapter;
import com.example.foobarpart2.ui.viewmodels.PostsViewModel;
import com.example.foobarpart2.ui.viewmodels.UserViewModel;
import com.example.foobarpart2.utilities.ImageUtils;

public class WallActivity extends AppCompatActivity {
    private PostsViewModel postViewModel;
    private UserViewModel userViewModel;
    private RecyclerView rvUserPosts;
    private PostListAdapter postListAdapter;
    private SwipeRefreshLayout refreshLayout;
    private User loggedInUser;
    private User wallUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        String wallUserName = getIntent().getStringExtra("friendUserName");

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        postViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
        loggedInUser = LoggedInUser.getInstance().getUser();

        rvUserPosts = findViewById(R.id.lstPosts);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);

        setupRecyclerView();

        userViewModel.getUser(wallUserName);
        userViewModel.getUserLiveData().observe(this, user -> {
            wallUser = user;

            updateWallUserInfo(wallUser);
            postViewModel.reloadWall(wallUser);
            loadWallPosts();
            updateFriendshipStatusAndUI();
        });

        refreshLayout.setOnRefreshListener(() -> {
            if (wallUser != null) {
                postViewModel.reloadWall(wallUser);
            } else {
                // Handle case where wallUser might be null on refresh
                refreshLayout.setRefreshing(false);
            }
        });

        ImageButton friendRequestButton = findViewById(R.id.friend_request_button);
        friendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest();
            }
        });


    }


    private void setupRecyclerView() {
        postListAdapter = new PostListAdapter(this, this, position -> {
            Intent intent = new Intent(WallActivity.this, CommentActivity.class);
            intent.putExtra("postId", postListAdapter.getPosts().get(position).getPostId());
            intent.putExtra("author", loggedInUser.getDisplayName());
            startActivity(intent);
        }, this::onDeletePost, this::onLikePost, this::onDisLikePost);

        rvUserPosts.setAdapter(postListAdapter);
        rvUserPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateWallUserInfo(User wallUser) {
        if (wallUser != null) {
            TextView userNameView = findViewById(R.id.user_name);
            ImageView profilePhotoView = findViewById(R.id.profile_photo);

            // Set the username
            userNameView.setText(wallUser.getDisplayName());

            if (wallUser.getProfilePic() != null && !wallUser.getProfilePic().isEmpty()) {
                Bitmap profilePicBitmap = ImageUtils.decodeBase64ToBitmap(wallUser.getProfilePic());
                profilePhotoView.setImageBitmap(profilePicBitmap);
            }
        }
    }

    private void loadWallPosts() {
        boolean isFriends = userViewModel.isFriendsWith(wallUser);

        if (isFriends || wallUser.getUsername().equals(LoggedInUser.getInstance().getUser().getUsername())) {
            postViewModel.getPostsForWall().observe(this, posts -> {
                if (!posts.isEmpty()) {
                    // If they are friends and there are posts, show the posts and hide the message.
                    postListAdapter.setPosts(posts);
                    rvUserPosts.setVisibility(View.VISIBLE);
                    findViewById(R.id.tvNotFriendsMessage).setVisibility(View.GONE);
                } else {
                    // They are friends but there are no posts to show, hide both the RecyclerView and the message
                    rvUserPosts.setVisibility(View.GONE);
                    TextView notFriendsMessage = findViewById(R.id.tvNotFriendsMessage);
                    notFriendsMessage.setText(R.string.no_posts_available);
                    notFriendsMessage.setVisibility(View.VISIBLE);
                }
                refreshLayout.setRefreshing(false);
            });
        } else {
            // Not friends, hide the RecyclerView and show the not-friends message
            rvUserPosts.setVisibility(View.GONE);
            TextView notFriendsMessage = findViewById(R.id.tvNotFriendsMessage);
            notFriendsMessage.setText(R.string.you_are_not_friends); // Reset the message to "not friends" message
            notFriendsMessage.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
        }
    }
    private void updateFriendshipStatusAndUI() {
        ImageButton friendRequestButton = findViewById(R.id.friend_request_button);

        if (userViewModel.isFriendsWith(wallUser)) {
            // If they are friends, show the "friends" icon
            friendRequestButton.setImageResource(R.drawable.ic_unfriend); // Assuming ic_friends is a drawable resource
            friendRequestButton.setOnClickListener(v -> removeFriend());
        } else {
            // If they are not friends, show the "add friend" icon
            friendRequestButton.setImageResource(R.drawable.ic_add_friend);
            friendRequestButton.setOnClickListener(v -> sendFriendRequest());
        }
    }




    public void onDeletePost(String postId) {
        // Implementation remains the same
    }

    private void onLikePost(String postId) {
        postViewModel.likePost(postId);
    }

    private void onDisLikePost(String postId) {
        postViewModel.disLikePost(postId);
    }

    private void sendFriendRequest() {
        userViewModel.addFriend(wallUser);

        // On success, change the button icon
        ImageButton friendRequestButton = findViewById(R.id.friend_request_button);
        friendRequestButton.setImageResource(R.drawable.ic_check);


        friendRequestButton.setEnabled(false);
    }
    private void removeFriend() {
        userViewModel.removeFriend(wallUser);

        // Update the UI to reflect the change
        ImageButton friendRequestButton = findViewById(R.id.friend_request_button);
        friendRequestButton.setImageResource(R.drawable.ic_add_friend);
        friendRequestButton.setOnClickListener(v -> sendFriendRequest());

    }

}
