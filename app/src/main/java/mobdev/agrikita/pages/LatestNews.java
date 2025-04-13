package mobdev.agrikita.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.NewsAdapter;
import mobdev.agrikita.models.LatestNewsViewModel;

public class LatestNews extends AppCompatActivity {

    private SearchView locationSearchView;
    private LinearLayout marketplaceLayout;
    private LinearLayout ordersLayout;
    private LinearLayout shopLayout;
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private LatestNewsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_news);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LatestNewsViewModel.class);

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

        // Observe news data changes
        observeNewsData();
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
                fetchNews(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: implement real-time search if needed
                return false;
            }
        });
    }

    private void fetchNews(String query) {
        String apiKey = "d1b68dc7b791475fb691ee39d51dfa42"; // Replace with your actual API key
        viewModel.fetchNews(query, apiKey);
    }

    private void observeNewsData() {
        viewModel.getNewsLiveData().observe(this, newsResponse -> {
            if (newsResponse != null && newsResponse.getArticles() != null) {
                newsAdapter.setNewsList(newsResponse.getArticles());
            }
        });

        viewModel.getErrorLiveData().observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSectionClickListeners() {
        marketplaceLayout.setOnClickListener(v -> {
            Toast.makeText(LatestNews.this, "Marketplace clicked", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(LatestNews.this, MarketplaceActivity.class);
            // startActivity(intent);
        });

        ordersLayout.setOnClickListener(v -> {
            Toast.makeText(LatestNews.this, "My Orders clicked", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(LatestNews.this, OrdersActivity.class);
            // startActivity(intent);
        });

        shopLayout.setOnClickListener(v -> {
            Toast.makeText(LatestNews.this, "My Shop clicked", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(LatestNews.this, MyShopActivity.class);
            // startActivity(intent);
        });
    }
}