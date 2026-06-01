package com.example.loan_for_lawn_mobile;

import android.app.AlertDialog;
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

import com.example.loan_for_lawn_mobile.data.api.ApiClient;
import com.example.loan_for_lawn_mobile.data.api.ApiModels;
import com.example.loan_for_lawn_mobile.utils.LoanCalculator;
import com.example.loan_for_lawn_mobile.utils.TokenManager;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanActivity extends AppCompatActivity {

    private static class LoanOffer {
        final int id;
        final int amount;
        final int months;
        final double interestRate;
        final String label;

        LoanOffer(int id, int amount, int months, double interestRate, String label) {
            this.id = id;
            this.amount = amount;
            this.months = months;
            this.interestRate = interestRate;
            this.label = label;
        }
    }

    private final LoanOffer[] offers = {
            new LoanOffer(1, 1000, 6, 4.5, "Na małe wydatki"),
            new LoanOffer(2, 3000, 12, 5.5, "Na drobne remonty"),
            new LoanOffer(3, 5000, 12, 6.5, "Na większy zakup"),
            new LoanOffer(4, 5000, 6, 8.0, "Szybka gotówka"),
            new LoanOffer(5, 10000, 24, 7.5, "Na większe inwestycje"),
            new LoanOffer(6, 10000, 12, 9.5, "Ekspresowa gotówka"),
            new LoanOffer(7, 25000, 24, 10.0, "Na wymarzony projekt"),
            new LoanOffer(8, 25000, 12, 12.5, "Premium na cel")
    };

    private TokenManager tokenManager;
    private LinearLayout offersContainer;
    private TextView errorText, successText;
    private LoanOffer selectedOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loan);

        tokenManager = new TokenManager(this);

        if (!tokenManager.isLoggedIn()) {
            startActivity(new android.content.Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        offersContainer = findViewById(R.id.offers_container);
        errorText = findViewById(R.id.error_text);
        successText = findViewById(R.id.success_text);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        ApiClient.setAuthToken(tokenManager.getToken());

        displayOffers();
    }

    private void displayOffers() {
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.forLanguageTag("pl-PL"));
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);

        for (LoanOffer offer : offers) {
            android.view.View offerView = getLayoutInflater().inflate(R.layout.item_offer, null);

            TextView amountText = offerView.findViewById(R.id.offer_amount);
            TextView labelText = offerView.findViewById(R.id.offer_label);
            TextView detailsText = offerView.findViewById(R.id.offer_details);
            Button confirmBtn = offerView.findViewById(R.id.btn_confirm);

            double monthly = LoanCalculator.calculateMonthlyPayment(offer.amount, offer.interestRate, offer.months);
            double total = monthly * offer.months;

            amountText.setText(fmt.format(offer.amount) + " PLN");
            labelText.setText(offer.label);
            detailsText.setText("Okres: " + offer.months + " mies. | "
                    + "Oprocentowanie: " + offer.interestRate + "%\n"
                    + "Miesięczna rata: " + fmt.format(monthly) + " PLN\n"
                    + "Całkowita spłata: " + fmt.format(total) + " PLN");

            offerView.setOnClickListener(v -> {
                selectedOffer = offer;
                highlightSelected(offerView);
                showConfirmationDialog(offer);
            });

            confirmBtn.setOnClickListener(v -> {
                showConfirmationDialog(offer);
            });

            offersContainer.addView(offerView);
        }
    }

    private void highlightSelected(android.view.View selected) {
        for (int i = 0; i < offersContainer.getChildCount(); i++) {
            offersContainer.getChildAt(i).setAlpha(0.5f);
        }
        selected.setAlpha(1.0f);
    }

    private void showConfirmationDialog(LoanOffer offer) {
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.forLanguageTag("pl-PL"));
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);

        double monthly = LoanCalculator.calculateMonthlyPayment(offer.amount, offer.interestRate, offer.months);
        double total = monthly * offer.months;

        String message = "Kwota: " + fmt.format(offer.amount) + " PLN\n"
                + "Okres: " + offer.months + " mies.\n"
                + "Oprocentowanie: " + offer.interestRate + "%\n"
                + "Miesięczna rata: " + fmt.format(monthly) + " PLN\n"
                + "Całkowita spłata: " + fmt.format(total) + " PLN\n\n"
                + "Czy jesteś pewien, że chcesz wziąć tę pożyczkę?";

        new AlertDialog.Builder(this)
                .setTitle("Potwierdzenie pożyczki")
                .setMessage(message)
                .setPositiveButton("Tak, chcę wziąć pożyczkę", (dialog, which) -> createLoan(offer))
                .setNegativeButton("Anuluj", null)
                .show();
    }

    private void createLoan(LoanOffer offer) {
        errorText.setVisibility(android.view.View.GONE);
        successText.setVisibility(android.view.View.GONE);

        String dueDate = LoanCalculator.calculateDueDate(offer.months);
        ApiModels.CreateLoanRequest request = new ApiModels.CreateLoanRequest(
                offer.amount, offer.interestRate, dueDate);

        ApiClient.getBackendService().createLoan(request).enqueue(new Callback<ApiModels.LoanResponse>() {
            @Override
            public void onResponse(Call<ApiModels.LoanResponse> call, Response<ApiModels.LoanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiModels.Loan loan = response.body().loan;
                    String msg = "Pożyczka " + loan.amount + " PLN została przyznana!";
                    successText.setText(msg);
                    successText.setVisibility(android.view.View.VISIBLE);
                } else {
                    ApiModels.ErrorResponse err = ApiClient.getRetrofitError(response, ApiModels.ErrorResponse.class);
                    showError(err != null ? err.error : "Nie udało się utworzyć pożyczki.");
                }
            }

            @Override
            public void onFailure(Call<ApiModels.LoanResponse> call, Throwable t) {
                showError("Błąd połączenia: " + t.getLocalizedMessage());
            }
        });
    }

    private void showError(String msg) {
        errorText.setText(msg);
        errorText.setVisibility(android.view.View.VISIBLE);
    }
}
