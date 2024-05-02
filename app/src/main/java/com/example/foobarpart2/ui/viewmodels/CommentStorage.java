package com.example.foobarpart2.ui.viewmodels;

import com.example.foobarpart2.db.entity.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentStorage {
    public static final Map<Integer, List<Comment>> commentsMap = new HashMap<>();
}
