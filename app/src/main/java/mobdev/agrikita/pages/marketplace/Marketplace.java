package mobdev.agrikita.pages.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.ProductAdapter;
import mobdev.agrikita.controllers.ProductService;
import mobdev.agrikita.controllers.UserService;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.response.UserResponse;
import mobdev.agrikita.pages.index.Home;

public class Marketplace extends AppCompatActivity {

    RecyclerView productGridView;
    List<Products> productList = new ArrayList<>();
    ProductAdapter adapter;
    ProductService productService;
    LinearLayout categoryBtnContainer, paginationContainer;
    ImageButton backtoHomepage;

    private static final int PAGE_SIZE = 6;
    private int currentPage = 0;
    private List<Products> displayedList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_marketplace);

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

        categoryBtnContainer = findViewById(R.id.mkpl_category_btn_container);
        paginationContainer = findViewById(R.id.mkpl_paginator_container);
        backtoHomepage = findViewById(R.id.backToHomapage);

        // Initialize RecyclerView (formerly GridView)
        productGridView = findViewById(R.id.product_grid_view);
        productGridView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns

        productService = new ProductService(this);
        String userID = CurrentUser.getInstance(this).getShopId();

        productService.getAllProducts(userID, new ProductService.ProductCallback(){
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
                generatePaginationButtons();
                showPage(0);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Marketplace.this, "Failed fetch data", Toast.LENGTH_LONG).show();
            }
        });

        backtoHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Marketplace.this, Home.class));
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

        // Keep track of selected button
        final AppCompatButton[] selectedCategoryBtn = {null};

        for (String cat : categoryList) {
            AppCompatButton btn = new AppCompatButton(new ContextThemeWrapper(this, R.style.CategoryButton), null, 0);
            btn.setText(cat);

            btn.setOnClickListener(v -> {
                if (selectedCategoryBtn[0] != null) {
                    selectedCategoryBtn[0].setSelected(false);
                }

                btn.setSelected(true);
                selectedCategoryBtn[0] = btn;

                filterProductsByCategory(cat);
            });

            if (cat.equals("All")) {
                btn.setSelected(true);
                selectedCategoryBtn[0] = btn;
            }

            categoryBtnContainer.addView(btn);
        }
    }
    private void generatePaginationButtons() {
        paginationContainer.removeAllViews();

        int totalPages = (int) Math.ceil((double) productList.size() / PAGE_SIZE);

        // Previous button
        paginationContainer.addView(createPageButton("<", () -> {
            int newPage = (currentPage - 1 + totalPages) % totalPages;
            showPage(newPage);
            generatePaginationButtons();
        }));

        // Page buttons
        Button currentPageBtn = createPageButton(String.valueOf(currentPage + 1), null);
        currentPageBtn.setEnabled(false);
        paginationContainer.addView(currentPageBtn);

        // Next button
        paginationContainer.addView(createPageButton(">", () -> {
            int newPage = (currentPage + 1) % totalPages;
            showPage(newPage);
            generatePaginationButtons();
        }));
    }

    private Button createPageButton(String pageNumber, Runnable onClick) {
        AppCompatButton btn = new AppCompatButton(new ContextThemeWrapper(this, R.style.PaginatorButton), null, 0);
        btn.setText(pageNumber);

        btn.setOnClickListener(v -> onClick.run());

        return btn;
    }

    private void showPage(int page) {
        currentPage = page;

        int startIndex = page * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, productList.size());

        displayedList = productList.subList(startIndex, endIndex);

        if (adapter == null) {
            adapter = new ProductAdapter(this, displayedList);
            productGridView.setAdapter(adapter);
            adapter.setOnItemClickListener(product -> {
                Intent go_to_product_detail = new Intent(Marketplace.this, ProductDetailPage.class);
                go_to_product_detail.putExtra("product_data", product);
                startActivity(go_to_product_detail);
            });
        } else {
            adapter.updateList(displayedList);
        }
    }
}
