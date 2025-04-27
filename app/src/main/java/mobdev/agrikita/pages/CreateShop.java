package mobdev.agrikita.pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import mobdev.agrikita.R;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.UserResponse;
import mobdev.agrikita.controllers.UserService;

public class CreateShop extends AppCompatActivity {
    private ScrollView mainScrollView;
    private ImageView shopImageLayout, uploadedImage;
    private TextInputLayout shopNameLayout, shopAddressLayout, shopZipCodeLayout, shopDescLayout;
    private TextInputEditText shopNameField, shopAddressField, shopZipCodeField, shopDescField;
    private Button browseCertificateButton, enterShopButton;
    Uri imageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_shop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        browseCertificateButton = findViewById(R.id.browseCertificateButton);
        enterShopButton = findViewById(R.id.enterShopButton);

        setupNavbar();

        shopImageLayout.setOnClickListener(v -> openFileChooser());

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
                    }
                }
        );

        enterShopButton.setOnClickListener(v -> {
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

//                        File imgProd = new File(getRealPathFromUri(imageUri));
//
//                        // 🔜 You can now store this in Firebase
//                        Log.d("VALID_INPUT", "User ID: " + userId);
//                        Log.d("VALID_INPUT", "Shop Name: " + shopNameStr);
//
//                        shopRequest = new CreateShop(userId, shopNameStr, imgProd);
//
//                        shopService.createShop(shopRequest);

                        Intent goToInventory = new Intent(CreateShop.this, InventoryManagement.class);
                        startActivity(goToInventory);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(CreateShop.this, "Failed to get shopID: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
}