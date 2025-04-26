package mobdev.agrikita.pages;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentSanitizer;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import mobdev.agrikita.R;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.UpdateProfileImageResponse;
import mobdev.agrikita.models.user.UpdateUserResponse;
import mobdev.agrikita.models.user.UserResponse;
import mobdev.agrikita.controllers.UserService;

import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class Profile extends AppCompatActivity {
    private TextView userName, userEmail, memberSinceText;
    private EditText firstNameInput, lastNameInput, emailInput, phoneInput;
    private ImageView userProfilePicture;
    private SwipeRefreshLayout refresh;
    private LinearLayout profileLayout, securityLayout, preferencesLayout;
    private MaterialButton btnProfile, btnSecurity, btnPreferences, btnLogout, btnSaveChanges;
    private CurrentUser currentUser;

    private UserService userService;
    /*Permissions and Image handling*/
    private Uri imageUri;
    private ImageView profileImageView;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission granted, proceed to open the gallery
                    openGallery();
                } else {
                    Toast.makeText(Profile.this, "Permission denied to access media images", Toast.LENGTH_SHORT).show();
                }
            });

    // Register ActivityResultLauncher for image picking
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    profileImageView.setImageURI(imageUri);
                } else {
                    Toast.makeText(Profile.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            });

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
        currentUser = CurrentUser.getInstance(this);
        userService = new UserService(this);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        memberSinceText = findViewById(R.id.memeberSinceText);
        refresh = findViewById(R.id.swipeRefreshLayout);
        firstNameInput = findViewById(R.id.firstNameField);
        lastNameInput = findViewById(R.id.lastNameField);
        emailInput = findViewById(R.id.emailField);
        phoneInput = findViewById(R.id.phoneField);
        userProfilePicture = findViewById(R.id.userProfilePicture);
        firstNameInput = findViewById(R.id.firstNameField);
        lastNameInput = findViewById(R.id.lastNameField);
        emailInput = findViewById(R.id.emailField);
        phoneInput = findViewById(R.id.phoneField);

        profileLayout = findViewById(R.id.profileLayout);
        securityLayout = findViewById(R.id.securityLayout);
        preferencesLayout = findViewById(R.id.preferencesLayout);

        btnLogout = findViewById(R.id.logout_button);
        btnProfile = findViewById(R.id.btnProfile);
        btnSecurity = findViewById(R.id.btnSecurity);
        btnPreferences = findViewById(R.id.btnPreferences);
        btnSaveChanges = findViewById(R.id.saveChangesBtn);
        Spinner languageSpinner = findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        userInformationSetup();
        btnLogout.setOnClickListener(v -> logout());

        btnProfile.setOnClickListener(v -> {
            profileLayout.setVisibility(VISIBLE);
            securityLayout.setVisibility(GONE);
            preferencesLayout.setVisibility(GONE);
            updateButtonColors(btnProfile);
        });

        btnSecurity.setOnClickListener(v -> {
            profileLayout.setVisibility(GONE);
            securityLayout.setVisibility(VISIBLE);
            preferencesLayout.setVisibility(GONE);
            updateButtonColors(btnSecurity);
        });

        btnPreferences.setOnClickListener(v -> {
            profileLayout.setVisibility(GONE);
            securityLayout.setVisibility(GONE);
            preferencesLayout.setVisibility(VISIBLE);
            updateButtonColors(btnPreferences);
        });
        btnSaveChanges.setOnClickListener(v -> updateUserDetails());

        refresh.setOnRefreshListener(() -> {
            fetchUserData();
            refresh.setRefreshing(false);
        });

        ImageView btnEditProfilePic = findViewById(R.id.btnEditProfilePic);
        btnEditProfilePic.setOnClickListener(v -> showEditProfileDialog());
    }

    private void showEditProfileDialog() {
        Dialog dialog = new Dialog(Profile.this);
        dialog.setContentView(R.layout.dialog_edit_profile_picture);
        profileImageView = dialog.findViewById(R.id.profileImageView);
        LinearLayout selectPictureLayout = dialog.findViewById(R.id.selectPictureLayout);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        selectPictureLayout.setOnClickListener(v -> checkPermissionsAndOpenGallery());

        btnSave.setOnClickListener(v -> {
            handleImageUpdate();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void handleImageUpdate() {
        if (imageUri == null) {
            Toast.makeText(Profile.this, "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageUri != null) {
            File imageFile = new File(getRealPathFromURI(imageUri));
            userService.updateProfileImage(currentUser.getUid(), imageFile, new UserService.UpdateProfileImageCallback() {
                @Override
                public void onSuccess(UpdateProfileImageResponse updateProfileImageResponse) {
                    Log.v("UploadImage", "Upload Image Success");
                    Toast.makeText(Profile.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    fetchUserData();
                }
                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(Profile.this, "Update Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // Helper method to get the real file path from a URI
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
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
        String name = currentUser.getUserName();
        String email = currentUser.getUserEmail();
        if (name.isEmpty() && email.isEmpty()) {
            fetchUserData();
        } else {
            userName.setText(name);
            userEmail.setText(email);
            setProfilePic();
        }
    }

    private void setProfilePic() {
        if (currentUser.getImageUrl() != null && !currentUser.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentUser.getImageUrl())
                    .circleCrop()
                    .into(userProfilePicture);

        }
    }
    private void fetchUserData() {
        CurrentUser.getInstance(this).fetchUserData(CurrentUser.getInstance(this).getUid(), new UserService.FetchUserCallback() {
            @Override
            public void onSuccess(UserResponse userResponse) {
                Toast.makeText(Profile.this, "Fetch OK", Toast.LENGTH_SHORT).show();
                userInformationSetup();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(Profile.this, "Failed to fetch user: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserDetails() {
        String uid = CurrentUser.getInstance(this).getUid();
        String fullName = currentUser.getUserName() != null ? currentUser.getUserName().trim() : "";
        String firstNameCurrent = "";
        String lastNameCurrent = "";

        if (!fullName.isEmpty()) {
            String[] nameParts = fullName.split("\\s+", 2);
            firstNameCurrent = nameParts[0];
            lastNameCurrent = nameParts.length > 1 ? nameParts[1] : "";
        }
        String firstName = firstNameInput.getText().toString().isEmpty() ? firstNameCurrent : firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString().isEmpty() ? lastNameCurrent : lastNameInput.getText().toString();
        String email = emailInput.getText().toString().isEmpty() ? currentUser.getUserEmail() : emailInput.getText().toString();
        String phone = phoneInput.getText().toString().isEmpty() ? currentUser.getUserPhone() : phoneInput.getText().toString();
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(Profile.this, "All fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (profileLayout.getVisibility() == VISIBLE && securityLayout.getVisibility() == GONE) {
            userService.updateUserData(uid, firstName, lastName, email, phone, new UserService.UpdateUserCallback() {
                @Override
                public void onSuccess(UpdateUserResponse updateUserResponse) {
                    Toast.makeText(Profile.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    firstNameInput.setText("");
                    lastNameInput.setText("");
                    emailInput.setText("");
                    phoneInput.setText("");

                    startActivity(new Intent(Profile.this, Profile.class));
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(Profile.this, "Update Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(Profile.this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkPermissionsAndOpenGallery() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, open the gallery
                openGallery();
            } else {
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            // For Android versions below 14, no need for permissions to open gallery
            openGallery();
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);  // Start the gallery activity
    }

}
