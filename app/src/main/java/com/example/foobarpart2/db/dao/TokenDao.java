package com.example.foobarpart2.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foobarpart2.db.entity.Token;

@Dao
public interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToken(Token token);

    @Query("SELECT * FROM Token LIMIT 1")
    Token getToken();

    @Query("DELETE FROM Token")
    void deleteAllTokens();

}
