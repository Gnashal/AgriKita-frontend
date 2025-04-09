package mobdev.agrikita.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mobdev.agrikita.R;

public class LandingPage extends AppCompatActivity {
    Button toLogin, toAboutMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toLogin = findViewById(R.id.btnGetStarted);
        toAboutMore = findViewById(R.id.btnLearnMore);

        toLogin.setOnClickListener(v -> ToLogin());

        /*TODO: Charles add nya ko sa Learn More nga page, or sa kinsa ang mu add ani unya*/

    }

    private void ToLogin() {
        startActivity(new Intent(this, Login.class));
    }
}