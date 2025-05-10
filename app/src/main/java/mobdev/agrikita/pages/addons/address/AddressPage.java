package mobdev.agrikita.pages.addons.address;

import android.os.Bundle;
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

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.AddressAdapter;
import mobdev.agrikita.models.address.AddressList;

public class AddressPage extends AppCompatActivity {
    ListView addressListView;
    TextView noAddressText;
    ImageButton addAddress, backBtn;
    AddressAdapter addressAdapter;
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


        setupAdapter();

    }

    private void setupAdapter() {
        AddressList addressList = new AddressList();
        addressAdapter = new AddressAdapter(this, addressList.getAddresses());
        addressListView.setAdapter(addressAdapter);
    }
}