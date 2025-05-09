package mobdev.agrikita.pages.shop;

import static mobdev.agrikita.controllers.ImagePickerUtil.prepareFilePart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import mobdev.agrikita.R;
import mobdev.agrikita.models.products.request.CreateProductRequest;
import mobdev.agrikita.controllers.ProductService;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.response.UserResponse;
import mobdev.agrikita.controllers.UserService;
import mobdev.agrikita.controllers.ImagePickerUtil;
import mobdev.agrikita.pages.addons.Navbar;
import okhttp3.MultipartBody;

public class CreateProduct extends AppCompatActivity {
    AutoCompleteTextView unitDropdown, categoryDropdown, freshnessDropdown;
    Button btnSubmit, btnCancel, btnAddQty, btnMinusQty, reselectButton;
    EditText editName, editQuantity, editPrice;
    TextInputEditText productOriginField, productStorageField, productDescField;
    SwitchCompat switchOrganic, switchFeature;
    ProductService productService;

    ImageView imageView, uploadedImage;
    Button chooseFileButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    View uploadIcon, uploadBox;
    private ActivityResultLauncher<Intent> imagePickerLauncher;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_product);
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

        productService = new ProductService(this);

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

        chooseFileButton = findViewById(R.id.addImgButton);
        uploadIcon = findViewById(R.id.upload_icon);
        uploadBox = findViewById(R.id.upload_box);
        uploadedImage = findViewById(R.id.uploaded_image);
        reselectButton = findViewById(R.id.reselectButton);

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

        setDropdown(unitDropdown, units);
        setDropdown(categoryDropdown, categories);
        setDropdown(freshnessDropdown, freshnessProd);

        setupNavbar();

        imagePickerLauncher = registerForActivityResult(
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

                        imageUri = selectedUri;

                        Glide.with(this)
                                .load(imageUri)
                                .transform(new CenterCrop(), new RoundedCorners(30))
                                .into(uploadedImage);

                        uploadBox.setVisibility(View.GONE);
                        uploadedImage.setVisibility(View.VISIBLE);
                        reselectButton.setVisibility(View.VISIBLE);
                        Log.d("Image Upload", "Uploaded image is visible: " + uploadedImage.getVisibility());
                    }
                }
        );


        btnSubmit.setOnClickListener(v -> {
            if (!validateInputs()) return;

            btnSubmit.setEnabled(false);
            btnSubmit.setText("Loading...");

            CurrentUser user = CurrentUser.getInstance(this);
            user.fetchUserData(user.getUid(), new UserService.FetchUserCallback() {
                @Override
                public void onSuccess(UserResponse resp) {
                    MultipartBody.Part imgPart = prepareFilePart(CreateProduct.this , "image", imageUri);

                    if (imgPart == null) {
                        Toast.makeText(CreateProduct.this, "Failed to prepare image file. Please try again.", Toast.LENGTH_SHORT).show();
                        resetSubmitButton();
                        return;
                    }

                    // Handling image upload using a background task
                    CompletableFuture<Void> uploadFuture = CompletableFuture.runAsync(() -> {
                        productService.uploadImage(imgPart, new ProductService.UploadCallback() {
                            @Override
                            public void onSuccess(String imageUrl) {
                                runOnUiThread(() -> {
                                    handleCreateProduct(imageUrl);
                                    resetSubmitButton();
                                });
                            }

                            @Override
                            public void onError(String errorMessage) {
                                runOnUiThread(() -> {
                                    Toast.makeText(CreateProduct.this, "Image upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    resetSubmitButton();
                                });
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                runOnUiThread(() -> {
                                    Toast.makeText(CreateProduct.this, "Failed to get imgPart: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    resetSubmitButton();
                                });
                            }
                        });
                    });

                    uploadFuture.thenRun(() -> {
                        Log.d("UPLOAD_COMPLETE", "Product creation will continue after the image upload is completed.");
                        // You can add any other logic that depends on the upload being finished here
                    });

                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(CreateProduct.this, "Failed to get shopID: " + errorMessage, Toast.LENGTH_SHORT).show();
                    resetSubmitButton();
                }
            });
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

        chooseFileButton.setOnClickListener(v -> ImagePickerUtil.launchImagePicker(CreateProduct.this, imagePickerLauncher));

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
        float priceValue;
        try {
            priceValue = Float.parseFloat(editPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            editPrice.setError("Invalid price format");
            return false;
        }

        if (priceValue >= 10000000) {
            editPrice.setError("Price can't be that high");
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

    private void handleCreateProduct(String imageUrl) {
        CurrentUser user = CurrentUser.getInstance(this);
        String shopId = user.getShopId();
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

        // Log the product details
        Log.d("VALID_INPUT", "Shop ID: " + shopId);
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

        CreateProductRequest prodRequest = new CreateProductRequest(shopId, imageUrl,
                name, price, unit, category, quantity, origin, freshness,
                storage, description, isOrganic, isFeatured, "available"
        );
        productService.createProduct(prodRequest);

        Intent goBack = new Intent(CreateProduct.this, InventoryManagement.class);
        startActivity(goBack);
    }

    @SuppressLint("SetTextI18n")
    private void resetSubmitButton() {
        btnSubmit.setText("Submit");
        btnSubmit.setEnabled(true);
    }

    public void reselectImage(View view) {
        imageUri = null;

        uploadedImage.setVisibility(View.GONE);
        uploadBox.setVisibility(View.VISIBLE);

        reselectButton.setVisibility(View.GONE);
        Glide.with(this).clear(uploadedImage);
    }
}