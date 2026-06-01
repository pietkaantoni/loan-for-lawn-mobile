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

public class ContactActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, messageInput;
    private Button submitButton;
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
        submitButton = findViewById(R.id.btn_send);
        errorText = findViewById(R.id.error_text);
        successText = findViewById(R.id.success_text);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        submitButton.setOnClickListener(v -> submitMessage());
    }

    private void submitMessage() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String message = messageInput.getText().toString().trim();

        errorText.setVisibility(android.view.View.GONE);
        successText.setVisibility(android.view.View.GONE);

        if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            errorText.setText("Wypełnij wszystkie pola.");
            errorText.setVisibility(android.view.View.VISIBLE);
            return;
        }

        nameInput.setText("");
        emailInput.setText("");
        messageInput.setText("");

        successText.setText("Dziękujemy za wiadomość, " + name + "! Odpowiemy na adres " + email + ".");
        successText.setVisibility(android.view.View.VISIBLE);
    }
}
