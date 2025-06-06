package mobdev.agrikita.pages.index;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.BestSellersAdapter;
import mobdev.agrikita.adapters.FeaturedFarmsAdapter;
import mobdev.agrikita.adapters.ProductAdapter;
import mobdev.agrikita.controllers.NotificationService;
import mobdev.agrikita.controllers.ProductService;
import mobdev.agrikita.controllers.ShopService;
import mobdev.agrikita.controllers.WeatherService;
import mobdev.agrikita.models.notifications.NotificationList;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.shop.Shop;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.response.UserResponse;
import mobdev.agrikita.controllers.UserService;
import mobdev.agrikita.pages.addons.checkout.ShoppingCartPage;
import mobdev.agrikita.pages.addons.WeatherForecast;
import mobdev.agrikita.pages.marketplace.Marketplace;
import mobdev.agrikita.pages.marketplace.ProductDetailPage;
import mobdev.agrikita.pages.addons.Notification;
import mobdev.agrikita.pages.shop.CreateShop;
import mobdev.agrikita.pages.shop.InventoryManagement;
import mobdev.agrikita.pages.shop.MyOrders;
import mobdev.agrikita.pages.shop.ShopDetailsPage;
import mobdev.agrikita.pages.welcome.Login;
import okhttp3.OkHttpClient;

public class Home extends AppCompatActivity {
    private TextView temperatureText, weatherDescriptionText, location, dateText, farmersTipText;
    private ImageView profileButton, weatherIcon;
    private LinearLayout marketplaceLayout, ordersLayout, shopLayout, toWeather, cartLayout, profileLayout;
    private RecyclerView bestSellersView, featuredShopsView, productsRecyclerView;
    private SwipeRefreshLayout refresh;
    private ImageButton toNotifications;
    private Button toMarketplace, seeAllProducts;
    private View notificationDot;

    private UserService userService;
    private OkHttpClient client = new OkHttpClient();
    private BestSellersAdapter bestSellersAdapter;
    private FeaturedFarmsAdapter featuredFarmsAdapter;
    private ProductAdapter productsAdapter;
    private List<Products> bestSellersList = new ArrayList<>();
    private List<Shop> featuredFarmsList = new ArrayList<>();
    private List<Products> productsList = new ArrayList<>();

    ProductService productService;
    ShopService shopService;
    ProgressBar progressBarFeaturedProducts;
    ProgressBar progressBarFeaturedShops;
    ProgressBar progressBarProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

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

        productService = new ProductService(this);
        shopService = new ShopService(this);
        /* Initialize Views */
        profileButton = findViewById(R.id.profileButton);
        toNotifications = findViewById(R.id.toNotifications);
        notificationDot = findViewById(R.id.notificationDot);
        toMarketplace = findViewById(R.id.see_all_BestSellers);
        seeAllProducts = findViewById(R.id.see_all_products);
        toWeather = findViewById(R.id.toWeatherForecast);
        location = findViewById(R.id.location);
        marketplaceLayout = findViewById(R.id.marketplaceLayout);
        ordersLayout = findViewById(R.id.ordersLayout);
        shopLayout = findViewById(R.id.shopLayout);
        cartLayout = findViewById(R.id.cartLayout);
        profileLayout = findViewById(R.id.profileLayout);
        refresh = findViewById(R.id.swipeRefreshLayout);
        temperatureText = findViewById(R.id.temperatureText);
        weatherDescriptionText = findViewById(R.id.weatherDescriptionText);
        weatherIcon = findViewById(R.id.weatherIcon);
        dateText = findViewById(R.id.dateText);
        bestSellersView = findViewById(R.id.bestSeller);
        featuredShopsView = findViewById(R.id.featuredFarms);

        progressBarFeaturedProducts = findViewById(R.id.progressBarProds);
        progressBarFeaturedShops = findViewById(R.id.progressBarShops);
        progressBarProducts = findViewById(R.id.progressBarProducts);
        farmersTipText = findViewById(R.id.farmersTipText);

        /* Show loading indicators */
        progressBarFeaturedProducts.setVisibility(View.VISIBLE);
        progressBarFeaturedShops.setVisibility(View.VISIBLE);
        progressBarProducts.setVisibility(View.VISIBLE);

        // Initialize the RecyclerView with GridLayoutManager (2 columns)
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        productsRecyclerView.setLayoutManager(gridLayoutManager);

        /* Setup RecyclerViews */
        setupBestSellersRecyclerView();
        setupFeaturedFarmsRecyclerView();
        setupProductsRecyclerView();

        /* Fetch data */
        fetchBestSellers();
        fetchFeaturedFarms();
        fetchShopProducts();

        /*Notification bar*/
        checkUnreadNotifications();

        userService = new UserService(this);

        // Load user data if available
        if (CurrentUser.getInstance(this).getUserData() == null) {
            fetchUserData();
        } else {
            setProfilePic();
        }

        // Setup weather data
        setupWeatherData();

