package mobdev.agrikita.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.NewsAdapter;
import mobdev.agrikita.api.RetrofitClient;
import mobdev.agrikita.api.UserServiceApi;
import mobdev.agrikita.models.auth.NewsApiResponse;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.UserResponse;
import mobdev.agrikita.models.user.UserService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {
    private ImageButton profileButton;
    private Spinner locationSpinner;
    private LinearLayout marketplaceLayout;
    private LinearLayout ordersLayout;
    private LinearLayout shopLayout;
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;

    private UserService userService;

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

        userService = new UserService(this);
        /*IMPORTANT: This sets up the user in the app*/
      if (CurrentUser.getInstance(this).getUserData() == null) { setupUser(); }

        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> toProfile());

        // Initialize views
        locationSpinner = findViewById(R.id.spinner_loc);
        marketplaceLayout = findViewById(R.id.marketplaceLayout);
        ordersLayout = findViewById(R.id.ordersLayout);
        shopLayout = findViewById(R.id.shopLayout);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);

        // Setup RecyclerView
        setupRecyclerView();


        setupLocationSpinner();

        // Set click listeners for the three sections
        setupSectionClickListeners();

        // Optional: fetch news on load
        fetchEverythingNews("agriculture");
    }

    private void setupRecyclerView() {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter();
        newsRecyclerView.setAdapter(newsAdapter);
    }

    private void setupLocationSpinner() {
        String[] locations = {"Philippines", "China", "United States", "Russia"}; // Sample locations

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchEverythingNews(String query) {
        String apiKey = "2e4ccecff1244970bd1240c65c99a2f2"; // ✅ Your API key
        String url = "https://newsapi.org/v2/everything?q=" + query + "&language=en&sortBy=publishedAt&pageSize=4&apiKey=" + apiKey;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(Home.this, "Error fetching news: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
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

    private void setupSectionClickListeners() {
        marketplaceLayout.setOnClickListener(v -> {
            Toast.makeText(this, "Marketplace clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Marketplace.class));
        });

        ordersLayout.setOnClickListener(v -> {
            Toast.makeText(this, "My Orders clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShoppingCartPage.class));
        });

        shopLayout.setOnClickListener(v -> {
            if(CurrentUser.getInstance(this).hasShop()){
                Intent goToShop = new Intent(Home.this, InventoryManagement.class);
                startActivity(goToShop);
            }else{
                Toast.makeText(this, "My Shop clicked but you got no shop", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toProfile() {
        startActivity(new Intent(this, Profile.class));
    }


    public void setupUser() {
        Toast.makeText(Home.this, "SetupUser Was Called", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String uid = prefs.getString("localId", "");
        if ( uid.isEmpty()) {
            Log.e("UserSetup", "UID is missing. Cannot fetch user data.");
            Toast.makeText(Home.this, "UID is missing. Cannot fetch user data", Toast.LENGTH_SHORT).show();
            return;
        }

        CurrentUser.getInstance(this).fetchUserData(uid, new UserService.FetchUserCallback() {
            @Override
            public void onSuccess(UserResponse userResponse) {
                saveToPrefs();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(Home.this, "Failed to fetch user: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
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
