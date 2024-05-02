package com.example.foobarpart2.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private final String username;
    private final String password;
    private String displayName;
    private String profilePic;
    private List<String> friends;
    private List<String> friendsRequest;

    public User(@NonNull String username, String password, String displayName, String profilePic,
                List<String> friends, List<String> friendsRequest) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.friends = friends;
        this.friendsRequest = friendsRequest;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriendsRequest() {
        if (this.friendsRequest == null) {
            return new ArrayList<>(); // Return an empty list if friendsRequest is null
        }
        return this.friendsRequest;
    }

    public void setFriendsRequest(List<String> friendsRequest) {
        this.friendsRequest = friendsRequest;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
