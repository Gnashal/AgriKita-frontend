package mobdev.agrikita.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import mobdev.agrikita.R;

public class ProductDetailPage extends AppCompatActivity {

    LinearLayout back_to_marketplace;
    ImageView prod_display1, prod_display2, prod_display3, prod_display4, seller_pfp_display;
    TextView seller_name, seller_rating, seller_startdate, seller_description,
            prod_category, prod_name, prod_rating, prod_instock, prod_price,
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
        prod_display1 = findViewById(R.id.pdp_prod_image1);
        prod_display2 = findViewById(R.id.pdp_prod_image2);
        prod_display3 = findViewById(R.id.pdp_prod_image3);
        prod_display4 = findViewById(R.id.pdp_prod_image4);
        seller_pfp_display = findViewById(R.id.pdp_seller_pfp);
        seller_name = findViewById(R.id.pdp_seller_name);
        seller_rating = findViewById(R.id.pdp_seller_rating);
        seller_startdate = findViewById(R.id.pdp_seller_start_date);
        seller_description = findViewById(R.id.pdp_seller_description);
        prod_category = findViewById(R.id.pdp_product_category);
        prod_name = findViewById(R.id.pdp_prod_name);
        prod_rating = findViewById(R.id.pdp_prod_rating);
        prod_instock = findViewById(R.id.pdp_prod_instock_amnt);
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

        back_to_marketplace.setOnClickListener(v -> goToMarketPlace());

        // images here!
        prod_display1.setImageResource(getIntent().getIntExtra("prod_imageID", 0));

        // images end here!

        // Seller details
        seller_name.setText(getIntent().getStringExtra("prod_seller"));

        // Product details
        prod_name.setText(getIntent().getStringExtra("prod_name"));
        prod_rating.setText(String.valueOf(getIntent().getDoubleExtra("prod_rating", 0.00)));
        prod_price.setText(String.valueOf(getIntent().getDoubleExtra("prod_price", 0.00)));
        prod_description.setText(getIntent().getStringExtra("prod_description"));
        prod_category.setText(getIntent().getStringExtra("prod_category"));
    }

    private void goToMarketPlace() { startActivity(new Intent(this, Marketplace.class)); }
}