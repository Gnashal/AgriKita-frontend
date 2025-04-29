package mobdev.agrikita.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.NewsAdapter;
import mobdev.agrikita.api.RetrofitClient;
import mobdev.agrikita.models.auth.NewsApiResponse;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.UserResponse;
import mobdev.agrikita.controllers.UserService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {
    private TextView location;
    private TextView temperatureText;
    private TextView weatherDescriptionText;
    private TextView dateText;
    private Button toWeather;
    private ImageView profileButton;
    private LinearLayout marketplaceLayout;
    private LinearLayout ordersLayout;
    private LinearLayout shopLayout;
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout refresh;

    private UserService userService;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* Initialize */
        profileButton = findViewById(R.id.profileButton);
        toWeather = findViewById(R.id.toWeatherForcast);
        location = findViewById(R.id.location);
        marketplaceLayout = findViewById(R.id.marketplaceLayout);
        ordersLayout = findViewById(R.id.ordersLayout);
        shopLayout = findViewById(R.id.shopLayout);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        refresh = findViewById(R.id.swipeRefreshLayout);
        temperatureText = findViewById(R.id.temperatureText);
        weatherDescriptionText = findViewById(R.id.weatherDescriptionText);
        dateText = findViewById(R.id.dateText);

        userService = new UserService(this);

        if (CurrentUser.getInstance(this).getUserData() == null) {
            fetchUserData();
        } else {
            setProfilePic();
        }

        profileButton.setOnClickListener(v -> toProfile());
        toWeather.setOnClickListener(v -> toWeather());

        refresh.setOnRefreshListener(() -> {
            fetchUserData();
            fetchWeatherData();
            refresh.setRefreshing(false);
        });

        setupRecyclerView();
        setupSectionClickListeners();
        fetchEverythingNews("agriculture");

        fetchWeatherData();  // Fetch weather when opening the app
    }

    private void setupRecyclerView() {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter();
        newsRecyclerView.setAdapter(newsAdapter);
    }

    private void fetchEverythingNews(String query) {
        String apiKey = getString(R.string.NEWS_API_KEY);
        String url = "https://newsapi.org/v2/everything?q=" + query + "&language=en&sortBy=publishedAt&pageSize=4&apiKey=" + apiKey;

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(Home.this, "Error fetching news: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    NewsApiResponse newsApiResponse = gson.fromJson(json, NewsApiResponse.class);

                    runOnUiThread(() -> {
                        if (newsApiResponse.getArticles() != null) {
                            newsAdapter.setArticles(newsApiResponse.getArticles());
                        } else {
                            Toast.makeText(Home.this, "No news articles found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void fetchWeatherData() {
        String city = "Manila"; // Static location for now
        String apiKey = "your_openweather_api_key"; // <--- Replace with your API key
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(Home.this, "Failed to get weather data", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(json);
                        String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                        double temperature = jsonObject.getJSONObject("main").getDouble("temp");

                        runOnUiThread(() -> {
                            weatherDescriptionText.setText(capitalizeWords(description));
                            temperatureText.setText(Math.round(temperature) + "°C");
                            dateText.setText(getFormattedDate());
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(Home.this, "Error parsing weather data", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }

    private String capitalizeWords(String input) {
        String[] words = input.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return builder.toString().trim();
    }

    private String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a | MMM d", Locale.getDefault());
        return sdf.format(new Date());
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
    }

    private void toProfile() {
        startActivity(new Intent(this, Profile.class));
    }

    private void toWeather() {
        startActivity(new Intent(this, WeatherForecast.class));
    }

    public void fetchUserData() {
        Toast.makeText(Home.this, "SetupUser Was Called", Toast.LENGTH_SHORT).show();
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
                setProfilePic();
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
}
