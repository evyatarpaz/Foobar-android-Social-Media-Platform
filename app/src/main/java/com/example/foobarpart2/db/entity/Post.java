package com.example.foobarpart2.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity
public class Post {
    private String _id;
    @PrimaryKey(autoGenerate = true)
    private int postId;
    @NonNull
    private String username;
    private String displayName;
    private String profilePic;
    @NonNull
    private Date date;
    @NonNull
    private String content;
    private int numComments;
    private List<Comment> comments;
    private int numlikes;
    private String[] likeby;
    private boolean isLiked;
    private String image;

    public Post(@NonNull String username, String displayName, String profilePic, @NonNull Date date, @NonNull String content, int numComments, List<Comment> comments, int numlikes, String[] likeby,boolean isLiked ,String image) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.date = date;
        this.content = content;
        this.numComments = numComments;
        this.comments = comments;
        this.numlikes = numlikes;
        this.likeby = likeby;
        this.isLiked = false;
        if (likeby != null) {
            for (String liker : likeby) {
                if (username.equals(liker)) {
                    this.isLiked = true;
                    break;
                }
            }
        }
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getPostId() {
        return this.postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @NonNull
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @NonNull
    public Date getDate() {
        return this.date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public int getNumlikes() {
        return this.numlikes;
    }

    public void setNumlikes(int numlikes) {
        this.numlikes = numlikes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCommentsCount() {
        return numComments;
    }

    public void setCommentsCount(int commentsCount) {
        this.numComments = commentsCount;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String[] getLikeby() {
        return likeby;
    }

    public void setLikeby(String[] likeby) {
        this.likeby = likeby;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public boolean isLiked() {
        return isLiked;
    }

    public void toggleLikeStatus() {
        isLiked = !isLiked; // Toggle the like status
        if (isLiked) {
            numlikes++; // If liked, increment the like count
        } else {
            numlikes--; // If unliked, decrement the like count
        }
    }

    // Method to add a comment to the post
    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.numComments++;
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        this.numComments--;
    }
}
