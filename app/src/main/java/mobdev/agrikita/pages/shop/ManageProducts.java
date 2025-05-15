package mobdev.agrikita.pages.shop;

import static mobdev.agrikita.controllers.ImagePickerUtil.prepareFilePart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.ImagePickerUtil;
import mobdev.agrikita.controllers.ProductService;
import mobdev.agrikita.controllers.UserService;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.products.request.UpdateProductRequest;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.response.UserResponse;
import mobdev.agrikita.pages.addons.Navbar;
import okhttp3.MultipartBody;

public class ManageProducts extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, categoryDropdown, freshnessDropdown, statusDropdown;
    EditText nameField, priceField, stockField, locationField, descriptionField;
    TextInputEditText productStorageField;
    SwitchCompat organicSwitch, featuredSwitch;
    ImageView productImage;
    Button updateButton, deleteButton, reselectBtn, addStock, minusStock, btnCancel, updateImgButton;
    Uri currentImageUri = null;
    LinearLayout uploadBox;
    ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_products);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupNavbar();

        productService = new ProductService(this);

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
                "Fresh From The Farm",
                "1–2 Days Old",
                "Fresh",
                "Moderate",
                "Stale",
                "Overripe",
                "Rotten"
        };
        String[] statusProd = {
                "available",
                "unavailable"
        };

        nameField = findViewById(R.id.productNameField);
        priceField = findViewById(R.id.productPriceField);
        stockField = findViewById(R.id.qtyInput);
        locationField = findViewById(R.id.productOriginField);
        descriptionField = findViewById(R.id.productDescField);
        unitDropdown = findViewById(R.id.unitDropdown);
        categoryDropdown = findViewById(R.id.categoryDropdown);
        freshnessDropdown = findViewById(R.id.freshnessDropdown);
        productStorageField = findViewById(R.id.productStorageField);
        statusDropdown = findViewById(R.id.statusDropdown);
        organicSwitch = findViewById(R.id.switchOrganic);
        featuredSwitch = findViewById(R.id.switchFeature);
        productImage = findViewById(R.id.uploaded_image);
        reselectBtn = findViewById(R.id.reselectButton);
        updateButton = findViewById(R.id.btnUpdateProduct);
        deleteButton = findViewById(R.id.btnDeleteProduct);
        uploadBox = findViewById(R.id.upload_box);
        addStock = findViewById(R.id.plusBtn);
        minusStock = findViewById(R.id.minusBtn);
        btnCancel = findViewById(R.id.btnCancel);
        updateImgButton = findViewById(R.id.updateImgButton);

        setDropdown(unitDropdown, units);
        setDropdown(categoryDropdown, categories);
        setDropdown(freshnessDropdown, freshnessProd);
        setDropdown(statusDropdown, statusProd);

        Products product = (Products) getIntent().getSerializableExtra("product");
        assert product != null;
        loadProductData(product);

        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedUri = result.getData().getData();

                        assert selectedUri != null;
                        String mimeType = getContentResolver().getType(selectedUri);
                        if (mimeType == null || !mimeType.startsWith("image/")) {
                            Toast.makeText(this, "Selected file is not a valid image.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        currentImageUri = selectedUri;

                        Glide.with(this)
                                .load(currentImageUri)
                                .transform(new CenterCrop(), new RoundedCorners(30))
                                .into(productImage);

                        uploadBox.setVisibility(View.GONE);
                        productImage.setVisibility(View.VISIBLE);
                        reselectBtn.setVisibility(View.VISIBLE);
                        Log.d("Image Upload", "Uploaded image is visible: " + productImage.getVisibility());
                    }
                }
        );

        btnCancel.setOnClickListener(v -> finish());

        addStock.setOnClickListener(v -> {
            int currentQty = getCurrentQuantity();
            currentQty++;
            stockField.setText(String.valueOf(currentQty));
        });

        minusStock.setOnClickListener(v -> {
            int currentQty = getCurrentQuantity();
            if (currentQty > 0) { // No negative bananas allowed
                currentQty--;
                stockField.setText(String.valueOf(currentQty));
            }
        });

        updateButton.setOnClickListener(v -> {
            if (!validateInputs()) return;

            updateButton.setEnabled(false);
            updateButton.setText("Loading...");

            CurrentUser user = CurrentUser.getInstance(this);
            user.fetchUserData(user.getUid(), new UserService.FetchUserCallback() {
                @Override
                public void onSuccess(UserResponse resp) {
                    if (currentImageUri != null) {
                        MultipartBody.Part imgPart = prepareFilePart(ManageProducts.this, "newImage", currentImageUri);
                        if (imgPart == null) {
                            Toast.makeText(ManageProducts.this, "Failed to prepare image file. Please try again.", Toast.LENGTH_SHORT).show();
                            resetSubmitButton();
                            return;
                        }
                        // Upload image first, then update product
                        CompletableFuture.runAsync(() -> {
                            productService.updateProductImage(product.getProductID(), product.getImageUrl(), imgPart, new ProductService.UpdateImageCallback() {
                                @Override
                                public void onSuccess(String message, String newImageUrl) {
                                    runOnUiThread(() -> {
                                        handleUpdateProduct(newImageUrl, product.getProductID());
                                        resetSubmitButton();
                                    });
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    runOnUiThread(() -> {
                                        Toast.makeText(ManageProducts.this, "Image upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                        resetSubmitButton();
                                    });
                                }
                            });
                        });
                    } else {
                        runOnUiThread(() -> {
                            handleUpdateProduct(product.getImageUrl(), product.getProductID());
                            resetSubmitButton();
                        });
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(ManageProducts.this, "Failed to get shopID: " + errorMessage, Toast.LENGTH_SHORT).show();
                        resetSubmitButton();
                    });
                }
            });
        });

        updateImgButton.setOnClickListener(v -> ImagePickerUtil.launchImagePicker(ManageProducts.this, imagePickerLauncher));
    }

    void loadProductData(Products product) {
        nameField.setText(product.getProductName());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStockQuantity()));
        locationField.setText(product.getOriginLocation());
        descriptionField.setText(product.getDescription());

        // Set AutoCompleteTextView dropdowns
        setDropdownToValue(unitDropdown, product.getMeasuringUnit());
        setDropdownToValue(categoryDropdown, product.getCategory());
        setDropdownToValue(freshnessDropdown, product.getFreshnessRate());
        setDropdownToValue(statusDropdown, product.getStatus());

        // Text field for storage
        productStorageField.setText(product.getStorage());

        organicSwitch.setChecked(product.isOrganic());
        featuredSwitch.setChecked(product.isFeatured());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.agrikita_logo)
                .error(R.drawable.error_no_image)
                .transform(new CenterCrop(), new RoundedCorners(30));

        Glide.with(this)
                .load(product.getImageUrl())
                .apply(requestOptions)
                .into(productImage);

        uploadBox.setVisibility(View.GONE);
        productImage.setVisibility(View.VISIBLE);
        reselectBtn.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void resetSubmitButton() {
        updateButton.setText("Update");
        updateButton.setEnabled(true);
    }

    private void setDropdown(AutoCompleteTextView view, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        view.setAdapter(adapter);
        view.setKeyListener(null); // disable manual typing
        view.setOnClickListener(v -> view.showDropDown());
    }

    public void reselectImage(View view) {
        currentImageUri = null;

        productImage.setVisibility(View.GONE);
        uploadBox.setVisibility(View.VISIBLE);

        reselectBtn.setVisibility(View.GONE);
        Glide.with(this).clear(productImage);
    }

    void setDropdownToValue(AutoCompleteTextView dropdown, String value) {
        dropdown.setText(value, false);
    }

    private void setupNavbar() {
        Navbar navbarFragment = new Navbar();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.navbarContainer, navbarFragment);
        transaction.commit();
    }

    private boolean validateInputs() {
        if (nameField.getText().toString().trim().isEmpty()) {
            nameField.setError("Name is required");
            return false;
        }
        if (priceField.getText().toString().trim().isEmpty()) {
            priceField.setError("Price is required");
            return false;
        }
        float priceValue;
        try {
            priceValue = Float.parseFloat(priceField.getText().toString().trim());
        } catch (NumberFormatException e) {
            priceField.setError("Invalid price format");
            return false;
        }

        if (priceValue >= 10000000) {
            priceField.setError("Price can't be that high");
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
        if (stockField.getText().toString().trim().isEmpty()) {
            stockField.setError("Quantity is required");
            return false;
        }
        if (statusDropdown.getText().toString().trim().isEmpty()) {
            stockField.setError("Status is required");
            return false;
        }
        if (Objects.requireNonNull(locationField.getText()).toString().trim().isEmpty()) {
            locationField.setError("Origin field is required");
            return false;
        }
        if (freshnessDropdown.getText().toString().trim().isEmpty()) {
            freshnessDropdown.setError("Freshness level is required");
            return false;
        }
        if (Objects.requireNonNull(productStorageField.getText()).toString().trim().isEmpty()) {
            productStorageField.setError("Storage field is required");
            return false;
        }
        if (Objects.requireNonNull(descriptionField.getText()).toString().trim().isEmpty()) {
            descriptionField.setError("Product description field is required");
            return false;
        }
        return true;
    }

    private int getCurrentQuantity() {
        String qtyStr = stockField.getText().toString().trim();
        try {
            return Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            return 0; // Default to 0 if it's empty or not a number
        }
    }

    private void handleUpdateProduct(String imageUrl, String productID) {
        String name = nameField.getText().toString().trim();
        String unit = unitDropdown.getText().toString().trim();
        String category = categoryDropdown.getText().toString().trim();
        String freshness = freshnessDropdown.getText().toString().trim();
        int quantity = Integer.parseInt(stockField.getText().toString().trim());
        float price = Float.parseFloat(priceField.getText().toString().trim());
        String origin = Objects.requireNonNull(locationField.getText()).toString().trim();
        String storage = Objects.requireNonNull(productStorageField.getText()).toString().trim();
        String description = Objects.requireNonNull(descriptionField.getText()).toString().trim();
        String status = Objects.requireNonNull(statusDropdown.getText()).toString().trim();

        boolean isOrganic = organicSwitch.isChecked();
        boolean isFeatured = featuredSwitch.isChecked();

        // Log the product details
        Log.d("VALID_INPUT", "Prod ID: " + productID);
        Log.d("VALID_INPUT", "Product Image Url: " + imageUrl);
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
        Log.d("VALID_INPUT", "Status: " + status);

        UpdateProductRequest updateRequest = new UpdateProductRequest(
                productID, name, price, unit, category, quantity,
                origin, freshness, storage, description, isOrganic, isFeatured, status
        );

        productService.updateProduct(updateRequest, new ProductService.UpdateProductCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(ManageProducts.this, message, Toast.LENGTH_SHORT).show();
                    resetSubmitButton();
                    Intent goBack = new Intent(ManageProducts.this, InventoryManagement.class);
                    startActivity(goBack);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(ManageProducts.this, "Update failed: " + t, Toast.LENGTH_SHORT).show();
                    resetSubmitButton();
                });
            }
        });
    }
}