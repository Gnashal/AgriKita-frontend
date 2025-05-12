package mobdev.agrikita.pages.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.InventoryManagementAdapter;
import mobdev.agrikita.adapters.ProductAdapter;
import mobdev.agrikita.controllers.ProductService;
import mobdev.agrikita.controllers.UserService;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.shop.Shop;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.response.UserResponse;
import mobdev.agrikita.pages.index.Home;
import mobdev.agrikita.pages.marketplace.Marketplace;
import mobdev.agrikita.pages.marketplace.ProductDetailPage;

public class ShopDetailsPage extends AppCompatActivity {
    ImageView back_btn, shopImg;
    TextView title, location, rating, emptyProductText;
    Shop selectedShop;
    ProductService productService;
    RecyclerView productsRecyclerView;
    private List<Products> productList;
    ProductAdapter adapterProducts;
    ProgressBar progressBarProds;
    String ShopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_details_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        Buttons
        back_btn = findViewById(R.id.back_btn);

//        Images
        shopImg = findViewById(R.id.imageShop);

//        TextViews
        title = findViewById(R.id.textTitle);
        location = findViewById(R.id.textFarmLocation);
        rating = findViewById(R.id.rating);
        emptyProductText = findViewById(R.id.emptyProductText);

//        RecyclerView
        productsRecyclerView = findViewById(R.id.productsRecyclerView);

//        ProgressBar
        progressBarProds = findViewById(R.id.progressBarProds);
        progressBarProds.setVisibility(View.VISIBLE);

        productService = new ProductService(this);

        selectedShop = (Shop) getIntent().getSerializableExtra("shop_data");

        loadShopData();

        back_btn.setOnClickListener(v -> startActivity(new Intent(this, Home.class)));
    }

    public void loadShopData(){
        ShopID = selectedShop.getShopId();
        title.setText(selectedShop.getName());
        location.setText(selectedShop.getAddr());
        rating.setText("0"); //Waiting for the rating shops calculator in the backend

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.agrikita_logo)
                .error(R.drawable.error_no_image)
                .centerCrop();

        Glide.with(this)
                .load(selectedShop.getShopImgUrl())
                .apply(requestOptions)
                .into(this.shopImg);

        productList = new ArrayList<>();
        fetchProducts(ShopID);
    }

    private void fetchProducts(String shopId) {
        productService.getProductsByShopID(shopId, new ProductService.ProductCallback() {
            @Override
            public void onProductsFetched(List<Products> products) {
                productsRecyclerView.setLayoutManager(new GridLayoutManager(ShopDetailsPage.this, 2));

                productList.clear();

                productList.addAll(products);

                if (adapterProducts == null) {
                    adapterProducts = new ProductAdapter(ShopDetailsPage.this, productList);
                    productsRecyclerView.setAdapter(adapterProducts);
                } else {
                    adapterProducts.notifyDataSetChanged();
                }

                checkAdapterProductsSize();

                adapterProducts.setOnItemClickListener(product -> {
                    Intent go_to_product_detail = new Intent(ShopDetailsPage.this, ProductDetailPage.class);
                    go_to_product_detail.putExtra("product_data", product);
                    startActivity(go_to_product_detail);
                });
                progressBarProds.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ShopDetailsPage.this, "Failed to fetch products: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBarProds.setVisibility(View.GONE);
            }
        });
    }

    private void checkAdapterProductsSize() {
        if (adapterProducts.getItemCount() == 0) {
            emptyProductText.setVisibility(View.VISIBLE);
            productsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyProductText.setVisibility(View.GONE);
            productsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}