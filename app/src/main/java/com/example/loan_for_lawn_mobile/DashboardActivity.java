package com.example.loan_for_lawn_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loan_for_lawn_mobile.data.AppDatabase;
import com.example.loan_for_lawn_mobile.data.dao.LoanDao;
import com.example.loan_for_lawn_mobile.data.entity.LoanEntity;
import com.example.loan_for_lawn_mobile.utils.TokenManager;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private TokenManager tokenManager;
    private LoanDao loanDao;
    private TextView welcomeText, statsTotal, statsActive, statsPaid, emptyText;
    private LinearLayout loansContainer;
    private Button newLoanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        tokenManager = new TokenManager(this);
        loanDao = AppDatabase.getInstance(this).loanDao();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        welcomeText = findViewById(R.id.welcome_text);
        statsTotal = findViewById(R.id.stats_total);
        statsActive = findViewById(R.id.stats_active);
        statsPaid = findViewById(R.id.stats_paid);
        emptyText = findViewById(R.id.empty_text);
        loansContainer = findViewById(R.id.loans_container);
        newLoanBtn = findViewById(R.id.btn_new_loan);

        if (!tokenManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        welcomeText.setText(getString(R.string.welcome_user, tokenManager.getUsername()));

        newLoanBtn.setOnClickListener(v ->
                startActivity(new Intent(this, LoanActivity.class)));

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_logout_dashboard).setOnClickListener(v -> {
            tokenManager.clear();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        loadLoans();
    }

    private void loadLoans() {
        String userId = tokenManager.getUserId();
        if (userId == null) return;

        List<LoanEntity> loans = loanDao.getByUserId(userId);
        displayLoans(loans);
    }

    private void displayLoans(List<LoanEntity> loans) {
        findViewById(R.id.loading_indicator).setVisibility(android.view.View.GONE);
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.forLanguageTag("pl-PL"));
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);

        int total = loans.size();
        int activeCount = 0;
        int paidCount = 0;

        for (LoanEntity l : loans) {
            if ("active".equals(l.getStatus())) activeCount++;
            else paidCount++;
        }

        statsTotal.setText(String.valueOf(total));
        statsActive.setText(String.valueOf(activeCount));
        statsPaid.setText(String.valueOf(paidCount));

        if (loans.isEmpty()) {
            emptyText.setVisibility(android.view.View.VISIBLE);
            return;
        }

        emptyText.setVisibility(android.view.View.GONE);

        for (LoanEntity loan : loans) {
            android.view.View loanView = getLayoutInflater().inflate(R.layout.item_loan, null);

            TextView amountText = loanView.findViewById(R.id.loan_amount);
            TextView rateText = loanView.findViewById(R.id.loan_rate);
            TextView dateText = loanView.findViewById(R.id.loan_date);
            TextView statusText = loanView.findViewById(R.id.loan_status);
            Button repayBtn = loanView.findViewById(R.id.btn_repay);

            amountText.setText(fmt.format(loan.getAmount()) + " PLN");
            rateText.setText("Oprocentowanie: " + loan.getInterestRate() + "%");

            try {
                String[] parts = loan.getDueDate().split("-");
                dateText.setText("Data ważności: " + parts[2] + "." + parts[1] + "." + parts[0]);
            } catch (Exception e) {
                dateText.setText("Data ważności: " + loan.getDueDate());
            }

            boolean isActive = "active".equals(loan.getStatus());
            statusText.setText(isActive ? "Aktywna" : "Spłacona");
            statusText.setBackgroundResource(isActive ?
                    android.R.color.holo_green_light : android.R.color.darker_gray);
            repayBtn.setVisibility(isActive ? android.view.View.VISIBLE : android.view.View.GONE);

            String loanId = loan.getId();
            repayBtn.setOnClickListener(v -> {
                loanDao.updateStatus(loanId, "paid");
                Toast.makeText(this, "Pożyczka spłacona!", Toast.LENGTH_SHORT).show();
                loansContainer.removeAllViews();
                loadLoans();
            });

            loansContainer.addView(loanView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tokenManager.isLoggedIn()) {
            loansContainer.removeAllViews();
            loadLoans();
        }
    }
}
