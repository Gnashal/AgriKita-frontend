package mobdev.agrikita.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mobdev.agrikita.R;
import mobdev.agrikita.models.auth.AuthService;
import mobdev.agrikita.models.auth.LoginResponse;

public class Login extends AppCompatActivity {
    EditText emailField, passwordField;
    Button signIn, forgotPassword, toSignUp;
    ImageView googleSignIn, fbSignIn;

    AuthService authService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        authService = new AuthService(this);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        signIn = findViewById(R.id.btnSignIn);
        toSignUp = findViewById(R.id.btnSignUp);
        forgotPassword = findViewById(R.id.btnForgotPassword);
        googleSignIn = findViewById(R.id.googleBtn);
        fbSignIn = findViewById(R.id.fbBtn);

        String newEmail = getIntent().getStringExtra("email");
        if (newEmail != null){
            emailField.setText(newEmail);
        }


        signIn.setOnClickListener(v -> login());
        toSignUp.setOnClickListener(v -> navigateToSignUp());
    }

    private void login() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        authService.loginUser(email, password, new AuthService.LoginCallback()
        {
            @Override
            public void onSuccess(LoginResponse loginResponse) {
                saveAuthTokens(loginResponse);
                navigateToHomeScreen();
            }

            @Override
            public void onFailure(String errorMessage) {
                showErrorMessage(errorMessage);
            }
        });
    }

    private void navigateToSignUp() {
        startActivity(new Intent(this, SignUp.class));
        finish();
    }
    private void navigateToHomeScreen() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveAuthTokens(LoginResponse loginResponse) {
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idToken", loginResponse.getIdToken());
        editor.putString("refreshToken", loginResponse.getRefreshToken());
        editor.putString("localId", loginResponse.getLocalId());
        editor.apply();
    }
}