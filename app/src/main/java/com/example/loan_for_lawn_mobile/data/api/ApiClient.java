package com.example.loan_for_lawn_mobile.data.api;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BACKEND_BASE_URL = "http://10.0.2.2:3001/api/";
    private static final String NBP_BASE_URL = "https://api.nbp.pl/api/";

    private static ApiService backendService;
    private static ApiService nbpService;
    private static String authToken;

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static void clearAuthToken() {
        authToken = null;
    }

    public static ApiService getBackendService() {
        if (backendService == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor authInterceptor = chain -> {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                if (authToken != null) {
                    builder.header("Authorization", "Bearer " + authToken);
                }
                builder.header("Content-Type", "application/json");
                return chain.proceed(builder.build());
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BACKEND_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            backendService = retrofit.create(ApiService.class);
        }
        return backendService;
    }

    public static ApiService getNbpService() {
        if (nbpService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Accept", "application/json")
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NBP_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            nbpService = retrofit.create(ApiService.class);
        }
        return nbpService;
    }

    public static <T> T getRetrofitError(Response<?> response, Class<T> errorClass) {
        try {
            if (response.errorBody() != null) {
                return new com.google.gson.Gson().fromJson(
                        response.errorBody().charStream(), errorClass);
            }
        } catch (Exception ignored) {}
        return null;
    }

    public static void resetServices() {
        backendService = null;
        nbpService = null;
    }
}
