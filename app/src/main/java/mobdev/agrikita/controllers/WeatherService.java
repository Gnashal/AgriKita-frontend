package mobdev.agrikita.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mobdev.agrikita.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService {

    private static final String TAG = "WeatherService";
    private final Context context;
    private final OkHttpClient client;
    private final String apiKey;

    public WeatherService(@NonNull Context context) {
        this.context = context;
        this.client = new OkHttpClient();
        this.apiKey = context.getString(R.string.OPENWEATHER_API_KEY);
    }

    public interface WeatherCallback {
        void onSuccess(String result);
        void onFailure(Exception e);
    }

    public void fetchWeatherData(String cityName, WeatherCallback callback) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey + "&units=metric";
        Log.v(TAG, "Request URL: " + url);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().string();
                    callback.onSuccess(result);
                } else {
                    callback.onFailure(new IOException("Unexpected response " + response));
                }
            } catch (IOException e) {
                Log.v(TAG, "Error fetching weather data", e);
                callback.onFailure(e);
            }
        });
    }
}
