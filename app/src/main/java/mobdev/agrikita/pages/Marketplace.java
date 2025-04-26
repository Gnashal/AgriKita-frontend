package mobdev.agrikita.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
import mobdev.agrikita.models.products.ProductService;
import mobdev.agrikita.models.products.Products;

public class Marketplace extends AppCompatActivity {

    RecyclerView productGridView;
    List<Products> productList = new ArrayList<>();
    ProductAdapter adapter;
    MaterialButton all_btn, fruits_btn, grains_btn, meat_btn, poultry_btn, produce_btn, herbs_btn;

    ProductService productService;

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

        productService = new ProductService(this);

        productService.getAllProducts(new ProductService.ProductCallback(){
            @Override
            public void onProductsFetched(List<Products> products) {
                productList = products;

                adapter = new ProductAdapter(Marketplace.this, productList);
                productGridView.setAdapter(adapter);

                adapter.setOnItemClickListener(product -> {
                    Intent go_to_product_detail = new Intent(Marketplace.this, ProductDetailPage.class);

                    go_to_product_detail.putExtra("product_data", product);

                    startActivity(go_to_product_detail);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Marketplace.this, "Failed fetching data", Toast.LENGTH_LONG);
            }
        });
    }
}
