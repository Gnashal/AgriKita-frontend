package mobdev.agrikita.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mobdev.agrikita.R;
import mobdev.agrikita.models.auth.AuthService;
import mobdev.agrikita.models.auth.ForgotPasswordResponse;
import mobdev.agrikita.models.auth.SignupResponse;

public class ForgotPassword extends AppCompatActivity {
    EditText emailInput;
    Button sendLink, resendLink, toOTP;
    AuthService authService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        authService = new AuthService(this  );
        emailInput = findViewById(R.id.emailField);
        sendLink = findViewById(R.id.btnSendResetLink);
        resendLink = findViewById(R.id.resendEmailButton);
        toOTP = findViewById(R.id.toOTPbtn);

        sendLink.setOnClickListener(v -> sendRecoveryLink());
        toOTP.setOnClickListener(v -> toPhone());
    }

    private void sendRecoveryLink() {
        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        authService.sendEmail(email, new AuthService.ForgotPasswordCallback() {
            @Override
            public void onSuccess(ForgotPasswordResponse message) {
                toLoginSuccess(email, message);
            }

            @Override
            public void onFailure(String error) {
                showErrorMessage(error);
            }
        });
    }

    private void toLoginSuccess(String email, ForgotPasswordResponse message) {
        Toast.makeText(this, "Success: " + message.getMessage(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }
    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void toPhone() {
        startActivity(new Intent(this, VerifyPhoneNumber.class));
        finish();
    }
}