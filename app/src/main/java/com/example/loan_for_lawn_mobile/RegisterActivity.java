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

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput, confirmInput;
    private Button registerButton;
    private TextView errorText;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        tokenManager = new TokenManager(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameInput = findViewById(R.id.input_username);
        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        confirmInput = findViewById(R.id.input_confirm_password);
        registerButton = findViewById(R.id.btn_register);
        errorText = findViewById(R.id.error_text);

        registerButton.setOnClickListener(v -> attemptRegister());

        findViewById(R.id.link_login).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirm = confirmInput.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Wypełnij wszystkie pola.");
            return;
        }
        if (username.length() < 3) {
            showError("Nazwa użytkownika musi mieć co najmniej 3 znaki.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Hasła nie są zgodne.");
            return;
        }
        if (password.length() < 6) {
            showError("Hasło musi mieć co najmniej 6 znaków.");
            return;
        }

        setLoading(true);
        errorText.setVisibility(android.view.View.GONE);

        ApiModels.AuthRequest request = new ApiModels.AuthRequest(username, email, password);
        ApiClient.getBackendService().register(request).enqueue(new Callback<ApiModels.AuthResponse>() {
            @Override
            public void onResponse(Call<ApiModels.AuthResponse> call, Response<ApiModels.AuthResponse> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    ApiModels.AuthResponse auth = response.body();
                    tokenManager.saveToken(auth.token);
                    tokenManager.saveUser(auth.user);
                    ApiClient.setAuthToken(auth.token);
                    Toast.makeText(RegisterActivity.this, "Konto utworzone pomyślnie!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    ApiModels.ErrorResponse err = ApiClient.getRetrofitError(response, ApiModels.ErrorResponse.class);
                    showError(err != null ? err.error : "Rejestracja nie powiodła się.");
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
        registerButton.setEnabled(!loading);
        registerButton.setText(loading ? "Rejestracja..." : "Zarejestruj się");
    }

    private void showError(String msg) {
        errorText.setText(msg);
        errorText.setVisibility(android.view.View.VISIBLE);
    }
}
