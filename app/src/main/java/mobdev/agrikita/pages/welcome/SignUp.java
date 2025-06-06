package mobdev.agrikita.pages.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.AuthService;
import mobdev.agrikita.models.auth.response.SignupResponse;

public class SignUp extends AppCompatActivity {
    EditText emailInput, usernameInput, nameInput, phoneInput, passwordInput, confirmPassInput;
    CheckBox checkbox;
    Button signUp, toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

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

        emailInput = findViewById(R.id.emailInput);
        usernameInput = findViewById(R.id.usernameInput);
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.contactInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPassInput = findViewById(R.id.confirmPasswordInput);
        signUp = findViewById(R.id.signupBtn);
        toLogin = findViewById(R.id.toLoginPage);
        checkbox = findViewById(R.id.termsCheckbox);

        signUp.setOnClickListener(v -> signUp());
        toLogin.setOnClickListener(v -> toLogin());
    }

    public void signUp(){
        String email = emailInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPassInput.getText().toString().trim();

        if (!isFieldMissing(email, username, name, phone, password, confirmPassword)) return;
        if (!isPasswordsSame(password, confirmPassword)) return;
        if (!isValidPassword(password)) return;
        if (!isCheckboxChecked()) return;


        AuthService.getInstance(this).signupUser(email, username, name, phone, password, new AuthService.SignupCallback() {
            @Override
            public void onSuccess(SignupResponse signupResponse) {
                toLoginSuccess(signupResponse);
            }

            @Override
            public void onFailure(String errorMessage) {
                showErrorMessage(errorMessage);
            }
        });
    }
    public boolean isPasswordsSame(String pass1, String pass2) {
        if (!pass1.equals(pass2)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean isValidPassword(String password) {
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be longer than 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.contains(" ") || password.contains("*") || password.contains("#") || password.contains("$")) {
            Toast.makeText(this, "Password must not contain special characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean isCheckboxChecked() {
        if (!checkbox.isChecked()) {
            Toast.makeText(this, "Please check the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isFieldMissing(String email, String username, String name, String phone, String password, String confirmPass) {
        if (email.isEmpty() || username.isEmpty() || name.isEmpty() ||
                phone.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void toLoginSuccess(SignupResponse signupResponse) {
        Intent intent = new Intent(SignUp.this, Login.class);
        intent.putExtra("email", signupResponse.getEmail());
        startActivity(intent);
        finish();
    }
    private void toLogin() {
        startActivity(new Intent(this, Login.class));
        finish();
    }
}