package com.example.loan_for_lawn_mobile.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.loan_for_lawn_mobile.data.entity.LoanEntity;

import java.util.List;

@Dao
public interface LoanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LoanEntity loan);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LoanEntity> loans);

    @Query("SELECT * FROM loans WHERE user_id = :userId ORDER BY created_at DESC")
    List<LoanEntity> getByUserId(String userId);

    @Query("SELECT * FROM loans WHERE id = :loanId")
    LoanEntity getById(String loanId);

    @Query("UPDATE loans SET status = :status WHERE id = :loanId")
    void updateStatus(String loanId, String status);

    @Query("DELETE FROM loans WHERE user_id = :userId")
    void deleteByUserId(String userId);

    @Query("DELETE FROM loans")
    void deleteAll();
}
