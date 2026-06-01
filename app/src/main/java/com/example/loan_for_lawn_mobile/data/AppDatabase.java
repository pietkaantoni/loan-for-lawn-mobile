package com.example.loan_for_lawn_mobile.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.loan_for_lawn_mobile.data.dao.LoanDao;
import com.example.loan_for_lawn_mobile.data.dao.UserDao;
import com.example.loan_for_lawn_mobile.data.entity.LoanEntity;
import com.example.loan_for_lawn_mobile.data.entity.UserEntity;

@Database(entities = {UserEntity.class, LoanEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract LoanDao loanDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "loan_for_lawn_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
