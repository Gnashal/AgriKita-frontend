package mobdev.agrikita.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import mobdev.agrikita.R;

public class CreateProduct extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, categoryDropdown, freshnessDropdown;
    Button btnSubmit, btnCancel, btnAddQty, btnMinusQty;
    EditText editName, editQuantity, editPrice;
    TextInputEditText productOriginField, productStorageField, productDescField;
    SwitchCompat switchOrganic, switchFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        unitDropdown = findViewById(R.id.unitDropdown);
        categoryDropdown = findViewById(R.id.categoryDropdown);
        freshnessDropdown = findViewById(R.id.freshnessDropdown);

        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnAddProduct);
        btnAddQty = findViewById(R.id.plusBtn);
        btnMinusQty = findViewById(R.id.minusBtn);

//         Text fields
        productOriginField = findViewById(R.id.productOriginField);
        productStorageField = findViewById(R.id.productStorageField);
        productDescField = findViewById(R.id.productDescField);
        editName = findViewById(R.id.productNameField);
        editQuantity = findViewById(R.id.qtyInput);
        editPrice = findViewById(R.id.productPriceField);

//        switches
        switchOrganic = findViewById(R.id.switchOrganic);
        switchFeature = findViewById(R.id.switchFeature);

        String[] units = {
                "Kg",       // Kilogram
                "g",        // Gram
                "Lb",       // Pound
                "Oz",       // Ounce
                "Ton",      // Metric Ton
                "Quintal",  // 100kg (used in agri trade)
                "L",        // Liter
                "ml",       // Milliliter
                "Bunch",    // Bundle of leafy produce
                "Dozen",    // 12-count
                "Pc",       // Piece
                "Bag",      // Sack (50kg, 100kg etc)
                "Crate",    // Box/crate of produce
                "Box",      // Small box
                "Tray",     // Flat containers (e.g. eggs)
                "Bucket",   // 5–10L container, common in dairy or fruit picking
                "Bundle",
                "Carton",
                "Stick",    // Sugarcane, bamboo
                "Head",     // Lettuce, cabbage
                "Cobs",     // Corn
                "Stalk",    // e.g. banana stalks
                "Sack"
        };
        String[] categories = {
                "Vegetables",
                "Fruits",
                "Grains",
                "Legumes",
                "Roots & Tubers",
                "Herbs",
                "Spices",
                "Leafy Greens",
                "Cereals",
                "Nuts & Seeds",
                "Dairy",
                "Livestock Products",
                "Poultry Products",
                "Fish & Seafood",
                "Flowers",
                "Timber & Wood",
                "Organic Compost",
                "Beverage Crops",     // e.g., coffee, tea
                "Oil Crops",          // e.g., sunflower, palm
                "Cash Crops",         // e.g., cotton, tobacco
                "Honey & Bee Products",
                "Eggs",
                "Mushrooms"
        };
        String[] freshnessProd = {
                "Harvested Today",
                "1–2 Days Old",
                "Fresh",
                "Moderate",
                "Stale",
                "Overripe",
                "Rotten"
        };

        setDropdown(unitDropdown, units);
        setDropdown(categoryDropdown, categories);
        setDropdown(freshnessDropdown, freshnessProd);

        setupNavbar();

        btnSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                String name = editName.getText().toString().trim();
                String unit = unitDropdown.getText().toString().trim();
                String category = categoryDropdown.getText().toString().trim();
                String freshness = freshnessDropdown.getText().toString().trim();
                int quantity = Integer.parseInt(editQuantity.getText().toString().trim());
                float price = Float.parseFloat(editPrice.getText().toString().trim());
                String origin = Objects.requireNonNull(productOriginField.getText()).toString().trim();
                String storage = Objects.requireNonNull(productStorageField.getText()).toString().trim();
                String description = Objects.requireNonNull(productDescField.getText()).toString().trim();
                boolean isOrganic = switchOrganic.isChecked();
                boolean isFeatured = switchFeature.isChecked();

                // 🔜 You can now store this in Firebase
                Log.d("VALID_INPUT", "Product Name: " + name);
                Log.d("VALID_INPUT", "Unit: " + unit);
                Log.d("VALID_INPUT", "Category: " + category);
                Log.d("VALID_INPUT", "Freshness: " + freshness);
                Log.d("VALID_INPUT", "Quantity: " + quantity);
                Log.d("VALID_INPUT", "Price: " + price);
                Log.d("VALID_INPUT", "Origin Location: " + origin);
                Log.d("VALID_INPUT", "Storage Info: " + storage);
                Log.d("VALID_INPUT", "Description: " + description);
                Log.d("VALID_INPUT", "Is Organic: " + isOrganic);
                Log.d("VALID_INPUT", "Is Featured: " + isFeatured);
            }
        });

        btnCancel.setOnClickListener(v -> finish());

        btnAddQty.setOnClickListener(v -> {
            int currentQty = getCurrentQuantity();
            currentQty++;
            editQuantity.setText(String.valueOf(currentQty));
        });

        btnMinusQty.setOnClickListener(v -> {
            int currentQty = getCurrentQuantity();
            if (currentQty > 0) { // No negative bananas allowed
                currentQty--;
                editQuantity.setText(String.valueOf(currentQty));
            }
        });

    }

    private int getCurrentQuantity() {
        String qtyStr = editQuantity.getText().toString().trim();
        try {
            return Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            return 0; // Default to 0 if it's empty or not a number
        }
    }

    private void setDropdown(AutoCompleteTextView view, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        view.setAdapter(adapter);
        view.setKeyListener(null); // disable manual typing
        view.setOnClickListener(v -> view.showDropDown());
    }

    private void setupNavbar() {
        Navbar navbarFragment = new Navbar();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.navbarContainer, navbarFragment);
        transaction.commit();
    }
    private boolean validateInputs() {
        if (editName.getText().toString().trim().isEmpty()) {
            editName.setError("Name is required");
            return false;
        }
        if (editPrice.getText().toString().trim().isEmpty()) {
            editPrice.setError("Price is required");
            return false;
        }
        if (unitDropdown.getText().toString().trim().isEmpty()) {
            unitDropdown.setError("Unit is required");
            return false;
        }
        if (categoryDropdown.getText().toString().trim().isEmpty()) {
            categoryDropdown.setError("Category is required");
            return false;
        }
        if (editQuantity.getText().toString().trim().isEmpty()) {
            editQuantity.setError("Quantity is required");
            return false;
        }
        if (Objects.requireNonNull(productOriginField.getText()).toString().trim().isEmpty()) {
            freshnessDropdown.setError("Origin field is required");
            return false;
        }
        if (freshnessDropdown.getText().toString().trim().isEmpty()) {
            freshnessDropdown.setError("Freshness level is required");
            return false;
        }
        if (Objects.requireNonNull(productStorageField.getText()).toString().trim().isEmpty()) {
            freshnessDropdown.setError("Storage field is required");
            return false;
        }
        if (Objects.requireNonNull(productDescField.getText()).toString().trim().isEmpty()) {
            freshnessDropdown.setError("Product description field is required");
            return false;
        }

        return true;
    }
}