        /* Set click listeners */
        profileButton.setOnClickListener(v -> toProfile());
        toWeather.setOnClickListener(v -> toWeather());
        seeAllProducts.setOnClickListener(v -> startActivity(new Intent(this, Marketplace.class)));

        // Handle swipe to refresh
        refresh.setOnRefreshListener(() -> {
            fetchUserData();
            setupWeatherData();
            fetchBestSellers();
            fetchFeaturedFarms();
            fetchShopProducts();
            NotificationService.getInstance(this);
            refresh.setRefreshing(false);
        });

        // Setup section click listeners for other views
        setupSectionClickListeners();
    }

    private void setupBestSellersRecyclerView() {
        bestSellersView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bestSellersAdapter = new BestSellersAdapter(this, bestSellersList);
        bestSellersView.setAdapter(bestSellersAdapter);

        bestSellersAdapter.setOnItemClickListener(product -> {
            Intent goToProductDetail = new Intent(Home.this, ProductDetailPage.class);
            goToProductDetail.putExtra("product_data", product);
            startActivity(goToProductDetail);
        });
    }

    private void setupFeaturedFarmsRecyclerView() {
        featuredShopsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        featuredFarmsAdapter = new FeaturedFarmsAdapter(this, featuredFarmsList);
        featuredShopsView.setAdapter(featuredFarmsAdapter);

        featuredFarmsAdapter.setOnItemClickListener(shop -> {
            Intent goToShopDetail = new Intent(Home.this, ShopDetailsPage.class);
            goToShopDetail.putExtra("shop_data", shop);
            startActivity(goToShopDetail);
        });
    }

    private void setupProductsRecyclerView() {
        productsAdapter = new ProductAdapter(this, productsList);
        productsRecyclerView.setAdapter(productsAdapter);

        productsAdapter.setOnItemClickListener(product -> {
            Intent goToProductDetail = new Intent(Home.this, ProductDetailPage.class);
            goToProductDetail.putExtra("product_data", product);
            startActivity(goToProductDetail);
        });
    }

    private void setupWeatherData() {
        WeatherService.getInstance(this).getCurrentLocation(this, this::fetchWeatherData);
        showCurrentDate();
    }

    private void showCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());
        dateText.setText(formattedDate);
    }

    private void fetchWeatherData() {
        WeatherService.getInstance(this).fetchWeatherData(new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(Boolean ok) {
                if (ok.equals(true)) {
                    runOnUiThread(() -> updateUI());
                } else {
                    Log.v("WeatherFetch", "Failed to fetch weather data");
                    Toast.makeText(Home.this, "Failed to fetch weather data.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.v("WeatherFetch", "Failed to fetch weather data: " + e.getMessage());
                Toast.makeText(Home.this, "Network Error in fetching weather data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateFarmerTip(String condition, int humidity) {
        if (condition.contains("sunny")) {
            if (humidity < 40) {
                return "It's sunny and dry. Water crops well to prevent dehydration.";
            } else {
                return "Sunny with moderate humidity. Consider light watering.";
            }
        } else if (condition.contains("rain")) {
            if (humidity > 70) {
                return "Rainy and humid. No need to water; watch for plant diseases.";
            } else {
                return "Rain expected. Delay irrigation and monitor moisture.";
            }
        } else if (condition.contains("cloudy")) {
            if (humidity > 60) {
                return "Cloudy and humid. Provide ventilation in greenhouses.";
            } else {
                return "Cloudy with dry air. Light watering might be necessary.";
            }
        } else if (condition.contains("storm")) {
            return "Storm alert! Secure your crops and farm equipment.";
        } else if (condition.contains("snow")) {
            return "Snowy conditions. Protect plants from frost and cold.";
        } else {
            if (humidity > 80) {
                return "Very humid today. Watch out for mold or mildew.";
            } else {
                return "Check soil and forecast before working the fields.";
            }
        }
    }

    private void updateUI() {
        String result = WeatherService.getInstance(this).getJsonWeatherString();
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONObject main = jsonObject.getJSONObject("main");
                double temperature = main.getDouble("temp");
                int humidityVal = main.getInt("humidity");

                JSONObject sys = jsonObject.getJSONObject("sys");
                long sunrise = sys.getLong("sunrise");
                long sunset = sys.getLong("sunset");

                String countryCode = sys.getString("country");
                Locale locale = new Locale("", countryCode);
                String fullCountryName = locale.getDisplayCountry();

                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                String description = weather.getString("description");
                String mainCondition = weather.getString("main");
                String iconCode = weather.getString("icon");

                String resourceName = "ic_" + iconCode;
                int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
                if (resId != 0) {
                    weatherIcon.setImageResource(resId);
                }

                weatherDescriptionText.setText(mainCondition);
                temperatureText.setText(String.format("%.0f°C", temperature));
                location.setText(fullCountryName);

                String tip = generateFarmerTip(mainCondition.toLowerCase(), humidityVal);
                farmersTipText.setText(tip);

            } catch (JSONException e) {
                Log.v("WeatherFetch", "JSON parsing error", e);
            }
        }
    }

    private void setupSectionClickListeners() {
        marketplaceLayout.setOnClickListener(v -> startActivity(new Intent(this, Marketplace.class)));

//        ordersLayout.setOnClickListener(v -> Toast.makeText(this, "My Orders clicked", Toast.LENGTH_SHORT).show());
        ordersLayout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyOrders.class);
            startActivity(intent);
        });

        shopLayout.setOnClickListener(v -> {
            if (CurrentUser.getInstance(this).hasShop()) {
                startActivity(new Intent(this, InventoryManagement.class));
            } else {
                Toast.makeText(this, "You don't have a shop, let's make you one", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, CreateShop.class));
            }
        });
        cartLayout.setOnClickListener(v -> startActivity(new Intent(this, ShoppingCartPage.class)));
        profileLayout.setOnClickListener(v -> toProfile());
        toNotifications.setOnClickListener(v -> startActivity(new Intent(this, Notification.class)));
        toMarketplace.setOnClickListener(v -> startActivity(new Intent(this, Marketplace.class)));

    }

    private void toProfile() {
        startActivity(new Intent(this, Profile.class));
    }

    private void toWeather() {
        startActivity(new Intent(this, WeatherForecast.class));
    }

    public void fetchUserData() {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String uid = prefs.getString("localId", "");
        if (uid.isEmpty()) {
            Log.e("UserSetup", "UID is missing. Cannot fetch user data.");
            Toast.makeText(Home.this, "UID is missing. Cannot fetch user data", Toast.LENGTH_SHORT).show();
            return;
        }

        CurrentUser.getInstance(this).fetchUserData(uid, new UserService.FetchUserCallback() {
            @Override
            public void onSuccess(UserResponse userResponse) {
                CurrentUser.getInstance(getBaseContext()).setUid(uid);
                saveToPrefs(() -> {
                    NotificationService.getInstance(getBaseContext());
                });
                runOnUiThread(() -> setProfilePic());
            }

            @Override
            public void onFailure(String errorMessage) {
                if (errorMessage.toLowerCase().contains("unauthorized") || errorMessage.toLowerCase().contains("token")) {
                    Toast.makeText(Home.this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Home.this, Login.class));
                    finish();
                } else {
                    Toast.makeText(Home.this, "Failed to fetch user: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setProfilePic() {
        CurrentUser currentUser = CurrentUser.getInstance(this);
        if (currentUser.getImageUrl() != null && !currentUser.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentUser.getImageUrl())
                    .circleCrop()
                    .into(profileButton);
        }
    }

    private void saveToPrefs(Runnable r) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        CurrentUser currentUser = CurrentUser.getInstance(this);
        Log.v("CurrentUser", "Fetched user: " + currentUser.toString());
        editor.putString("UserID", currentUser.getUid());
        if (currentUser.hasShop()) {
            String shopId = currentUser.getShopId();
            if (shopId != null) {
                editor.putBoolean("HasShop", true);
                editor.putString("ShopID", shopId);
            } else {
                editor.putBoolean("HasShop", false);
            }
        } else {
            editor.putBoolean("HasShop", false);
        }

        editor.apply();
        r.run();
    }

    private void fetchBestSellers() {
        productService.getFeaturedProducts(new ProductService.ProductCallback() {
            @Override
            public void onProductsFetched(List<Products> products) {
                bestSellersList.clear();
                bestSellersList.addAll(products);
                bestSellersAdapter.notifyDataSetChanged();
                progressBarFeaturedProducts.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("BestSellersActivity", "Error fetching best sellers", throwable);
                progressBarFeaturedProducts.setVisibility(View.GONE);
            }
        });
    }

    private void fetchFeaturedFarms() {
        shopService.getFeaturedShops(new ShopService.FeaturedShopsCallback() {
            @Override
            public void onSuccess(List<Shop> shops) {
                featuredFarmsList.clear();
                featuredFarmsList.addAll(shops);
                featuredFarmsAdapter.notifyDataSetChanged();
                progressBarFeaturedShops.setVisibility(View.GONE);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("BestSellersActivity", "Error fetching best sellers" + errorMessage);
                progressBarFeaturedShops.setVisibility(View.GONE);
            }
        });
    }

    private void fetchShopProducts() {
        productService.getFeaturedProducts(new ProductService.ProductCallback() {
            @Override
            public void onProductsFetched(List<Products> products) {
                productsList.clear();

                int productCount = Math.min(products.size(), 15); // To handle cases where less than 6 products are fetched
                productsList.addAll(products.subList(0, productCount));
                progressBarProducts.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("ProductList", "Error fetching product", throwable);
                progressBarProducts.setVisibility(View.GONE);
            }
        });
    }
    private void checkUnreadNotifications() {
        boolean hasUnreadNotifications = NotificationList.getInstance().hasUnreadNotifications();
        if (hasUnreadNotifications) {
            notificationDot.setVisibility(View.VISIBLE);
        } else {
            notificationDot.setVisibility(View.GONE);
        }
    }


}