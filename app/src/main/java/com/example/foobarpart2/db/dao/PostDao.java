package com.example.foobarpart2.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foobarpart2.db.entity.Post;

import java.util.List;
@Dao
public interface PostDao {
    @Query("SELECT * FROM post")
    List<Post> index();
    @Query("SELECT * FROM post WHERE username = :username")
    List<Post> getWallPosts(String username);
    @Query("SELECT * FROM post WHERE postId = :id")
    Post get(int id);
    @Query("SELECT * FROM post WHERE _id = :serverId")
    Post get(String serverId);

    @Query("DELETE FROM post")
    void clear();

    @Insert(entity = Post.class)
    void insert(Post... posts);

    @Update
    void update(Post... posts);

    @Delete
    void delete(Post... posts);
}
