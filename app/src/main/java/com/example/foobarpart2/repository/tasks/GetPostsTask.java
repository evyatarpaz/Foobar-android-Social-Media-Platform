package com.example.foobarpart2.repository.tasks;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.foobarpart2.db.dao.PostDao;
import com.example.foobarpart2.db.entity.Post;
import com.example.foobarpart2.network.api.PostAPI;

import java.util.List;

public class GetPostsTask extends AsyncTask<Void, Void, Void> {
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;
    private final PostAPI api;

    public GetPostsTask(MutableLiveData<List<Post>> postListData, PostDao dao, PostAPI api) {
        this.postListData = postListData;
        this.dao = dao;
        this.api = api;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            api.get();

        } catch (Exception e) {
            // Handle any errors during fetching, parsing, or updating the database
            e.printStackTrace();
        }
        return null;
    }
}