package com.example.foobarpart2.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarpart2.R;
import com.example.foobarpart2.db.entity.Post;
import com.example.foobarpart2.models.LoggedInUser;
import com.example.foobarpart2.ui.activity.EditPostActivity;
import com.example.foobarpart2.ui.activity.WallActivity;
import com.example.foobarpart2.utilities.DateUtil;
import com.example.foobarpart2.utilities.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {
    public interface OnCommentButtonClickListener {
        void onCommentButtonClicked(int position);
    }

    public interface PostActionListener {
        void onDeletePost(String postId);
    }

    public interface PostLikeListener {
        void onLikePost(String postId);
    }

    public interface PostDisLikeListener {
        void onDisLikePost(String postId);
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final ImageView profile;
        private final ImageButton btnLike;
        private final ImageButton btnComment;
        private final ImageButton btnShare;
        private final TextView numOfLikes;
        private final TextView numOfComments;
        private final ImageButton postSettingsBtn;
        private final TextView uploadTime;


        private PostViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            profile = itemView.findViewById(R.id.profile_pic);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnShare = itemView.findViewById(R.id.btnShare);
            numOfLikes = itemView.findViewById(R.id.num_of_likes);
            numOfComments = itemView.findViewById(R.id.num_of_comments);
            postSettingsBtn = itemView.findViewById(R.id.postSettings);
            uploadTime = itemView.findViewById(R.id.tvUploadTime);

            btnComment.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    commentButtonClickListener.onCommentButtonClicked(position);
                }
            });
        }


    }

    private static final int REQUEST_CODE_EDIT_POST = 200;
    private final LayoutInflater mInflater;
    private List<Post> posts;
    private Activity activity;
    private PostActionListener listener;
    private PostLikeListener likeListener;
    private PostDisLikeListener disLikeListener;

    private OnCommentButtonClickListener commentButtonClickListener;

    public PostListAdapter(Activity activity, Context context,
                           OnCommentButtonClickListener commentButtonClickListener,
                           PostActionListener listener, PostLikeListener postLikeListener, PostDisLikeListener disLikeListener) {
        this.activity = activity;
        this.mInflater = LayoutInflater.from(context);
        this.commentButtonClickListener = commentButtonClickListener;
        this.listener = listener;
        this.likeListener = postLikeListener;
        this.disLikeListener = disLikeListener;
        this.posts = new ArrayList<>();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (posts != null) {
            final Post current = posts.get(position);
            holder.tvAuthor.setText(current.getDisplayName());
            holder.uploadTime.setText(DateUtil.formatDateString(current.getDate().toString()));
            holder.tvContent.setText(current.getContent());

            if (current.getImage() != null) {
                holder.ivPic.setVisibility(View.VISIBLE);
                holder.ivPic.setImageBitmap(ImageUtils.decodeBase64ToBitmap(current.getImage()));
            } else {
                holder.ivPic.setVisibility(View.GONE);
            }
            if (current.getProfilePic() != null) {
                holder.profile.setImageBitmap(ImageUtils.decodeBase64ToBitmap(current.getProfilePic()));
            }

            if (current.isLiked()){
                holder.btnLike.setImageResource(R.drawable.ic_liked);
            }

            holder.numOfLikes.setText(String.valueOf(current.getNumlikes()));

            holder.numOfComments.setText(String.valueOf(current.getCommentsCount()));

            holder.btnLike.setOnClickListener(v -> {
                // Toggle like status for the current post
                current.toggleLikeStatus();

                // Update like count in the UI
                holder.numOfLikes.setText(String.valueOf(current.getNumlikes()));

                // Update like button icon based on like status
                if (current.isLiked()) {
                    holder.btnLike.setImageResource(R.drawable.ic_liked);
                    likeListener.onLikePost(posts.get(position).get_id());
                } else {
                    holder.btnLike.setImageResource(R.drawable.ic_like);
                    disLikeListener.onDisLikePost(posts.get(position).get_id());
                }
            });
            holder.btnShare.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(mInflater.getContext(), holder.btnShare);
                popup.inflate(R.menu.share_menu);
                popup.show();
            });


            if (current.getUsername().equals(LoggedInUser.getInstance().getUser().getUsername())) {
                holder.postSettingsBtn.setVisibility(View.VISIBLE);
                holder.postSettingsBtn.setOnClickListener(v -> {
                    PopupMenu popup = new PopupMenu(activity, holder.postSettingsBtn);
                    popup.inflate(R.menu.post_options_menu);
                    popup.setOnMenuItemClickListener(item -> {
                        int itemId = item.getItemId();
                        if (itemId == R.id.action_edit_post) {
                            // Start EditPostActivity
                            Intent intent = new Intent(activity, EditPostActivity.class);
                            intent.putExtra("postId", posts.get(position).getPostId());
                            intent.putExtra("content", posts.get(position).getContent());
                            activity.startActivityForResult(intent, REQUEST_CODE_EDIT_POST);
                            return true;
                        }
                        if (itemId == R.id.action_delete_post) {
                            if (position != RecyclerView.NO_POSITION) {
                                Post postToDelete = posts.get(position);
                                listener.onDeletePost(postToDelete.get_id());
                                removeAt(position);
                            }
                            return true;
                        }
                        return false;
                    });
                    // Show the popup menu
                    popup.show();
                });
            } else {
                holder.postSettingsBtn.setVisibility(View.GONE);
            }

            holder.tvAuthor.setOnClickListener(v -> {
                Intent intent = new Intent(activity, WallActivity.class);
                intent.putExtra("friendUserName", posts.get(position).getUsername());
                activity.startActivity(intent);
            });
        }
    }

    public void setPosts(List<Post> postList) {
        posts = postList;
        notifyDataSetChanged();
    }

    public void addPost(Post post) {
        posts.add(post);
        notifyItemInserted(posts.size() - 1);
    }

    @Override
    public int getItemCount() {
        if (posts != null) {
            return posts.size();
        } else return 0;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void removeAt(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, posts.size());
    }


}
