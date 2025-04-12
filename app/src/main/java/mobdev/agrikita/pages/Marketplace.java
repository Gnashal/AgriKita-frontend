package mobdev.agrikita.pages;

import android.os.Bundle;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.ProductAdapter;
import mobdev.agrikita.models.Product;

public class Marketplace extends AppCompatActivity {

    GridView productGridView;
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

        productGridView = findViewById(R.id.product_grid_view);

        // Sample data
        productList.add(new Product("Tomatoes", "Fresh red tomatoes", "Produce", 25.00, 4.5, 30, R.drawable.test_tomato));
        productList.add(new Product("Bananas", "Sweet yellow bananas", "Fruits", 15.00, 4.2, 50, R.drawable.test_banana));
        productList.add(new Product("Chicken", "Whole dressed chicken", "Meat", 120.00, 4.7, 10, R.drawable.test_chicken));

        adapter = new ProductAdapter(this, productList);
        productGridView.setAdapter(adapter);
    }
}