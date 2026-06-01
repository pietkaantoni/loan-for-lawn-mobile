package com.example.loan_for_lawn_mobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loan_for_lawn_mobile.data.api.ApiClient;
import com.example.loan_for_lawn_mobile.data.api.ApiModels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, messageInput;
    private Button sendButton;
    private TextView errorText, successText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        messageInput = findViewById(R.id.input_message);
        sendButton = findViewById(R.id.btn_send);
        errorText = findViewById(R.id.error_text);
        successText = findViewById(R.id.success_text);

        sendButton.setOnClickListener(v -> sendMessage());

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void sendMessage() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String message = messageInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            showError("Wypełnij wszystkie pola.");
            return;
        }

        errorText.setVisibility(android.view.View.GONE);
        sendButton.setEnabled(false);
        sendButton.setText("Wysyłanie...");

        ApiModels.ContactRequest request = new ApiModels.ContactRequest(name, email, message);
        ApiClient.getBackendService().sendContact(request).enqueue(new Callback<ApiModels.ContactResponse>() {
            @Override
            public void onResponse(Call<ApiModels.ContactResponse> call, Response<ApiModels.ContactResponse> response) {
                sendButton.setEnabled(true);
                sendButton.setText("Wyślij wiadomość");
                if (response.isSuccessful()) {
                    successText.setVisibility(android.view.View.VISIBLE);
                    nameInput.setText("");
                    emailInput.setText("");
                    messageInput.setText("");
                    nameInput.setVisibility(android.view.View.GONE);
                    emailInput.setVisibility(android.view.View.GONE);
                    messageInput.setVisibility(android.view.View.GONE);
                    sendButton.setVisibility(android.view.View.GONE);
                    findViewById(R.id.contact_form_title).setVisibility(android.view.View.GONE);
                } else {
                    showError("Nie udało się wysłać wiadomości.");
                }
            }

            @Override
            public void onFailure(Call<ApiModels.ContactResponse> call, Throwable t) {
                sendButton.setEnabled(true);
                sendButton.setText("Wyślij wiadomość");
                showError("Błąd połączenia: " + t.getLocalizedMessage());
            }
        });
    }

    private void showError(String msg) {
        errorText.setText(msg);
        errorText.setVisibility(android.view.View.VISIBLE);
    }
}
