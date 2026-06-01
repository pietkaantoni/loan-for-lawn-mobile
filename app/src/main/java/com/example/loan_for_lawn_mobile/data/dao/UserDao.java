package com.example.loan_for_lawn_mobile.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.loan_for_lawn_mobile.data.entity.UserEntity;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);

    @Query("SELECT * FROM users WHERE id = :userId")
    UserEntity getById(String userId);

    @Query("DELETE FROM users")
    void deleteAll();
}
