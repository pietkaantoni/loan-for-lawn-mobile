package com.example.loan_for_lawn_mobile.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "loans",
        foreignKeys = @ForeignKey(entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("user_id"))
public class LoanEntity {
    @PrimaryKey
    private String id;

    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "interest_rate")
    private double interestRate;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "due_date")
    private String dueDate;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    public LoanEntity(String id, String userId, double amount, double interestRate,
                      String status, String dueDate, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
