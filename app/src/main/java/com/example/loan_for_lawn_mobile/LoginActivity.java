package com.example.loan_for_lawn_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loan_for_lawn_mobile.data.api.ApiClient;
import com.example.loan_for_lawn_mobile.data.api.ApiModels;
import com.example.loan_for_lawn_mobile.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView errorText;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        tokenManager = new TokenManager(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        errorText = findViewById(R.id.error_text);

        loginButton.setOnClickListener(v -> attemptLogin());

        findViewById(R.id.link_register).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Wypełnij wszystkie pola.");
            return;
        }

        setLoading(true);
        errorText.setVisibility(android.view.View.GONE);

        ApiModels.LoginRequest request = new ApiModels.LoginRequest(email, password);
        ApiClient.getBackendService().login(request).enqueue(new Callback<ApiModels.AuthResponse>() {
            @Override
            public void onResponse(Call<ApiModels.AuthResponse> call, Response<ApiModels.AuthResponse> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    ApiModels.AuthResponse auth = response.body();
                    tokenManager.saveToken(auth.token);
                    tokenManager.saveUser(auth.user);
                    ApiClient.setAuthToken(auth.token);
                    Toast.makeText(LoginActivity.this, "Zalogowano pomyślnie!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    ApiModels.ErrorResponse err = ApiClient.getRetrofitError(response, ApiModels.ErrorResponse.class);
                    showError(err != null ? err.error : "Nieprawidłowy email lub hasło.");
                }
            }

            @Override
            public void onFailure(Call<ApiModels.AuthResponse> call, Throwable t) {
                setLoading(false);
                showError("Błąd połączenia: " + t.getLocalizedMessage());
            }
        });
    }

    private void setLoading(boolean loading) {
        loginButton.setEnabled(!loading);
        loginButton.setText(loading ? "Logowanie..." : "Zaloguj się");
    }

    private void showError(String msg) {
        errorText.setText(msg);
        errorText.setVisibility(android.view.View.VISIBLE);
    }
}
