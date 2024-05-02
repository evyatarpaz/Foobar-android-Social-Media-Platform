package com.example.foobarpart2.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foobarpart2.db.entity.User;

import java.util.List;


@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> index();

    @Query("SELECT * FROM user WHERE username = :username")
    User get(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... users);
    @Update
    void update(User... users);

    @Delete
    void delete(User... users);
    @Query("DELETE FROM User")
    void deleteAllUsers();
}
