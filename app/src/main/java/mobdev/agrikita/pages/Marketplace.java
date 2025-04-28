package mobdev.agrikita.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
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
import mobdev.agrikita.controllers.ProductService;
import mobdev.agrikita.models.products.Products;

public class Marketplace extends AppCompatActivity {

    RecyclerView productGridView;
    List<Products> productList = new ArrayList<>();
    ProductAdapter adapter;
    AppCompatButton selectedBtn;

    ProductService productService;

    LinearLayout categoryBtnContainer;

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

        categoryBtnContainer = findViewById(R.id.mkpl_category_btn_container);

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

                generateCategoryBtn();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Marketplace.this, "Failed fetching data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterProductsByCategory(String category) {
        List<Products> filteredList = new ArrayList<>();

        if (category.equals("All")) {
            filteredList = productList;
        } else {
            for (Products item: productList) {
                if (item.getCategory().equalsIgnoreCase(category)) {
                    filteredList.add(item);
                }
            }
        }

        adapter.updateList(filteredList);
    }

    private void generateCategoryBtn() {
        List<String> categoryList = new ArrayList<>();

        categoryList.add("All");

        for (Products product : productList) {
            String category = product.getCategory();

            if (category != null && !categoryList.contains(category)) {
                categoryList.add(category);
            }
        }

        for (String cat : categoryList) {
            // In your code:
            AppCompatButton btn = new AppCompatButton(new ContextThemeWrapper(this, R.style.CategoryButton), null, 0);
            btn.setText(cat);

            btn.setOnClickListener(v -> filterProductsByCategory(cat));

            categoryBtnContainer.addView(btn);
        }
    }
}
