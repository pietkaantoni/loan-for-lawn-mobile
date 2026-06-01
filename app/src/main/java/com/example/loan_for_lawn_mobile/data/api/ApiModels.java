package com.example.loan_for_lawn_mobile.data.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiModels {

    public static class AuthRequest {
        private String username;
        private String email;
        private String password;

        public AuthRequest(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public static class AuthResponse {
        public String token;
        public User user;
    }

    public static class User {
        public String id;
        public String username;
        public String email;
        @SerializedName("created_at")
        public String createdAt;
    }

    public static class UserResponse {
        public User user;
    }

    public static class CreateLoanRequest {
        public double amount;
        @SerializedName("interest_rate")
        public double interestRate;
        @SerializedName("due_date")
        public String dueDate;

        public CreateLoanRequest(double amount, double interestRate, String dueDate) {
            this.amount = amount;
            this.interestRate = interestRate;
            this.dueDate = dueDate;
        }
    }

    public static class Loan {
        public String id;
        @SerializedName("user_id")
        public String userId;
        public String amount;
        @SerializedName("interest_rate")
        public String interestRate;
        public String status;
        @SerializedName("due_date")
        public String dueDate;
        @SerializedName("created_at")
        public String createdAt;
    }

    public static class LoanResponse {
        public Loan loan;
    }

    public static class LoanListResponse {
        public List<Loan> loans;
    }

    public static class RepayResponse {
        public Loan loan;
        public String message;
    }

    public static class ContactRequest {
        public String name;
        public String email;
        public String message;

        public ContactRequest(String name, String email, String message) {
            this.name = name;
            this.email = email;
            this.message = message;
        }
    }

    public static class ContactResponse {
        public boolean success;
        public String message;
    }

    public static class RateCurrency {
        public String code;
        public String currency;
    }

    public static class AvailableCurrenciesResponse {
        public List<RateCurrency> popular;
        public List<RateCurrency> others;
    }

    public static class Rate {
        public String currency;
        public String code;
        public double rate;
    }

    public static class NbpRate {
        public String currency;
        public String code;
        public double mid;
    }

    public static class NbpTable {
        public String table;
        public String no;
        public String effectiveDate;
        public List<NbpRate> rates;
    }

    public static class RatesResponse {
        public String date;
        public String base;
        public List<Rate> rates;
    }

    public static class ErrorResponse {
        public String error;
    }
}
