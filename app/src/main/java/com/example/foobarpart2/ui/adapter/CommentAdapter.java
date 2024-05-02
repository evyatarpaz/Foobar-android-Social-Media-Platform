package com.example.foobarpart2.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarpart2.R;
import com.example.foobarpart2.db.entity.Comment;
import com.example.foobarpart2.models.LoggedInUser;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;

    private CommentActionListener actionListener;

    public CommentAdapter(List<Comment> comments, CommentActionListener actionListener) {
        this.comments = comments;
        this.actionListener = actionListener;
    }


    public interface CommentActionListener {
        void onEditComment(int position, Comment comment);
        void onDeleteComment(int postId, Comment comment);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.authorTextView.setText(comment.getUsername());
        holder.contentTextView.setText(comment.getContent());

        if (comment.getUsername().equals(LoggedInUser.getInstance().getUser().getUsername())) {
            holder.editCommentButton.setVisibility(View.VISIBLE);
            holder.deleteCommentButton.setVisibility(View.VISIBLE);
        } else {
            holder.editCommentButton.setVisibility(View.GONE);
            holder.deleteCommentButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged(); // Notify any registered observers that the data set has changed.
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView authorTextView;
        TextView contentTextView;
        TextView editCommentButton, deleteCommentButton;

        public CommentViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.commentAuthor);
            contentTextView = itemView.findViewById(R.id.commentContent);
            editCommentButton = itemView.findViewById(R.id.editComment);
            deleteCommentButton = itemView.findViewById(R.id.deleteComment);

            editCommentButton.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    actionListener.onEditComment(position, comments.get(position));
                }
            });

            deleteCommentButton.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Comment comment = comments.get(position);
                    comments.remove(position);
                    notifyItemRemoved(position);
                    actionListener.onDeleteComment(comment.getPostId(), comment); // Update the storage
                }
            });

        }
    }
}
