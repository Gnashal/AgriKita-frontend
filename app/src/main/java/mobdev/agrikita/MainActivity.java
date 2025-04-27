package mobdev.agrikita;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import mobdev.agrikita.pages.Home;
import mobdev.agrikita.pages.LandingPage;
import mobdev.agrikita.pages.Login;
import mobdev.agrikita.pages.Navbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String tokenID = prefs.getString("idToken", "");

        if (tokenID.isEmpty() || tokenID.isBlank()) {
            toLogin();
        } else {
            toHome();
        }

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
