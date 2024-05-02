package com.example.foobarpart2.network.api;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.foobarpart2.MyApplication;
import com.example.foobarpart2.R;
import com.example.foobarpart2.db.dao.PostDao;
import com.example.foobarpart2.db.entity.Comment;
import com.example.foobarpart2.db.entity.Post;
import com.example.foobarpart2.db.entity.User;
import com.example.foobarpart2.models.LoggedInUser;
import com.example.foobarpart2.network.request.PostEditRequest;
import com.example.foobarpart2.repository.TokenRepository;
import com.example.foobarpart2.repository.UsersRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAPI {
    private final MutableLiveData<List<Post>> wallPostData;
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private TokenRepository tokenRepository;
    private UsersRepository usersRepository;

    public PostAPI(MutableLiveData<List<Post>> postListData, PostDao dao, MutableLiveData<List<Post>> wallPostData) {
        this.postListData = postListData;
        this.wallPostData = wallPostData;
        this.dao = dao;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") // Server's date format
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        tokenRepository = new TokenRepository();
        usersRepository = new UsersRepository();
    }

    public void get() {
        Call<List<Post>> call = webServiceAPI.getPosts(tokenRepository.get());
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                new Thread(() -> {
                    dao.clear();
                    dao.insert(response.body().toArray(new Post[0]));
                    postListData.postValue(dao.index());
                }).start();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWallPosts(User wallUser) {
        Call<List<Post>> call = webServiceAPI.getPostsFriend(wallUser.getUsername(), tokenRepository.get());
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to load the posts, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> {
                        dao.clear();
                        dao.insert(response.body().toArray(new Post[0]));
                        wallPostData.postValue(dao.getWallPosts(wallUser.getUsername()));
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void add(Post post) {
        User user = LoggedInUser.getInstance().getUser();
        Call<Void> call = webServiceAPI.createPost(user.getUsername(), tokenRepository.get(), post);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 410){
                    Toast.makeText(MyApplication.context, "Unable to add the post, " +
                                    "it includes a forbidden link :( "
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to add the post, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> {
                        dao.insert(post);
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete(String postId) {
        User user = LoggedInUser.getInstance().getUser();
        Call<Void> call = webServiceAPI.deletePost(user.getUsername(), postId, tokenRepository.get());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to delete the post, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> {
                        dao.delete(dao.get(postId));
                    }).start();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void edit(String serverId, String updatedContent, String image) {
        User user = LoggedInUser.getInstance().getUser();
        PostEditRequest postEditRequest = new PostEditRequest(updatedContent, image);
        Call<Post> call = webServiceAPI.editPost(user.getUsername(), serverId, tokenRepository.get(),
                postEditRequest);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.code() == 410){
                    Toast.makeText(MyApplication.context, "Unable to edit the post, " +
                                    "it includes a forbidden link try again without it"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to edit the post, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> dao.update(response.body())).start();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void likePost(String postId) {
        User user = LoggedInUser.getInstance().getUser();
        Call<Post> call = webServiceAPI.likePost(user.getUsername(), postId, tokenRepository.get());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to like the post, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> dao.update(response.body())).start();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void disLikePost(String postId) {
        User user = LoggedInUser.getInstance().getUser();
        Call<Post> call = webServiceAPI.disLikePost(user.getUsername(), postId, tokenRepository.get());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to like the post, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> dao.update(response.body())).start();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addComment(String postId, Comment newComment) {
        User user = LoggedInUser.getInstance().getUser();
        Call<Post> call = webServiceAPI.addComment(user.getUsername(), postId, tokenRepository.get(), newComment);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyApplication.context, "Unable to add the comment, try later :)"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> dao.update(response.body())).start();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(MyApplication.context, "Unable to connect to the server."
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }


}
