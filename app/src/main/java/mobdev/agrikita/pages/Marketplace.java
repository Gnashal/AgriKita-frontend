package mobdev.agrikita.pages;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.ProductAdapter;
import mobdev.agrikita.models.Product;

public class Marketplace extends AppCompatActivity {

    RecyclerView productGridView;
    List<Product> productList = new ArrayList<>();
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_marketplace);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView (formerly GridView)
        productGridView = findViewById(R.id.product_grid_view);
        productGridView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns

        // Sample data
        productList.add(new Product("Tomatoes", "Fresh red tomatoes", "Produce", "Farmer Juan", 25.00, 4.5, 0, R.drawable.test_tomato));
        productList.add(new Product("Bananas", "Sweet yellow bananas", "Fruits", "Farmer Maria", 15.00, 4.2, 0, R.drawable.test_banana));
        productList.add(new Product("Carrots", "Fresh orange carrots", "Produce", "Farmer Alex", 20.00, 4.3, 0, R.drawable.test_carrot));
        productList.add(new Product("Eggs", "Farm fresh eggs", "Poultry", "Poultry Bros", 10.00, 4.6, 0, R.drawable.test_eggs));
        productList.add(new Product("Rice", "Premium quality rice", "Grains", "Golden Fields", 50.00, 4.4, 0, R.drawable.test_rice));
        productList.add(new Product("Onions", "Fresh white onions", "Produce", "Onion King", 18.00, 4.1, 0, R.drawable.test_onion));
        productList.add(new Product("Chicken", "Whole dressed chicken", "Meat", "Chicken House", 120.00, 4.7, 0, R.drawable.test_chicken));

        adapter = new ProductAdapter(this, productList);
        productGridView.setAdapter(adapter);
    }
}
