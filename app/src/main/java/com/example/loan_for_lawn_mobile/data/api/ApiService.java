package com.example.loan_for_lawn_mobile.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/exchangerates/tables/A?format=json")
    Call<List<ApiModels.NbpResponse>> getExchangeRates();
}
