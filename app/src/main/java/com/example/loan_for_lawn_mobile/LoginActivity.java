package com.example.loan_for_lawn_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loan_for_lawn_mobile.data.AppDatabase;
import com.example.loan_for_lawn_mobile.data.dao.UserDao;
import com.example.loan_for_lawn_mobile.data.entity.UserEntity;
import com.example.loan_for_lawn_mobile.utils.PasswordUtil;
import com.example.loan_for_lawn_mobile.utils.TokenManager;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView errorText;
    private TokenManager tokenManager;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        tokenManager = new TokenManager(this);
        userDao = AppDatabase.getInstance(this).userDao();

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

        errorText.setVisibility(android.view.View.GONE);

        UserEntity user = userDao.getByEmail(email);
        if (user == null) {
            showError("Nieprawidłowy email lub hasło.");
            return;
        }
        if (!PasswordUtil.check(password, user.getPasswordHash())) {
            showError("Nieprawidłowy email lub hasło.");
            return;
        }

        tokenManager.saveSession(user.getId(), user.getUsername(), user.getEmail());
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    private void showError(String msg) {
        errorText.setText(msg);
        errorText.setVisibility(android.view.View.VISIBLE);
    }
}
