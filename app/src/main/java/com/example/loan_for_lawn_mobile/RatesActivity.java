package com.example.loan_for_lawn_mobile;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loan_for_lawn_mobile.data.api.ApiClient;
import com.example.loan_for_lawn_mobile.data.api.ApiModels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatesActivity extends AppCompatActivity {

    private LinearLayout popularContainer, othersContainer;
    private TextView ratesTable, dateText, errorText, loadingText;
    private final List<String> selectedCodes = new ArrayList<>();
    private List<ApiModels.NbpRate> allRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rates);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        popularContainer = findViewById(R.id.popular_container);
        othersContainer = findViewById(R.id.others_container);
        ratesTable = findViewById(R.id.rates_table);
        dateText = findViewById(R.id.date_text);
        errorText = findViewById(R.id.error_text);
        loadingText = findViewById(R.id.loading_text);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadRates();
    }

    private void loadRates() {
        loadingText.setVisibility(android.view.View.VISIBLE);

        ApiClient.getClient().getExchangeRates().enqueue(new Callback<List<ApiModels.NbpResponse>>() {
            @Override
            public void onResponse(Call<List<ApiModels.NbpResponse>> call, Response<List<ApiModels.NbpResponse>> response) {
                loadingText.setVisibility(android.view.View.GONE);
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ApiModels.NbpResponse table = response.body().get(0);
                    dateText.setText("Aktualne kursy średnie walut względem PLN (NBP) — " + table.effectiveDate);
                    allRates = table.rates;
                    displayCurrencies();
                } else {
                    showError("Nie udało się pobrać kursów.");
                }
            }

            @Override
            public void onFailure(Call<List<ApiModels.NbpResponse>> call, Throwable t) {
                loadingText.setVisibility(android.view.View.GONE);
                showError("Błąd połączenia: " + t.getLocalizedMessage());
            }
        });
    }

    private void displayCurrencies() {
        if (allRates == null) return;

        String[] popularCodes = {"EUR", "USD", "GBP", "CHF", "JPY", "CZK", "DKK", "NOK", "SEK"};

        List<ApiModels.NbpRate> popular = new ArrayList<>();
        List<ApiModels.NbpRate> others = new ArrayList<>();

        for (ApiModels.NbpRate rate : allRates) {
            boolean isPopular = false;
            for (String code : popularCodes) {
                if (code.equals(rate.code)) {
                    isPopular = true;
                    break;
                }
            }
            if (isPopular) popular.add(rate);
            else others.add(rate);
        }

        for (ApiModels.NbpRate rate : popular) {
            android.widget.Button chip = new android.widget.Button(this);
            chip.setText(rate.code + " - " + rate.currency);
            chip.setTextSize(12);
            chip.setPadding(16, 8, 16, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 8, 8);
            chip.setLayoutParams(params);
            chip.setBackgroundResource(R.drawable.chip_bg);
            chip.setOnClickListener(v -> toggleCurrency(rate.code, chip));
            popularContainer.addView(chip);
        }

        for (ApiModels.NbpRate rate : others) {
            android.widget.Button chip = new android.widget.Button(this);
            chip.setText(rate.code + " - " + rate.currency);
            chip.setTextSize(12);
            chip.setPadding(16, 8, 16, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 8, 8);
            chip.setLayoutParams(params);
            chip.setBackgroundResource(R.drawable.chip_bg);
            chip.setOnClickListener(v -> toggleCurrency(rate.code, chip));
            othersContainer.addView(chip);
        }

        for (ApiModels.NbpRate rate : popular.subList(0, Math.min(5, popular.size()))) {
            selectedCodes.add(rate.code);
        }

        refreshRatesDisplay();
    }

    private void toggleCurrency(String code, android.widget.Button chip) {
        if (selectedCodes.contains(code)) {
            selectedCodes.remove(code);
            chip.setAlpha(0.5f);
        } else {
            selectedCodes.add(code);
            chip.setAlpha(1.0f);
        }
        refreshRatesDisplay();
    }

    private void refreshRatesDisplay() {
        if (selectedCodes.isEmpty()) {
            ratesTable.setText("Wybierz waluty, aby zobaczyć kursy.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Waluta\tKod\tKurs (PLN)\n");

        for (ApiModels.NbpRate rate : allRates) {
            if (selectedCodes.contains(rate.code)) {
                sb.append(rate.currency).append("\t")
                        .append(rate.code).append("\t")
                        .append(String.format("%.4f", rate.mid)).append("\n");
            }
        }

        ratesTable.setText(sb.toString());
    }

    private void showError(String msg) {
        errorText.setText(msg);
        errorText.setVisibility(android.view.View.VISIBLE);
    }
}
