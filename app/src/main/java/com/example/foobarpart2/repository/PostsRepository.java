package com.example.foobarpart2.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.foobarpart2.MyApplication;
import com.example.foobarpart2.db.dao.PostDao;
import com.example.foobarpart2.db.database.AppDB;
import com.example.foobarpart2.db.entity.Comment;
import com.example.foobarpart2.db.entity.Post;
import com.example.foobarpart2.db.entity.User;
import com.example.foobarpart2.network.api.PostAPI;
import com.example.foobarpart2.repository.tasks.GetPostsTask;

import java.util.LinkedList;
import java.util.List;


public class PostsRepository {
    private PostDao dao;
    private PostListData postListData;
    private MutableLiveData<Post> postMutableLiveData;
    private PostAPI api;
    private MutableLiveData<List<Post>> wallPostData;

    public PostsRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "postDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        this.dao = db.postDao();
        postListData = new PostListData();
        postMutableLiveData = new MutableLiveData<>();
        wallPostData = new MutableLiveData<>();
        api = new PostAPI(postListData, dao,wallPostData);

    }

    public void add(Post post) {
        api.add(post);
    }

    public void delete(String postId) {
        api.delete(postId);
    }

    public void getPostFromDao(int postId) {
        new Thread(() -> {
            Post post = dao.get(postId);
            postMutableLiveData.postValue(post);
        }).start();
    }

    public LiveData<Post> getPostData() {
        return this.postMutableLiveData;
    }

    public void edit(String serverId, String updatedContent, String image) {
        api.edit(serverId,updatedContent,image);
    }

    public void likePost(String postId) {
        api.likePost(postId);
    }

    public void disLikePost(String postId) {
        api.disLikePost(postId);
    }

    public void addComment(String postId, Comment newComment) {
        api.addComment(postId,newComment);
    }

    public void reloadWall(User wallUser) {
        api.getWallPosts(wallUser);
    }

    public LiveData<List<Post>> getWallPostsData() {
        return this.wallPostData;
    }

    class PostListData extends MutableLiveData<List<Post>> {

        public PostListData() {
            super();
            setValue(new LinkedList<Post>());

        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                postListData.postValue(dao.index());
            }).start();

        }

    }

    public LiveData<List<Post>> getAll() {
        return postListData;

    }

    public void reload() {
        new GetPostsTask(postListData, dao,api).execute();
    }
}




