    package mobdev.agrikita.pages.marketplace;

    import android.content.Context;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.Window;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.ProgressBar;
    import android.widget.RatingBar;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.content.ContextCompat;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;
    import androidx.core.view.WindowInsetsControllerCompat;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
    import com.bumptech.glide.request.RequestOptions;
    import com.google.android.material.button.MaterialButton;

    import java.time.OffsetDateTime;
    import java.time.format.DateTimeFormatter;

    import mobdev.agrikita.R;
    import mobdev.agrikita.controllers.ProductService;
    import mobdev.agrikita.controllers.ShopService;
    import mobdev.agrikita.models.products.Products;
    import mobdev.agrikita.controllers.ShoppingCartController;
    import mobdev.agrikita.models.products.request.RateProductRequest;
    import mobdev.agrikita.models.products.response.RateProductResponse;
    import mobdev.agrikita.models.shop.response.GetShopByShopIDResponse;
    import mobdev.agrikita.pages.addons.checkout.ShoppingCartPage;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class ProductDetailPage extends AppCompatActivity {

        LinearLayout back_to_marketplace;
        ImageView prod_display, seller_pfp_display;
        TextView seller_name, seller_startdate, seller_description,
                prod_category, prod_name, prod_rating, prod_price, prod_instock,
                prod_description, prod_origin, prod_freshness, prod_storage, prod_quantit_to_buy,
                prod_total_price, prod_shipping_cost, prod_unit_measurement;
        ProgressBar loadingSpinner;

        MaterialButton prod_addMore_btn, prod_subMore_btn, prod_addToCart_btn;
        ImageButton prod_share_btn;
        LinearLayout prod_rate_btn;
        ProductService productService;
        ShopService shopService;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_product_detail_page);
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
            prod_rate_btn = findViewById(R.id.pdp_rate_btn);
            prod_share_btn = findViewById(R.id.pdp_share_btn);
            prod_unit_measurement = findViewById(R.id.unitMeasurement);
            loadingSpinner = findViewById(R.id.loading_spinner);

            productService = new ProductService(this);
            Products selectedProd = (Products) getIntent().getSerializableExtra("product_data");

            back_to_marketplace.setOnClickListener(v -> goToMarketPlace());

            prod_addMore_btn.setOnClickListener(v -> buyMore_prod(selectedProd));
            prod_subMore_btn.setOnClickListener(v -> subMore_prod(selectedProd));
            prod_addToCart_btn.setOnClickListener(v -> goToShoppingCart(selectedProd));

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
            prod_unit_measurement.setText(selectedProd.getMeasuringUnit());
            // Render Product Images here!
            String prodPic = selectedProd.getImageUrl();


            Glide.with(this)
                    .load(prodPic)
                    .placeholder(R.drawable.agrikita_logo)
                    .error(R.drawable.agrikita_logo)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                    .into(prod_display);
            // ===========================

            loadingSpinner.setVisibility(View.VISIBLE);
            // Get Shop Details
            shopService = new ShopService(this);

            shopService.getShopById(selectedProd.getShopID(), new ShopService.ShopCallback() {
                @Override
                public void onSuccess(GetShopByShopIDResponse shop) {
                    loadingSpinner.setVisibility(View.GONE);
                    OffsetDateTime dateTime = OffsetDateTime.parse(shop.getCreatedAt());
                    String reformattedDate = dateTime.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
                    seller_name.setText(shop.getName());
                    seller_description.setText(shop.getDescription());
                    seller_startdate.setText(reformattedDate);

                    String sellerPic = shop.getImageUrl();

                    Glide.with(ProductDetailPage.this)
                            .load(sellerPic)
                            .placeholder(R.drawable.agrikita_logo)
                            .error(R.drawable.agrikita_logo)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                            .into(seller_pfp_display);
                }

                @Override
                public void onError(String errorMessage) {
                    loadingSpinner.setVisibility(View.GONE);
                    seller_name.setText("Juan");
                    seller_description.setText("From the next mountain");
                    seller_startdate.setText("June 12, 1898");
                    Toast.makeText(ProductDetailPage.this, "Failed fetching data", Toast.LENGTH_LONG).show();
                }
            });

            prod_rate_btn.setOnClickListener(v -> {
                View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null);
                RatingBar ratingBar = dialogView.findViewById(R.id.dialog_rating_bar);

                new AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setPositiveButton("Submit", (dialog, which) -> {
                            float rating = ratingBar.getRating();
                            String prodID = selectedProd.getProductID();

                            if(!prodID.isEmpty()){
                                sendRatingToServer(prodID, rating);
                            }

                            Toast.makeText(this, "Rated: " + rating + ", " + prodID, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }

        private void goToMarketPlace() { startActivity(new Intent(this, Marketplace.class)); }
        private void buyMore_prod(Products product) {
            product.setQuantityToBuy(product.getQuantityToBuy() + 1);
            prod_quantit_to_buy.setText(String.valueOf(product.getQuantityToBuy()));
            prod_total_price.setText("₱ "+String.format("%.2f", getTotal(product)));
            prod_shipping_cost.setText(String.format("₱ "+"%.2f", getShipping(product)));
        }

        private void subMore_prod(Products product) {
            if (product.getQuantityToBuy() > 1) {  // prevent 0
                product.setQuantityToBuy(product.getQuantityToBuy() - 1);
                prod_quantit_to_buy.setText(String.valueOf(product.getQuantityToBuy()));
                prod_total_price.setText(String.format("₱ %.2f", getTotal(product)));
                prod_shipping_cost.setText(String.format("₱ %.2f", getShipping(product)));
            }
        }

        private void goToShoppingCart(Products product) {
            ShoppingCartController.getInstance().addToCart(product);
            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Marketplace.class));
            finish();
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

        private void sendRatingToServer(String productID, float rating) {
            RateProductRequest request = new RateProductRequest(productID, rating);

            productService.rateProduct(request, new ProductService.RateCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(ProductDetailPage.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(ProductDetailPage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }