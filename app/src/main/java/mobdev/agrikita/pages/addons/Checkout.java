package mobdev.agrikita.pages.addons;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import mobdev.agrikita.R;

public class Checkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Button proceedToPaymentButton = findViewById(R.id.proceed_to_payment_button);
        proceedToPaymentButton.setOnClickListener(v -> {
            EditText cardNumber = findViewById(R.id.card_number);
            EditText cardholderName = findViewById(R.id.cardholder_name);
            EditText expirationDate = findViewById(R.id.expiration_date);
            EditText cvv = findViewById(R.id.cvv);

            if (cardNumber.getText().toString().isEmpty() ||
                cardholderName.getText().toString().isEmpty() ||
                expirationDate.getText().toString().isEmpty() ||
                cvv.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill in all payment details.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}