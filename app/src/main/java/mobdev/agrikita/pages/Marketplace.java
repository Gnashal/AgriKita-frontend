package mobdev.agrikita.pages;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.ProductAdapter;
import mobdev.agrikita.models.Product;

public class Marketplace extends AppCompatActivity {

    RecyclerView productGridView;
    List<Product> productList = new ArrayList<>();
    ProductAdapter adapter;
    MaterialButton all_btn, fruits_btn, grains_btn, meat_btn, poultry_btn, produce_btn, herbs_btn;

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

        // Call the stuff from the .xml file
        all_btn = findViewById(R.id.all_button);
        fruits_btn = findViewById(R.id.fruit_button);
        grains_btn = findViewById(R.id.grain_button);
        meat_btn = findViewById(R.id.meat_button);
        poultry_btn = findViewById(R.id.poultry_button);
        produce_btn = findViewById(R.id.produce_button);
        herbs_btn = findViewById(R.id.herb_button);

        // Initialize RecyclerView (formerly GridView)
        productGridView = findViewById(R.id.product_grid_view);
        productGridView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns

        // Sample data replace this for the back logics
        productList.add(new Product("Tomatoes", "Fresh red tomatoes", "Produce", "Farmer Juan", 25.00, 4.5, 0, R.drawable.test_tomato));
        productList.add(new Product("Bananas", "Sweet yellow bananas", "Fruits", "Farmer Maria", 15.00, 4.2, 0, R.drawable.test_banana));
        productList.add(new Product("Carrots", "Fresh orange carrots", "Produce", "Farmer Alex", 20.00, 4.3, 0, R.drawable.test_carrot));
        productList.add(new Product("Eggs", "Farm fresh eggs", "Poultry", "Poultry Bros", 10.00, 4.6, 0, R.drawable.test_eggs));
        productList.add(new Product("Rice", "Premium quality rice", "Grains", "Golden Fields", 50.00, 4.4, 0, R.drawable.test_rice));
        productList.add(new Product("Onions", "Fresh white onions", "Produce", "Onion King", 18.00, 4.1, 0, R.drawable.test_onion));
        productList.add(new Product("Chicken", "Whole dressed chicken", "Meat", "Chicken House", 120.00, 4.7, 0, R.drawable.test_chicken));

        adapter = new ProductAdapter(this, productList);
        productGridView.setAdapter(adapter);

        adapter.setOnItemClickListener(product -> {
            Intent go_to_product_detail = new Intent(this, ProductDetailPage.class);

            go_to_product_detail.putExtra("prod_name", product.getName());
            go_to_product_detail.putExtra("prod_description", product.getDescription());
            go_to_product_detail.putExtra("prod_category", product.getCategory());
            go_to_product_detail.putExtra("prod_seller", product.getSeller());
            go_to_product_detail.putExtra("prod_price", product.getPrice());
            go_to_product_detail.putExtra("prod_quantity", product.getQuantity());
            go_to_product_detail.putExtra("prod_imageID", product.getImageResId());

            startActivity(go_to_product_detail);
        });
    }
}
