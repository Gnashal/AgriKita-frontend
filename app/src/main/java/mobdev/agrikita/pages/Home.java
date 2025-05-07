package mobdev.agrikita.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowInsets;
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
import mobdev.agrikita.adapters.NewsAdapter;
import mobdev.agrikita.controllers.NewsController;
import mobdev.agrikita.controllers.ProductService;
import mobdev.agrikita.controllers.ShopService;
import mobdev.agrikita.controllers.WeatherService;
import mobdev.agrikita.models.auth.NewsApiResponse;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.shop.Shop;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.UserResponse;
import mobdev.agrikita.controllers.UserService;
import okhttp3.OkHttpClient;

public class Home extends AppCompatActivity {
    private TextView temperatureText, weatherDescriptionText, location, dateText;
    private ImageView profileButton, weatherIcon;
    private LinearLayout marketplaceLayout, ordersLayout, shopLayout, toWeather, cartLayout, profileLayout;
    private RecyclerView newsRecyclerView, bestSellersView, featuredShopsView;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout refresh;
    private ImageButton toNotifications;
    private Button toMarketplace;

    private UserService userService;
    private OkHttpClient client = new OkHttpClient();
    private BestSellersAdapter bestSellersAdapter;
    private FeaturedFarmsAdapter featuredFarmsAdapter;
    private List<Products> bestSellersList = new ArrayList<>();
    private List<Shop> featuredFarmsList = new ArrayList<>();
    ProductService productService;
    ShopService shopService;
    ProgressBar progressBarFeaturedProducts;
    ProgressBar getProgressBarFeaturedShops;

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

        /* Initialize */
        profileButton = findViewById(R.id.profileButton);
        toNotifications = findViewById(R.id.toNotifications);
        toMarketplace = findViewById(R.id.see_all_BestSellers);
        toWeather = findViewById(R.id.toWeatherForecast);
        location = findViewById(R.id.location);
        marketplaceLayout = findViewById(R.id.marketplaceLayout);
        ordersLayout = findViewById(R.id.ordersLayout);
        shopLayout = findViewById(R.id.shopLayout);
        cartLayout = findViewById(R.id.cartLayout);
        profileLayout = findViewById(R.id.profileLayout);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        refresh = findViewById(R.id.swipeRefreshLayout);
        temperatureText = findViewById(R.id.temperatureText);
        weatherDescriptionText = findViewById(R.id.weatherDescriptionText);
        weatherIcon = findViewById(R.id.weatherIcon);
        dateText = findViewById(R.id.dateText);
        bestSellersView = findViewById(R.id.bestSeller);
        featuredShopsView = findViewById(R.id.featuredFarms);
        progressBarFeaturedProducts = findViewById(R.id.progressBarProds);
        getProgressBarFeaturedShops = findViewById(R.id.progressBarShops);

        progressBarFeaturedProducts.setVisibility(View.VISIBLE);
        getProgressBarFeaturedShops.setVisibility(View.VISIBLE);

        bestSellersView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredShopsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        bestSellersAdapter = new BestSellersAdapter(this, bestSellersList);
        bestSellersView.setAdapter(bestSellersAdapter);

        featuredFarmsAdapter = new FeaturedFarmsAdapter(this, featuredFarmsList);
        featuredShopsView.setAdapter(featuredFarmsAdapter);

        fetchBestSellers();
        fetchFeaturedFarms();

        userService = new UserService(this);

        if (CurrentUser.getInstance(this).getUserData() == null) {
            fetchUserData();
        } else {
            setProfilePic();
        }
        setupWeatherData();

        profileButton.setOnClickListener(v -> toProfile());
        toWeather.setOnClickListener(v -> toWeather());

        refresh.setOnRefreshListener(() -> {
            fetchUserData();
            setupWeatherData();
            refresh.setRefreshing(false);
        });


        setupRecyclerView();
        setupSectionClickListeners();
        fetchEverythingNews();
    }

    private void setupRecyclerView() {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter();
        newsRecyclerView.setAdapter(newsAdapter);
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
    private void updateUI() {
        String result = WeatherService.getInstance(this).getJsonWeatherString();
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONObject main = jsonObject.getJSONObject("main");
                double temperature = main.getDouble("temp");
                /*int humidityVal = main.getInt("humidity");
                int pressureVal = main.getInt("pressure");*/

                /*JSONObject windObj = jsonObject.getJSONObject("wind");
                double windSpeed = windObj.getDouble("speed");*/

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

                /*Temperature.setText(String.format("%.0f°", temperature));
                maxTemp.setText(String.format("Max: %.0f°", maxTemperature));
                minTemp.setText(String.format("Min: %.0f°", minTemperature));
                humidity.setText(String.format("Humidity: %d%%", humidityVal));
                pressure.setText(String.format("Pressure: %d hPa", pressureVal));
                wind.setText(String.format("Wind: %.1f km/h", windSpeed));
                sunriseTime.setText(android.text.format.DateFormat.format("hh:mm a", sunrise * 1000));
                sunsetTime.setText(android.text.format.DateFormat.format("hh:mm a", sunset * 1000));*/

            } catch (JSONException e) {
                Log.v("WeatherFetch", "JSON parsing error", e);
            }
        }
    }

    private void fetchEverythingNews() {
        NewsController.getInstance(this).fetchNews(new NewsController.NewsCallback() {
            @Override
            public void onSuccess(NewsApiResponse response) {
                runOnUiThread(() -> {
                    if (response.getArticles() != null) {
                        newsAdapter.setArticles(response.getArticles());
                    } else {
                        Toast.makeText(Home.this, "No articles found", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(Home.this, message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setupSectionClickListeners() {
        marketplaceLayout.setOnClickListener(v -> startActivity(new Intent(this, Marketplace.class)));

        ordersLayout.setOnClickListener(v -> Toast.makeText(this, "My Orders clicked", Toast.LENGTH_SHORT).show());

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
                saveToPrefs();
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

    private void saveToPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserID", CurrentUser.getInstance(this).getUid());
        editor.putBoolean("HasShop", CurrentUser.getInstance(this).hasShop());
        if (CurrentUser.getInstance(this).hasShop()) {
            editor.putString("ShopID", CurrentUser.getInstance(this).getShopId());
        }
        editor.apply();
    }

    private void fetchBestSellers() {
        productService.getFeaturedProducts(new ProductService.ProductCallback() {
            @Override
            public void onProductsFetched(List<Products> products) {
                bestSellersList.clear();
                bestSellersList.addAll(products);
                bestSellersAdapter.notifyDataSetChanged();
                progressBarFeaturedProducts.setVisibility(View.GONE);// Notify adapter about data changes
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
                getProgressBarFeaturedShops.setVisibility(View.GONE);

            }

            @Override
            public void onError(String errorMessage) {
                Log.e("BestSellersActivity", "Error fetching best sellers" + errorMessage);
                getProgressBarFeaturedShops.setVisibility(View.GONE);
            }
        });
    }
}
