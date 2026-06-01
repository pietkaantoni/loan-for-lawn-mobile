package com.example.loan_for_lawn_mobile.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String NBP_BASE_URL = "https://api.nbp.pl/";
    private static ApiService nbpService;

    public static ApiService getClient() {
        if (nbpService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NBP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            nbpService = retrofit.create(ApiService.class);
        }
        return nbpService;
    }
}
