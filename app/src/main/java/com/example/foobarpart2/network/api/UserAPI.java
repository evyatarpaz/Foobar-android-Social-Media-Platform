package com.example.foobarpart2.network.api;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.foobarpart2.MyApplication;
import com.example.foobarpart2.R;
import com.example.foobarpart2.db.dao.UserDao;
import com.example.foobarpart2.db.entity.Token;
import com.example.foobarpart2.db.entity.User;
import com.example.foobarpart2.models.LoggedInUser;
import com.example.foobarpart2.network.request.LoginRequest;
import com.example.foobarpart2.repository.TokenRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private MutableLiveData<Boolean> signUpResult;
    private MutableLiveData<Boolean> authenticateResult;
    private MutableLiveData<User> userData;
    private UserDao dao;
    private final TokenRepository tokenRepository = new TokenRepository();
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserAPI(MutableLiveData<Boolean> signUpResult, MutableLiveData<Boolean> authenticateResult, MutableLiveData<User> userData, UserDao dao) {
        this.signUpResult = signUpResult;
        this.authenticateResult = authenticateResult;
        this.userData = userData;
        this.dao = dao;


        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void add(User user) {
        Call<Void> call = webServiceAPI.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to sign you up, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> dao.insert(user)).start();
                    signUpResult.setValue(true);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete(User user) {
        Call<Void> call = webServiceAPI.deleteUser(user.getUsername(),tokenRepository.get());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to delete user, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> dao.delete(user)).start();
                    Toast.makeText(MyApplication.context, "Your User has been Deleted, Hope to See You Soon"
                            , Toast.LENGTH_SHORT).show();
                    LoggedInUser.getInstance().setUser(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void authenticate(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<Token> call = webServiceAPI.authenticateUser(loginRequest);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "User Not Found!"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    TokenRepository tokenRepository = new TokenRepository();
                    tokenRepository.delete();
                    tokenRepository.add(response.body());
                    authenticateResult.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getUser(String username) {

        Call<User> call = webServiceAPI.getUser(username, tokenRepository.get());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to get your User information"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> dao.insert(response.body())).start();
                    userData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void addFriend(User wallUser) {
        Call<Void> call = webServiceAPI.addFriend(wallUser.getUsername(), tokenRepository.get());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to add the friend try later"
                            , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyApplication.context, "friend request sent successfully"
                            , Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void acceptFriendRequest(String username) {
        User loggedInUser = LoggedInUser.getInstance().getUser();
        Call<Void> call = webServiceAPI.acceptFriendRequest(loggedInUser.getUsername(),username,
                tokenRepository.get());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to accept the friend request try later"
                            , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyApplication.context, "friend accepted"
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void declineFriendRequest(String username) {
        User loggedInUser = LoggedInUser.getInstance().getUser();
        Call<Void> call = webServiceAPI.declineFriendRequest(loggedInUser.getUsername(),username,
                tokenRepository.get());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to decline the friend request try later"
                            , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyApplication.context, "friend request declined"
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUser(User updatedUser) {
        Call<User> call = webServiceAPI.updateUser(updatedUser.getUsername(),tokenRepository.get(),updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to decline the friend request try later"
                            , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyApplication.context, "updated your info"
                            , Toast.LENGTH_SHORT).show();
                    new Thread(() -> dao.update(response.body())).start();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeFriend(User wallUser) {
        User loggedInUser = LoggedInUser.getInstance().getUser();
        Call<Void> call = webServiceAPI.declineFriendRequest(loggedInUser.getUsername(),wallUser.getUsername(),
                tokenRepository.get());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to remove the friend try later"
                            , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyApplication.context, "friend removed successfully"
                            , Toast.LENGTH_SHORT).show();

                }

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
