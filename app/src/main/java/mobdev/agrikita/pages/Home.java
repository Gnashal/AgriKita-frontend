package mobdev.agrikita.pages;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.NewsAdapter;

public class Home extends AppCompatActivity {
    private SearchView locationSearchView;
    private LinearLayout marketplaceLayout;
    private LinearLayout ordersLayout;
    private LinearLayout shopLayout;
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private NewsApiClient newsApiClient;

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

        newsApiClient = new NewsApiClient("2e4ccecff1244970bd1240c65c99a2f2"); // Replace with your API key

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

        // Fetch initial news
        fetchTopHeadlines("nature"); // Default query
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
                fetchTopHeadlines(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: implement real-time search if needed
                return false;
            }
        });
    }

    private void fetchTopHeadlines(String query) {
        Log.d("NewsAPI", "Fetching top headlines for query: " + query);
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q(query)
                        .language("en")
                        .pageSize(4) // Limit to 4 articles
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        Log.d("NewsAPI", "onSuccess called");
                        runOnUiThread(() -> {
                            if (response.getArticles() != null) {
                                newsAdapter.setArticles(response.getArticles());
                            } else {
                                Toast.makeText(Home.this, "No articles found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("NewsAPI", "Error: ", throwable);
                        runOnUiThread(() -> {
                            String errorMessage = "Failed to load news";

                            if (throwable instanceof UnknownHostException) {
                                errorMessage = "No internet connection";
                            } else if (throwable instanceof SocketTimeoutException) {
                                errorMessage = "Connection timeout";
                            } else if (throwable.getMessage() != null) {
                                if (throwable.getMessage().contains("associated address")) {
                                    errorMessage = "Network connection failed";
                                } else {
                                    errorMessage = throwable.getMessage();
                                }
                            }

                            Toast.makeText(Home.this, errorMessage, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
        );
    }

    private void setupSectionClickListeners() {
        marketplaceLayout.setOnClickListener(v -> {
            Toast.makeText(this, "Marketplace clicked", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(LatestNews.this, MarketplaceActivity.class);
            // startActivity(intent);
        });

        ordersLayout.setOnClickListener(v -> {
            Toast.makeText(this, "My Orders clicked", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(LatestNews.this, OrdersActivity.class);
            // startActivity(intent);
        });

        shopLayout.setOnClickListener(v -> {
            Toast.makeText(this, "My Shop clicked", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(LatestNews.this, MyShopActivity.class);
            // startActivity(intent);
        });
    }
}