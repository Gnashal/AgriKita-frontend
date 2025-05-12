package mobdev.agrikita.pages.addons.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.AddressController;
import mobdev.agrikita.controllers.OrderService;
import mobdev.agrikita.controllers.ShoppingCartController;
import mobdev.agrikita.models.address.Address;
import mobdev.agrikita.models.order.OrderItem;
import mobdev.agrikita.models.order.request.CreateOrderRequest;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.pages.marketplace.Marketplace;

public class Checkout extends AppCompatActivity {

    private EditText nameField, phoneNumberField, regionField, provinceField, cityField, streetField, postalCodeField;
    private CheckBox useDefaultCheck;
    private RadioButton codCheck, gcashCheck, mayaCheck, maximCheck, lalamoveCheck, ninjaVanCheck;
    private Button placeOrderButton;
    private TextView subtotal, shippingFee, total;
    private String manualName, manualPhone, manualRegion, manualProvince, manualCity, manualStreet, manualPostalCode ,logistics, paymentMethod;

    private OrderService orderService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

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
        nameField = findViewById(R.id.nameField);
        phoneNumberField = findViewById(R.id.phoneNumberField);
        regionField = findViewById(R.id.regionField);
        provinceField = findViewById(R.id.provinceField);
        cityField = findViewById(R.id.cityField);
        streetField = findViewById(R.id.streetField);
        postalCodeField = findViewById(R.id.postalCodeField);
        useDefaultCheck = findViewById(R.id.useDefaultCheck);
        codCheck = findViewById(R.id.codCheck);
        gcashCheck = findViewById(R.id.gcashCheck);
        mayaCheck = findViewById(R.id.mayaCheck);
        maximCheck = findViewById(R.id.maximCheck);
        lalamoveCheck = findViewById(R.id.lalamoveCheck);
        ninjaVanCheck = findViewById(R.id.ninjaVanCheck);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        subtotal = findViewById(R.id.subtotalText);
        shippingFee = findViewById(R.id.shippingFeeText);
        total = findViewById(R.id.totalText);
        orderService = new OrderService(Checkout.this);
        setupTotalViews();
        useDefaultCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                manualName = nameField.getText().toString();
                manualPhone = phoneNumberField.getText().toString();
                manualRegion = regionField.getText().toString();
                manualProvince = provinceField.getText().toString();
                manualCity = cityField.getText().toString();
                manualStreet = streetField.getText().toString();
                manualPostalCode = postalCodeField.getText().toString();
                applyDefaultAddress();
            } else {
                revertToManualInput();
            }
        });
        placeOrderButton.setOnClickListener(v -> placeOrder(setupTotalViews()));
    }

    private void applyDefaultAddress() {
        Address address = AddressController.getInstance(this)
                .getDefaultAddressForUid(CurrentUser.getInstance(this).getUid());

        if (address != null) {
            nameField.setText(CurrentUser.getInstance(this).getUserName());
            phoneNumberField.setText(address.getPhone());
            regionField.setText(address.getRegion());
            provinceField.setText(address.getProvince());
            cityField.setText(address.getCity());
            streetField.setText(address.getStreetName());
            postalCodeField.setText(address.getZipCode());
        } else {
            Toast.makeText(this, "No default address found.", Toast.LENGTH_SHORT).show();
            useDefaultCheck.setChecked(false);
        }
    }

    private void revertToManualInput() {
        nameField.setText(manualName != null ? manualName : "");
        phoneNumberField.setText(manualPhone != null ? manualPhone : "");
        regionField.setText(manualRegion != null ? manualRegion : "");
        provinceField.setText(manualProvince != null ? manualProvince : "");
        cityField.setText(manualCity != null ? manualCity : "");
        streetField.setText(manualStreet != null ? manualStreet : "");
        postalCodeField.setText(manualPostalCode != null ? manualPostalCode: "");
    }

    private boolean determinePaymentMethod() {
        if (codCheck.isChecked()) {
            paymentMethod = "cod";
        } else if (gcashCheck.isChecked()) {
            paymentMethod = "gcash";
        } else if (mayaCheck.isChecked()) {
            paymentMethod = "maya";
        } else {
            Toast.makeText(this, "Please select a payment method.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean determineLogistics() {
        if (maximCheck.isChecked()) {
            logistics = "maxim";
        } else if (lalamoveCheck.isChecked()) {
            logistics = "lalamove";
        } else if (ninjaVanCheck.isChecked()) {
            logistics = "ninjavan";
        } else {
            Toast.makeText(this, "Please select a logistics service.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private double setupTotalViews() {
        double subtotal = getIntent().getDoubleExtra("subtotal", 0.00);
        double shipping = getIntent().getDoubleExtra("shipping", 0.00);
        double total = getIntent().getDoubleExtra("total", 0.00);

        this.subtotal.setText("Subtotal: ₱ " + String.format("%.2f", subtotal));
        this.shippingFee.setText("Shipping Fee: ₱ " + String.format("%.2f", shipping));
        this.total.setText("Total: ₱ " + String.format("%.2f", total));

        return total;
    }
    private boolean areFieldsValid() {
        if (nameField.getText().toString().isEmpty() ||
                phoneNumberField.getText().toString().isEmpty() ||
                regionField.getText().toString().isEmpty() ||
                provinceField.getText().toString().isEmpty() ||
                cityField.getText().toString().isEmpty() ||
                streetField.getText().toString().isEmpty()) {

            Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean runChecks() {
        return areFieldsValid() && determinePaymentMethod() && determineLogistics();
    }

    private void placeOrder(double total) {
        if (!runChecks()) {
            return;
        }
        Address address = AddressController.getInstance(this)
                .getDefaultAddressForUid(CurrentUser.getInstance(this).getUid());
        List<OrderItem> orderItemList = ShoppingCartController.getInstance().convertAndClearCart();
        String buyerId = CurrentUser.getInstance(this).getUid();
        String buyerName = CurrentUser.getInstance(this).getUserName();
        CreateOrderRequest req = new CreateOrderRequest(buyerId, buyerName, logistics, paymentMethod, total, orderItemList, address);
        orderService.createOrder(req, new OrderService.CreateOrderCallback() {
            @Override
            public void onSuccess(String message, String orderId) {
                runOnUiThread(() -> {
                    Toast.makeText(Checkout.this, message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Checkout.this, OrderConfirmation.class)
                            .putExtra("orderId", orderId));
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(Checkout.this, error, Toast.LENGTH_LONG).show());
            }
        });
    }
}