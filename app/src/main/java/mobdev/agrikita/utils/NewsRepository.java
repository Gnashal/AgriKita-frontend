package mobdev.agrikita.utils;

import mobdev.agrikita.models.NewsResponse;
import mobdev.agrikita.api.NewsApiService;
import mobdev.agrikita.api.NewsRetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private final NewsApiService apiService;

    public NewsRepository() {
        // Use the News-specific Retrofit client
        this.apiService = NewsRetrofitClient.getClient().create(NewsApiService.class);
    }

    public void getNews(String query, String apiKey, NewsCallback callback) {
        Call<NewsResponse> call = apiService.getNews(query, apiKey);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewsResponse newsResponse = response.body();
                    // Limit the articles to 4
                    if (newsResponse.getArticles() != null && newsResponse.getArticles().size() > 4) {
                        newsResponse.setArticles(newsResponse.getArticles().subList(0, 4)); // Get only the first 4 articles
                    }
                    callback.onSuccess(newsResponse);
                } else {
                    callback.onFailure("Failed to fetch news: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public interface NewsCallback {
        void onSuccess(NewsResponse response);
        void onFailure(String errorMessage);
    }
}
