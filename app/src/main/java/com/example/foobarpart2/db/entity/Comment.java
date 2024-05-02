package com.example.foobarpart2.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int commentId;
    private int postId;
    private String id;
    @NonNull
    private String username;
    private String displayName;
    @NonNull
    private String content;
    private Date timestamp;

    public Comment(int postId, String id,@NonNull String username,String displayName, @NonNull String content, Date timestamp) {
        this.postId = postId;
        this.id = id;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.displayName = displayName;
    }

    public Comment(int postId, String id, @NonNull String username, @NonNull String content) {
        this.postId = postId;
        this.id = id;
        this.username = username;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommentId() {
        return commentId;
    }
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
