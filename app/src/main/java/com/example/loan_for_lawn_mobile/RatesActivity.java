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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatesActivity extends AppCompatActivity {

    private LinearLayout ratesContainer;
    private TextView dateText, errorText, loadingText;

    private static final String[] POPULAR_CODES = {
            "EUR", "USD", "GBP", "CHF", "JPY", "CZK", "DKK", "NOK", "SEK", "CAD"
    };

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

        ratesContainer = findViewById(R.id.rates_container);
        dateText = findViewById(R.id.date_text);
        errorText = findViewById(R.id.error_text);
        loadingText = findViewById(R.id.loading_text);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadRates();
    }

    private void loadRates() {
        ratesContainer.removeAllViews();
        ratesContainer.setVisibility(android.view.View.GONE);
        errorText.setVisibility(android.view.View.GONE);
        loadingText.setVisibility(android.view.View.VISIBLE);

        ApiClient.getClient().getExchangeRates().enqueue(new Callback<List<ApiModels.NbpResponse>>() {
            @Override
            public void onResponse(Call<List<ApiModels.NbpResponse>> call, Response<List<ApiModels.NbpResponse>> response) {
                loadingText.setVisibility(android.view.View.GONE);
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ApiModels.NbpResponse table = response.body().get(0);
                    dateText.setText("Kursy średnie z dnia " + table.effectiveDate);
                    displayPopularRates(table.rates);
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

    private void displayPopularRates(List<ApiModels.NbpRate> allRates) {
        ratesContainer.removeAllViews();
        ratesContainer.setVisibility(android.view.View.VISIBLE);

        List<ApiModels.NbpRate> popular = new ArrayList<>();
        for (String code : POPULAR_CODES) {
            for (ApiModels.NbpRate rate : allRates) {
                if (code.equals(rate.code)) {
                    popular.add(rate);
                    break;
                }
            }
        }

        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.forLanguageTag("pl-PL"));
        fmt.setMinimumFractionDigits(4);
        fmt.setMaximumFractionDigits(4);

        for (int i = 0; i < popular.size(); i++) {
            ApiModels.NbpRate rate = popular.get(i);

            android.view.View row = getLayoutInflater().inflate(R.layout.item_rate, null);

            TextView codeText = row.findViewById(R.id.rate_code);
            TextView nameText = row.findViewById(R.id.rate_name);
            TextView valueText = row.findViewById(R.id.rate_value);

            codeText.setText(rate.code);
            nameText.setText(rate.currency);
            valueText.setText(fmt.format(rate.mid) + " PLN");

            android.view.View divider = new android.view.View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1));
            divider.setBackgroundColor(0xFFE0E0E0);

            ratesContainer.addView(row);
            if (i < popular.size() - 1) {
                ratesContainer.addView(divider);
            }
        }
    }

    private void showError(String msg) {
        ratesContainer.setVisibility(android.view.View.GONE);
        errorText.setText(msg);
        errorText.setVisibility(android.view.View.VISIBLE);
    }
}
