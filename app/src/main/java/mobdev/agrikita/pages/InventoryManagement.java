package mobdev.agrikita.pages;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.InventoryManagementAdapter;
import mobdev.agrikita.adapters.ProductAdapter;
import mobdev.agrikita.models.Products;

public class InventoryManagement extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InventoryManagementAdapter adapter;
    private List<Products> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler_view_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample Data
        productList = new ArrayList<>();
        productList.add(new Products("Cebu Farm", R.drawable.agrikita_logo, "⭐ 4.8", "₱26", "Per kilo", "Produce", "Fresh Tomatoes", "Locally sourced, high-quality fresh tomatoes, hand-picked."));
        productList.add(new Products("Manila Harvest", R.drawable.agrikita_logo, "⭐ 4.6", "₱40", "Per kilo", "Produce", "Fresh Potatoes", "High quality, organically grown potatoes."));

        adapter = new InventoryManagementAdapter(productList);
        recyclerView.setAdapter(adapter);
    }
}