package com.example.foobarpart2.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.foobarpart2.MyApplication;
import com.example.foobarpart2.db.dao.UserDao;
import com.example.foobarpart2.db.database.AppDB;
import com.example.foobarpart2.db.entity.User;
import com.example.foobarpart2.models.LoggedInUser;
import com.example.foobarpart2.network.api.UserAPI;

import java.util.List;

public class UsersRepository {
    private UserDao dao;
    private MutableLiveData<Boolean> signUpResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> authenticateResult = new MutableLiveData<>();
    private MutableLiveData<User> userData = new MutableLiveData<>();


    private UserAPI api;

    public UsersRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "userDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        this.dao = db.userDao();
        api = new UserAPI(signUpResult, authenticateResult, userData, dao);
    }

    public void add(User user) {
        api.add(user);
    }

    public void delete(User user) {
        api.delete(user);
    }

    public void get(String username) {
        api.getUser(username);
    }


    public void authenticate(String username, String password) {
        api.authenticate(username, password);
    }

    public LiveData<Boolean> getSignUpResult() {
        return this.signUpResult;
    }

    public LiveData<Boolean> getAuthenticateResult() {
        return this.authenticateResult;
    }

    public LiveData<User> getUserData() {
        return this.userData;
    }


    public void reload() {

    }

    public void logOutCurrUser() {
        new Thread(() -> dao.deleteAllUsers());
        LoggedInUser.getInstance().setUser(null);
    }

    public boolean isFriendsWith(User wallUser) {
        User loggedInUser = LoggedInUser.getInstance().getUser();
        List<String> friendsUsernames = loggedInUser.getFriends();
        // Check if the list is not null and contains the username
        return (friendsUsernames != null) && friendsUsernames.contains(wallUser.getUsername());
    }

    public void addFriend(User wallUser) {
        api.addFriend(wallUser);
    }

    public void acceptFriendRequest(String username) {
        api.acceptFriendRequest(username);
    }

    public void declineFriendRequest(String username) {
        api.declineFriendRequest(username);
    }

    public void updateUser(User updatedUser) {
        api.updateUser(updatedUser);
    }

    public void removeFriend(User wallUser) {
        api.removeFriend(wallUser);
    }
}



