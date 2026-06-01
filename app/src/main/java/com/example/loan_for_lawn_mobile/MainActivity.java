package com.example.loan_for_lawn_mobile;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loan_for_lawn_mobile.utils.TokenManager;

public class MainActivity extends AppCompatActivity {

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tokenManager = new TokenManager(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btn_login).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        findViewById(R.id.btn_register).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        findViewById(R.id.btn_loan).setOnClickListener(v -> {
            if (tokenManager.isLoggedIn()) {
                startActivity(new Intent(this, LoanActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        findViewById(R.id.nav_rates).setOnClickListener(v ->
                startActivity(new Intent(this, RatesActivity.class)));

        findViewById(R.id.nav_about).setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));

        findViewById(R.id.nav_contact).setOnClickListener(v ->
                startActivity(new Intent(this, ContactActivity.class)));

        updateAuthState();
    }

    private void updateAuthState() {
        boolean loggedIn = tokenManager.isLoggedIn();
        String username = tokenManager.getUsername();

        findViewById(R.id.auth_buttons).setVisibility(loggedIn ? android.view.View.GONE : android.view.View.VISIBLE);
        findViewById(R.id.user_info).setVisibility(loggedIn ? android.view.View.VISIBLE : android.view.View.GONE);
        findViewById(R.id.btn_login).setVisibility(loggedIn ? android.view.View.GONE : android.view.View.VISIBLE);

        if (loggedIn && username != null) {
            ((android.widget.TextView) findViewById(R.id.user_greeting)).setText(
                    getString(R.string.welcome_user, username));

            findViewById(R.id.btn_dashboard).setOnClickListener(v ->
                    startActivity(new Intent(this, DashboardActivity.class)));

            findViewById(R.id.btn_logout).setOnClickListener(v -> {
                tokenManager.clear();
                recreate();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAuthState();
    }
}
