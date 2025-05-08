package mobdev.agrikita.pages.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.AuthService;
import mobdev.agrikita.models.auth.response.ForgotPasswordResponse;

public class ForgotPassword extends AppCompatActivity {
    EditText emailInput;
    Button sendLink, resendLink, toOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat insetsController = new WindowInsetsControllerCompat(window, decorView);
        insetsController.setAppearanceLightStatusBars(true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
        AuthService.getInstance(this).sendEmail(email, new AuthService.ForgotPasswordCallback() {
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