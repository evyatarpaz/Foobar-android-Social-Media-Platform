package com.example.foobarpart2.db.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foobarpart2.db.converters.CommentConverters;
import com.example.foobarpart2.db.converters.StringArrayConverters;
import com.example.foobarpart2.db.converters.StringListConverter;
import com.example.foobarpart2.db.converters.TimeConverters;
import com.example.foobarpart2.db.converters.UriConverters;
import com.example.foobarpart2.db.dao.PostDao;
import com.example.foobarpart2.db.dao.TokenDao;
import com.example.foobarpart2.db.dao.UserDao;
import com.example.foobarpart2.db.entity.Post;
import com.example.foobarpart2.db.entity.Token;
import com.example.foobarpart2.db.entity.User;

@Database(entities = {User.class, Token.class, Post.class}, version = 26)
@TypeConverters({TimeConverters.class, UriConverters.class, CommentConverters.class, StringArrayConverters.class, StringListConverter.class})
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TokenDao tokenDao();
    public abstract PostDao postDao();
}
