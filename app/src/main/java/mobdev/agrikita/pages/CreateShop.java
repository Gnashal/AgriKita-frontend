package mobdev.agrikita.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.FragmentTransaction;


import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.ImagePickerUtil;
import mobdev.agrikita.controllers.ShopService;
import mobdev.agrikita.models.shop.CreateShopResponse;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.UserResponse;
import mobdev.agrikita.controllers.UserService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateShop extends AppCompatActivity {
    private ScrollView mainScrollView;
    private ImageView shopImageLayout, uploadedImage;
    private TextInputLayout shopNameLayout, shopAddressLayout, shopZipCodeLayout, shopDescLayout;
    private TextInputEditText shopNameField, shopAddressField, shopZipCodeField, shopDescField;
    private Button enterShopButton, reselectButton;
    Uri imageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    ShopService shopService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_shop);
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

        shopService = new ShopService(this);

        mainScrollView = findViewById(R.id.main);
        shopImageLayout = findViewById(R.id.shopImageLayout);
        uploadedImage = findViewById(R.id.uploaded_image);

        shopNameLayout = findViewById(R.id.shopNameLayout);
        shopAddressLayout = findViewById(R.id.shopAddressLayout);
        shopZipCodeLayout = findViewById(R.id.shopZipCodeLayout);
        shopDescLayout = findViewById(R.id.shopDescLayout);

        shopNameField = findViewById(R.id.shopNameField);
        shopAddressField = findViewById(R.id.shopAddressField);
        shopZipCodeField = findViewById(R.id.shopZipCodeField);
        shopDescField = findViewById(R.id.shopDescField);

        reselectButton = findViewById(R.id.reselectButton);
        enterShopButton = findViewById(R.id.enterShopButton);

        setupNavbar();

        shopImageLayout.setOnClickListener(v -> ImagePickerUtil.launchImagePicker(CreateShop.this, imagePickerLauncher));

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
                                .circleCrop()
                                .into(uploadedImage);

                        shopImageLayout.setVisibility(View.GONE);
                        uploadedImage.setVisibility(View.VISIBLE);
                        reselectButton.setVisibility(View.VISIBLE);
                    }
                }
        );

        enterShopButton.setOnClickListener(v -> {
            if (!validateInputs()) return;

            enterShopButton.setEnabled(false);
            enterShopButton.setText("Loading...");
            if (validateInputs()) {
                CurrentUser user = CurrentUser.getInstance(this);
                user.fetchUserData(CurrentUser.getInstance(this).getUid(), new UserService.FetchUserCallback() {
                    @Override
                    public void onSuccess(UserResponse response) {
                        String userId = user.getUid();
                        String shopNameStr = Objects.requireNonNull(shopNameField.getText()).toString().trim();
                        String shopAddr = Objects.requireNonNull(shopAddressField.getText()).toString().trim();
                        int zipCode = Integer.parseInt(Objects.requireNonNull(shopZipCodeField.getText()).toString().trim());
                        String shopDesc = Objects.requireNonNull(shopDescField.getText()).toString().trim();

                        MultipartBody.Part imagePart = ImagePickerUtil.prepareFilePart(CreateShop.this, "file", imageUri);
                        if (imagePart == null) {
                            Toast.makeText(CreateShop.this, "Failed to prepare image for upload.", Toast.LENGTH_SHORT).show();
                            resetSubmitButton();
                            return;
                        }
                        RequestBody ownerUid = toPlainText(userId);
                        RequestBody name = toPlainText(shopNameStr);
                        RequestBody address = toPlainText(shopAddr);
                        RequestBody zipCodeBody = toPlainText(String.valueOf(zipCode));
                        RequestBody shopDescription = toPlainText(shopDesc);

                        shopService.createShop(imagePart, ownerUid, name, address, zipCodeBody, shopDescription, new ShopService.CreateShopCallback() {
                            @Override
                            public void onSuccess(CreateShopResponse response) {
                                Toast.makeText(CreateShop.this, "Shop created: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent goToInventory = new Intent(CreateShop.this, InventoryManagement.class);
                                startActivity(goToInventory);
                                resetSubmitButton();
                                finish();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(CreateShop.this, "Failed to create shop: " + errorMessage, Toast.LENGTH_SHORT).show();
                                resetSubmitButton();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(CreateShop.this, "Failed to get shopID: " + errorMessage, Toast.LENGTH_SHORT).show();
                        resetSubmitButton();
                    }
                });
            }
        });
    }

    private RequestBody toPlainText(String value) {
        return RequestBody.create(value, MediaType.get("text/plain"));
    }

    private void setupNavbar() {
        Navbar navbarFragment = new Navbar();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.navbarContainer, navbarFragment);
        transaction.commit();
    }

    private boolean validateInputs() {
        if (shopNameField.getText().toString().trim().isEmpty()) {
            shopNameField.setError("Name is required");
            return false;
        }
        if (shopAddressField.getText().toString().trim().isEmpty()) {
            shopAddressField.setError("Address is required");
            return false;
        }
        if (shopZipCodeField.getText().toString().trim().isEmpty()) {
            shopZipCodeField.setError("Zip Code is required");
            return false;
        }
        if (shopDescField.getText().toString().trim().isEmpty()) {
            shopDescField.setError("Description is required");
            return false;
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void resetSubmitButton() {
        enterShopButton.setText("Submit");
        enterShopButton.setEnabled(true);
    }

    public void reselectImage(View view) {
        imageUri = null;

        uploadedImage.setVisibility(View.GONE);
        shopImageLayout.setVisibility(View.VISIBLE);

        reselectButton.setVisibility(View.GONE);
        Glide.with(this).clear(uploadedImage);
    }
}