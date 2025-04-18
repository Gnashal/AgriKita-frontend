package mobdev.agrikita.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import mobdev.agrikita.R;
import mobdev.agrikita.models.user.CurrentUser;

import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.app.Dialog;
import android.widget.Button;
import android.view.View;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;



public class Profile extends AppCompatActivity {
    TextView userName, userEmail, memberSinceText;

    LinearLayout profileLayout, securityLayout, preferencesLayout;
    MaterialButton btnProfile, btnSecurity, btnPreferences, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        memberSinceText = findViewById(R.id.memeberSinceText);

        profileLayout = findViewById(R.id.profileLayout);
        securityLayout = findViewById(R.id.securityLayout);
        preferencesLayout = findViewById(R.id.preferencesLayout);

        btnLogout = findViewById(R.id.logout_button);
        btnProfile = findViewById(R.id.btnProfile);
        btnSecurity = findViewById(R.id.btnSecurity);
        btnPreferences = findViewById(R.id.btnPreferences);

        Spinner languageSpinner = findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        userInformationSetup();
        btnLogout.setOnClickListener(v -> logout());

        btnProfile.setOnClickListener(v -> {
            profileLayout.setVisibility(View.VISIBLE);
            securityLayout.setVisibility(View.GONE);
            preferencesLayout.setVisibility(View.GONE);
            updateButtonColors(btnProfile);
        });

        btnSecurity.setOnClickListener(v -> {
            profileLayout.setVisibility(View.GONE);
            securityLayout.setVisibility(View.VISIBLE);
            preferencesLayout.setVisibility(View.GONE);
            updateButtonColors(btnSecurity);
        });

        btnPreferences.setOnClickListener(v -> {
            profileLayout.setVisibility(View.GONE);
            securityLayout.setVisibility(View.GONE);
            preferencesLayout.setVisibility(View.VISIBLE);
            updateButtonColors(btnPreferences);
        });

        ImageView btnEditProfilePic = findViewById(R.id.btnEditProfilePic);
        btnEditProfilePic.setOnClickListener(v -> showEditProfileDialog());
    }

    private void showEditProfileDialog() {
        Dialog dialog = new Dialog(Profile.this);
        dialog.setContentView(R.layout.dialog_edit_profile_picture);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> {
            // TODO: Save profile picture logic
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }



    // Helper method to update button colors
    private void updateButtonColors(MaterialButton selectedButton) {
        btnProfile.setBackgroundColor(getResources().getColor(R.color.light_gray)); // default color
        btnSecurity.setBackgroundColor(getResources().getColor(R.color.light_gray));
        btnPreferences.setBackgroundColor(getResources().getColor(R.color.light_gray));

        selectedButton.setBackgroundColor(getResources().getColor(R.color.white)); // selected button
    }

    private void logout() {
        SharedPreferences authPrefs = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor authEditor = authPrefs.edit();
        authEditor.remove("idToken");
        authEditor.remove("refreshToken");
        authEditor.remove("localId");
        authEditor.putBoolean("isLoggedIn", false);
        authEditor.apply();
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPrefs.edit();
        userEditor.remove("UserID");
        if (userPrefs.getBoolean("HasShop", false)) {
            userEditor.remove("ShopID");
        }
        userEditor.remove("HasShop");
        userEditor.apply();

        CurrentUser.clear();
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        finish();
    }
    private void userInformationSetup() {
        userName.setText(CurrentUser.getInstance().getUserName());
        userEmail.setText(CurrentUser.getInstance().getUserEmail());
    }
}
