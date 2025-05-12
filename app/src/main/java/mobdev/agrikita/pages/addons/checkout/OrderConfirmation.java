package mobdev.agrikita.pages.addons.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mobdev.agrikita.R;
import mobdev.agrikita.pages.index.Home;

public class OrderConfirmation extends AppCompatActivity {
    TextView ordeRef;
    Button returnHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_confirmation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ordeRef = findViewById(R.id.orderNumber);
        returnHome = findViewById(R.id.returnHomeButton);

        ordeRef.setText(getIntent().getStringExtra("orderId"));
        returnHome.setOnClickListener(v -> {
           startActivity(new Intent(OrderConfirmation.this, Home.class));
           finish();
        });
    }
}