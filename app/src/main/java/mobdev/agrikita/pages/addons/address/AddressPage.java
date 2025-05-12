package mobdev.agrikita.pages.addons.address;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.AddressAdapter;
import mobdev.agrikita.controllers.AddressController;
import mobdev.agrikita.models.address.Address;
import mobdev.agrikita.models.address.AddressList;
import mobdev.agrikita.models.user.CurrentUser;

public class AddressPage extends AppCompatActivity {
    private ListView addressListView;
    private TextView noAddressText;
    private ImageButton addAddress, backBtn;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addresses);

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
        addressListView = findViewById(R.id.address_list_view);
        addAddress = findViewById(R.id.addNewAddress);
        backBtn = findViewById(R.id.backBtn);
        noAddressText = findViewById(R.id.noAddressText);
        addAddress.setOnClickListener(v -> startActivity(new Intent(this, AddAddress.class)));
        backBtn.setOnClickListener(v -> finish());
        addressList = AddressController.getInstance(this).getAddressesForUid(CurrentUser.getInstance(this).getUid());
        setupView();

    }

    private void setupView() {
        if (addressList.isEmpty()){
            noAddressText.setVisibility(View.VISIBLE);
        } else {
            noAddressText.setVisibility(View.GONE);
            setupAdapter();
        }
    }
    private void setupAdapter() {
        /*gets the addresses for the current user and sets the adapter*/
        Log.v("AddressPageSqlite", "Fetched " + addressList.size() + " addresses");
        addressAdapter = new AddressAdapter(this, addressList);
        addressListView.setAdapter(addressAdapter);
    }
}