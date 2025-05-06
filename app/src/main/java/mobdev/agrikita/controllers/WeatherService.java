package mobdev.agrikita.controllers;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mobdev.agrikita.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService {
    private WeatherService instance;
    private static final String TAG = "WeatherService";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private final Context context;
    private final OkHttpClient client;
    private final String apiKey;
    private String currentCity;
    private String jsonWeatherString;

    public WeatherService(@NonNull Context context) {
        this.context = context;
        this.client = new OkHttpClient();
        this.apiKey = context.getString(R.string.OPENWEATHER_API_KEY);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public WeatherService getInstance(Context context) {
        if (instance == null) {
            instance = new WeatherService(context);
        }
        return instance;
    }

    public interface WeatherCallback {
        void onSuccess(Boolean ok);
        void onFailure(Exception e);
    }



    public void fetchWeatherData(WeatherCallback callback) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + currentCity + "&appid=" + apiKey + "&units=metric";
        Log.v(TAG, "Request URL: " + url);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().string();
                    jsonWeatherString = result;
                    Log.v(TAG, "JSON Weather String: " + result);
                    callback.onSuccess(true);
                } else {
                    callback.onFailure(new IOException("Unexpected response " + response));
                }
                if (currentCity == null || currentCity.isEmpty()) {
                    callback.onFailure(new IllegalStateException("Current city is null or empty"));
                }
            } catch (IOException e) {
                Log.v(TAG, "Error fetching weather data", e);
                callback.onFailure(e);
            }
        });
    }


    public void getCurrentLocation(Activity act, Runnable onCityReady) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                getCityFromCoordinates(lat, lon, onCityReady);
            }
        });
    }


    private void getCityFromCoordinates(double lat, double lon, Runnable onCityReady) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String city = addresses.get(0).getLocality();
                if (city != null) {
                    currentCity = city.equals("Lungsod ng Cebu") ? city.substring(city.lastIndexOf(" ") + 1) : city;
                    Log.v(TAG, "Detected city: " + currentCity);
                    onCityReady.run();
                }
            }
        } catch (IOException e) {
            Log.v(TAG, "Geocoder error", e);
        }
    }

    public String getJsonWeatherString() {
        return jsonWeatherString;
    }
}
