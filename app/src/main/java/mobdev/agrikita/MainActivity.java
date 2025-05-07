package mobdev.agrikita;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import mobdev.agrikita.controllers.AuthService;
import mobdev.agrikita.pages.Home;
import mobdev.agrikita.pages.LandingPage;
import mobdev.agrikita.pages.Login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean("isFirstTime", true);
        String tokenID = prefs.getString("idToken", "");

        if (isFirstTime) {
            prefs.edit().putBoolean("isFirstTime", false).apply();
            toLanding();
        } else if (tokenID.isEmpty()) {
            toLogin();
        } else {
            AuthService.getInstance(this).validateAndNavigate(new AuthService.RefreshTokenCallback() {
                @Override
                public void onSuccess(boolean ok) {
                    if (ok) {
                        toHome();
                    } else {
                        toLogin();
                    }
                }

                @Override
                public void onFailure(String error) {
                    toLogin();
                }
            });
        }
    }

    private void toLanding() {
        startActivity(new Intent(this, LandingPage.class));
        finish();
    }
    private void toLogin() {
        startActivity(new Intent(this, Login.class));
        finish();
    }
    private void toHome() {
        startActivity(new Intent(this, Home.class));
        finish();
    }
}
