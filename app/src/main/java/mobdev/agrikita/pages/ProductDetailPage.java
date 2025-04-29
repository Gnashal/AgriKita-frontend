package mobdev.agrikita.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.UserService;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.controllers.ShoppingCartController;

public class ProductDetailPage extends AppCompatActivity {

    LinearLayout back_to_marketplace;
    ImageView prod_display, seller_pfp_display;
    TextView seller_name, seller_startdate, seller_description,
            prod_category, prod_name, prod_rating, prod_price, prod_instock,
            prod_description, prod_origin, prod_freshness, prod_storage, prod_quantit_to_buy,
            prod_total_price, prod_shipping_cost;
    MaterialButton prod_addMore_btn, prod_subMore_btn, prod_addToCart_btn;
    ImageButton prod_heart_btn, prod_share_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Connecting the layout ids to here
        back_to_marketplace = findViewById(R.id.pdp_back_to_mrktp);

        // Seller Related
        seller_pfp_display = findViewById(R.id.pdp_seller_pfp);
        seller_name = findViewById(R.id.pdp_seller_name);
        seller_startdate = findViewById(R.id.pdp_seller_start_date);
        seller_description = findViewById(R.id.pdp_seller_description);

        // Product Related
        prod_display = findViewById(R.id.pdp_prod_image);
        prod_instock = findViewById(R.id.pdp_prod_instockamt);
        prod_category = findViewById(R.id.pdp_product_category);
        prod_name = findViewById(R.id.pdp_prod_name);
        prod_rating = findViewById(R.id.pdp_prod_rating);
        prod_price = findViewById(R.id.pdp_prod_price);
        prod_description = findViewById(R.id.pdp_prod_description);
        prod_origin = findViewById(R.id.pdp_prod_origin);
        prod_freshness = findViewById(R.id.pdp_prod_freshness);
        prod_storage = findViewById(R.id.pdp_prod_storage);
        prod_quantit_to_buy = findViewById(R.id.pdp_prod_quantity_to_buy);
        prod_total_price = findViewById(R.id.pdp_prod_totalprice);
        prod_shipping_cost = findViewById(R.id.pdp_prod_shipping_cost);
        prod_addMore_btn = findViewById(R.id.pdp_add_btn);
        prod_subMore_btn= findViewById(R.id.pdp_sub_btn);
        prod_addToCart_btn = findViewById(R.id.pdp_addtocart_btn);
        prod_heart_btn = findViewById(R.id.pdp_heart_btn);
        prod_share_btn = findViewById(R.id.pdp_share_btn);

        Products selectedProd = (Products) getIntent().getSerializableExtra("product_data");

        back_to_marketplace.setOnClickListener(v -> goToMarketPlace());

        prod_addMore_btn.setOnClickListener(v -> buyMore_prod(selectedProd));
        prod_subMore_btn.setOnClickListener(v -> subMore_prod(selectedProd));
        prod_addToCart_btn.setOnClickListener(v -> goToShoppingCart(selectedProd));
        prod_heart_btn.setOnClickListener(v -> {
            Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
        });
        prod_share_btn.setOnClickListener(v -> {
            Toast.makeText(this, "Pick platform to share", Toast.LENGTH_SHORT).show();
        });

        if (selectedProd == null) {
            Toast.makeText(this, "Error: Product data missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // About Product
        prod_name.setText(selectedProd.getProductName());
        prod_description.setText(selectedProd.getDescription());
        prod_category.setText(selectedProd.getCategory());
        prod_price.setText("₱ "+String.format("%.2f", selectedProd.getPrice()));
        prod_rating.setText(selectedProd.getRating());
        prod_origin.setText(selectedProd.getOriginLocation());
        prod_freshness.setText(selectedProd.getFreshnessRate());
        prod_storage.setText(selectedProd.getStorage());
        prod_instock.setText(String.format("%d", selectedProd.getStockQuantity())+" In stock");
        
        // Render Product Images here!
        String prodPic = selectedProd.getImageUrl();


        Glide.with(this)
                .load(prodPic)
                .placeholder(R.drawable.agrikita_logo)
                .error(R.drawable.agrikita_logo)
                .into(prod_display);
        // ===========================


        // About Seller
        seller_name.setText(selectedProd.getShopID());
        seller_description.setText("Test Im am the seller of this "+selectedProd.getProductName());
        seller_startdate.setText("30-4-2025");
        // seller_pfp_display
    }

    private void goToMarketPlace() { startActivity(new Intent(this, Marketplace.class)); }
    private void buyMore_prod(Products product) {
        product.setQuantityToBuy(product.getQuantityToBuy() + 1);
        prod_quantit_to_buy.setText(String.valueOf(product.getQuantityToBuy()));
        prod_total_price.setText("₱ "+String.format("%.2f", getTotal(product)));
        prod_shipping_cost.setText(String.format("₱ "+"%.2f", getShipping(product)));
        Toast.makeText(this, product.getProductName() + " Added", Toast.LENGTH_SHORT).show();
    }

    private void subMore_prod(Products product) {
        if (product.getQuantityToBuy() > 1) {  // prevent 0
            product.setQuantityToBuy(product.getQuantityToBuy() - 1);
            prod_quantit_to_buy.setText(String.valueOf(product.getQuantityToBuy()));
            prod_total_price.setText(String.format("₱ %.2f", getTotal(product)));
            prod_shipping_cost.setText(String.format("₱ %.2f", getShipping(product)));
            Toast.makeText(this, product.getProductName() + " Subtracted", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToShoppingCart(Products product) {
        ShoppingCartController.getInstance().addToCart(product);
        startActivity(new Intent(ProductDetailPage.this, ShoppingCartPage.class));
    }

    private double getTotal(Products product) {
        double result = 0;

        result += product.getPrice() * product.getQuantityToBuy();

        return result;
    }

    private double getShipping(Products product) {
        double result = 0;

        result += 11.11 * product.getQuantityToBuy();

        return result;
    }

}