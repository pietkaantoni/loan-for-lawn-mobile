package com.example.loan_for_lawn_mobile.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/register")
    Call<ApiModels.AuthResponse> register(@Body ApiModels.AuthRequest body);

    @POST("auth/login")
    Call<ApiModels.AuthResponse> login(@Body ApiModels.LoginRequest body);

    @GET("auth/me")
    Call<ApiModels.UserResponse> getMe();

    @GET("loans")
    Call<ApiModels.LoanListResponse> getLoans();

    @POST("loans")
    Call<ApiModels.LoanResponse> createLoan(@Body ApiModels.CreateLoanRequest body);

    @POST("loans/{id}/repay")
    Call<ApiModels.RepayResponse> repayLoan(@Path("id") String id);

    @GET("rates/available")
    Call<ApiModels.AvailableCurrenciesResponse> getAvailableCurrencies();

    @GET("rates")
    Call<ApiModels.RatesResponse> getRates(@Query("currencies") String currencies);

    @POST("contact")
    Call<ApiModels.ContactResponse> sendContact(@Body ApiModels.ContactRequest body);

    @GET("health")
    Call<Object> health();

    @GET("exchangerates/tables/A/")
    Call<List<ApiModels.NbpTable>> getNbpRates();
}
