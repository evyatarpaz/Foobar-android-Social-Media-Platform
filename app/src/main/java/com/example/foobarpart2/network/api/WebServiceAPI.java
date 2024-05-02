package com.example.foobarpart2.network.api;

import com.example.foobarpart2.db.entity.Comment;
import com.example.foobarpart2.db.entity.Post;
import com.example.foobarpart2.db.entity.Token;
import com.example.foobarpart2.db.entity.User;
import com.example.foobarpart2.network.request.LoginRequest;
import com.example.foobarpart2.network.request.PostEditRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {

    /*
     * Token Requests
     */

    @POST("tokens")
    Call<Token> authenticateUser(@Body LoginRequest loginRequest);

    /*
     * User Requests
     */
    @POST("users")
    Call<Void> createUser(@Body User user);

    @GET("users/{id}")
    Call<User> getUser(@Path("id") String username, @Header("Authorization") String token);

    @PATCH("users/{id}")
    Call<User> updateUser(@Path("id") String username, @Header("Authorization") String token, @Body User updatedUser);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") String username,@Header("Authorization") String token);

    /*
     * Post Requests
     */
    @GET("posts")
    Call<List<Post>> getPosts(@Header("Authorization") String token);

    @GET("users/{id}/posts")
    Call<List<Post>> getPosts(@Path("id") String username,
                              @Header("Authorization") String token);

    @POST("users/{id}/posts")
    Call<Void> createPost(@Path("id") String username,
                          @Header("Authorization") String token, @Body Post post);

    @PATCH("users/{id}/posts/{pid}")
    Call<Post> editPost(@Path("id") String username, @Path("pid") String _id,
                        @Header("Authorization") String token, @Body PostEditRequest postEditRequest);

    @DELETE("users/{id}/posts/{pid}")
    Call<Void> deletePost(@Path("id") String username, @Path("pid") String postId,
                          @Header("Authorization") String token);

    @PATCH("users/{id}/posts/{pid}/like")
    Call<Post> likePost(@Path("id") String username, @Path("pid") String _id,
                        @Header("Authorization") String token);

    @DELETE("users/{id}/posts/{pid}/like")
    Call<Post> disLikePost(@Path("id") String username, @Path("pid") String _id,
                           @Header("Authorization") String token);

    @POST("users/{id}/posts/{pid}/comment")
    Call<Post> addComment(@Path("id") String username, @Path("pid") String _id,
                          @Header("Authorization") String token, @Body Comment newComment);
    @GET("users/{id}/posts")
    Call<List<Post>> getPostsFriend(@Path("id") String username, @Header("Authorization") String token);

    @POST("users/{id}/friends")
    Call<Void> addFriend(@Path("id")String friendUserName, @Header("Authorization") String token);
    @PATCH("users/{id}/friends/{fid}")
    Call<Void> acceptFriendRequest(@Path("id")String loggedInUserName,@Path("fid") String friendUserName,@Header("Authorization") String token);
    @DELETE("users/{id}/friends/{fid}")
    Call<Void> declineFriendRequest(@Path("id")String loggedInUserName,@Path("fid") String friendUserName,@Header("Authorization") String token);

}
