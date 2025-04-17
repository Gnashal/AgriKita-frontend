package mobdev.agrikita.pages;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import mobdev.agrikita.models.auth.NewsApiResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {
    private SearchView locationSearchView;
    private LinearLayout marketplaceLayout;
    private LinearLayout ordersLayout;
    private LinearLayout shopLayout;
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    // private NewsApiClient newsApiClient; //  No longer needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        setupNavbar();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // newsApiClient = new NewsApiClient("YOUR_API_KEY"); // ❌ Old library not used

        // Initialize views
        locationSearchView = findViewById(R.id.searchView);
        marketplaceLayout = findViewById(R.id.marketplaceLayout);
        ordersLayout = findViewById(R.id.ordersLayout);
        shopLayout = findViewById(R.id.shopLayout);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);

        // Setup RecyclerView
        setupRecyclerView();

        // Setup search functionality
        setupSearch();

        // Set click listeners for the three sections
        setupSectionClickListeners();

        // Optional: fetch news on load
        fetchEverythingNews("trees OR food OR farming OR nature OR agribusiness");
    }

    private void setupRecyclerView() {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter();
        newsRecyclerView.setAdapter(newsAdapter);
    }

    private void setupSearch() {
        locationSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchEverythingNews(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // Real-time search not implemented
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
        });

        ordersLayout.setOnClickListener(v -> {
            Toast.makeText(this, "My Orders clicked", Toast.LENGTH_SHORT).show();
        });

        shopLayout.setOnClickListener(v -> {
            Toast.makeText(this, "My Shop clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupNavbar() {
        Navbar navbarFragment = new Navbar();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.navbarContainer, navbarFragment);
        transaction.commit();
    }
}
