package mobdev.agrikita.pages.addons.address;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.AddressController;
import mobdev.agrikita.models.address.Address;
import mobdev.agrikita.models.user.CurrentUser;

public class AddAddress extends AppCompatActivity {
    EditText nameInput, phoneInput, regionInput, provinceInput, cityInput, barangayInput, streetInput, zipCodeInput;
    SwitchCompat isDefaultSw;
    Button submitBtn;
    ImageButton backBtn;
    ToggleButton workLabel, homeLabel, otherLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nameInput = findViewById(R.id.nameInput);
        phoneInput= findViewById(R.id.phoneInput);
        regionInput = findViewById(R.id.regionInput);
        provinceInput = findViewById(R.id.provinceInput);
        cityInput = findViewById(R.id.cityInput);
        barangayInput = findViewById(R.id.barangayInput);
        streetInput = findViewById(R.id.streetInput);
        zipCodeInput = findViewById(R.id.zipCodeInput);
        isDefaultSw = findViewById(R.id.defaultSwitch);
        submitBtn = findViewById(R.id.submitBtn);
        workLabel = findViewById(R.id.workBtn);
        homeLabel = findViewById(R.id.homeBtn);
        otherLabel = findViewById(R.id.otherBtn);
        backBtn = findViewById(R.id.backBtn);
        joinedToggleListenersSetup();
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AddressPage.class));
            finish();
        });

        submitBtn.setOnClickListener(v -> {
            addAddress();
            Toast.makeText(this, "Address added successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AddressPage.class));
            finish();
        });


    }

    private void joinedToggleListenersSetup() {
        CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (buttonView == workLabel) {
                        homeLabel.setChecked(false);
                        otherLabel.setChecked(false);
                    } else if (buttonView == homeLabel) {
                        workLabel.setChecked(false);
                        otherLabel.setChecked(false);
                    } else if (buttonView == otherLabel) {
                        workLabel.setChecked(false);
                        homeLabel.setChecked(false);
                    }
                }
            }
        };
        workLabel.setOnCheckedChangeListener(toggleListener);
        homeLabel.setOnCheckedChangeListener(toggleListener);
        otherLabel.setOnCheckedChangeListener(toggleListener);
    }

    private void addAddress() {
        String uid = CurrentUser.getInstance(this).getUid();
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String region = regionInput.getText().toString();
        String province = provinceInput.getText().toString();
        String city = cityInput.getText().toString();
        String barangay = barangayInput.getText().toString();
        String street = streetInput.getText().toString();
        String zipCode = zipCodeInput.getText().toString();
        String label = determineLabel();
        boolean isDefault = isDefaultSw.isChecked();
        Address address = new Address(phone, name, isDefault, region, province, city, barangay, street, zipCode, label);
        long ok = AddressController.getInstance(this).insertAddress(address, uid);
        if (ok < 0) {
            Log.v("SqliteAddress", "Failed to add address");
        } else {
            Toast.makeText(this, "Address Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }
    private String determineLabel() {
        if (workLabel.isChecked()) {
            return "Work";
        } else if (homeLabel.isChecked()) {
            return "Home";
        } else {
            return "Other";
        }
    }

}