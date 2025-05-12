package mobdev.agrikita.controllers;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import mobdev.agrikita.R;
import mobdev.agrikita.models.news.NewsApiResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsController {
    private static NewsController instance;
    private OkHttpClient client = new OkHttpClient();
    private Context context;

    public NewsController(Context context) {
        this.context = context;
    }

    public static synchronized NewsController getInstance(Context context) {
        if (instance == null) {
            instance = new NewsController(context);
        }
        return instance;
    }

    public interface NewsCallback {
        void onSuccess(NewsApiResponse response);
        void onError(String message);
    }

    public void fetchNews(NewsCallback callback) {
        String query = "agriculture | nature | trees";
        String apiKey = context.getString(R.string.NEWS_API_KEY);
        String url = "https://newsapi.org/v2/everything?q=" + query + "&language=en&sortBy=publishedAt&pageSize=4&apiKey=" + apiKey;

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Error fetching news: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    NewsApiResponse newsApiResponse = gson.fromJson(json, NewsApiResponse.class);
                    callback.onSuccess(newsApiResponse);
                } else {
                    callback.onError("Failed to fetch news.");
                }
            }
        });
    }
}
