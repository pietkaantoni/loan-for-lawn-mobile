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

import com.example.loan_for_lawn_mobile.data.AppDatabase;
import com.example.loan_for_lawn_mobile.data.dao.UserDao;
import com.example.loan_for_lawn_mobile.data.entity.UserEntity;
import com.example.loan_for_lawn_mobile.utils.PasswordUtil;
import com.example.loan_for_lawn_mobile.utils.TokenManager;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput, confirmInput;
    private Button registerButton;
    private TextView errorText;
    private TokenManager tokenManager;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        tokenManager = new TokenManager(this);
        userDao = AppDatabase.getInstance(this).userDao();

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

        if (userDao.getByEmail(email) != null) {
            showError("Konto z tym adresem email już istnieje.");
            return;
        }
        if (userDao.getByUsername(username) != null) {
            showError("Nazwa użytkownika jest już zajęta.");
            return;
        }

        errorText.setVisibility(android.view.View.GONE);
        registerButton.setEnabled(false);
        registerButton.setText("Rejestracja...");

        String id = UUID.randomUUID().toString();
        String hash = PasswordUtil.hash(password);
        String createdAt = java.time.LocalDate.now().toString();

        UserEntity user = new UserEntity(id, username, email, hash, createdAt);
        userDao.insert(user);

        tokenManager.saveSession(id, username, email);
        Toast.makeText(this, "Konto utworzone pomyślnie!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    private void showError(String msg) {
        errorText.setText(msg);
        errorText.setVisibility(android.view.View.VISIBLE);
        registerButton.setEnabled(true);
        registerButton.setText("Zarejestruj się");
    }
}
