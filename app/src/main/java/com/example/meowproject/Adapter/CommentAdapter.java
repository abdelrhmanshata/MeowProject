package com.example.meowproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.meowproject.Model.Comment;
import com.example.meowproject.R;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    Context context;
    List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item_comment, viewGroup, false);
        TextView userName = view.findViewById(R.id.userName);
        TextView userComment = view.findViewById(R.id.userComment);
        TextView commentDate = view.findViewById(R.id.commentDate);

        userName.setText(comments.get(i).getUserName());
        userComment.setText(comments.get(i).getComment());
        commentDate.setText(comments.get(i).getCommentDate());

        return view;
    }
}