package mobdev.agrikita.pages.addons;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mobdev.agrikita.adapters.NewsAdapter;
import mobdev.agrikita.controllers.NewsController;
import mobdev.agrikita.R;
import mobdev.agrikita.controllers.WeatherService;
import mobdev.agrikita.models.auth.response.NewsApiResponse;
import mobdev.agrikita.pages.index.Home;

public class WeatherForecast extends AppCompatActivity {
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private EditText location;
    private TextView currentDate, conditionText, Temperature, maxTemp, minTemp, humidity,
            pressure, wind, sunriseTime, sunsetTime, countryName, weatherDesc,  farmersTipText;
//    private Button changeCountryBtn;
    private SwipeRefreshLayout refreshBtn;
    private ImageView weatherIcon;
    private ProgressBar loadingSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_forecast);
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

        // Initialize views
        location = findViewById(R.id.location);
        currentDate = findViewById(R.id.currentDate);
        conditionText = findViewById(R.id.conditionText);
        Temperature = findViewById(R.id.Temperature);
        maxTemp = findViewById(R.id.maxTemp);
        minTemp = findViewById(R.id.minTemp);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        wind = findViewById(R.id.wind);
        sunriseTime = findViewById(R.id.sunriseTime);
        sunsetTime = findViewById(R.id.sunsetTime);
//        changeCountryBtn = findViewById(R.id.fetchWeatherButton);
        weatherIcon = findViewById(R.id.weatherIcon);
        weatherDesc = findViewById(R.id.weatherDescription);
        countryName = findViewById(R.id.countryName);
        refreshBtn = findViewById(R.id.swipeRefreshLayout);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        farmersTipText = findViewById(R.id.farmersTipText);

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsAdapter = new NewsAdapter();
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        showCurrentDate();
        fetchWeather();
        fetchEverythingNews();
//        changeCountryBtn.setOnClickListener(view -> {
//            String cityName = location.getText().toString();
//            if (!cityName.isEmpty()) {
//                Log.v("WeatherForecastFetch", "Fetching weather for: " + cityName);
//                fetchWeather();
//            }
//        });
        refreshBtn.setOnRefreshListener(() -> {
            fetchWeather();
            fetchEverythingNews();
        });
    }

    private void showCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());
        currentDate.setText(formattedDate);
    }

    private void fetchWeather() {
        runOnUiThread(() -> {
            findViewById(R.id.loadingOverlay).setVisibility(View.VISIBLE);
            loadingSpinner.setVisibility(View.VISIBLE);
//            changeCountryBtn.setEnabled(false);
            location.setEnabled(false);
        });
        WeatherService.getInstance(this).fetchWeatherData(new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(Boolean ok) {
                runOnUiThread(() -> {
                    findViewById(R.id.loadingOverlay).setVisibility(View.GONE);
                    loadingSpinner.setVisibility(View.GONE);
//                    changeCountryBtn.setEnabled(true);
                    location.setEnabled(true);

                    if (refreshBtn.isRefreshing()) {
                        refreshBtn.setRefreshing(false);
                    }
                    if (ok) {
                        updateUI();
                    } else {
                        Toast.makeText(WeatherForecast.this, "Failed to fetch weather data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                findViewById(R.id.loadingOverlay).setVisibility(View.GONE);
                loadingSpinner.setVisibility(View.GONE);
                Toast.makeText(WeatherForecast.this, "Failed to fetch weather data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String generateFarmerTip(String condition, int humidity) {
        if (condition.contains("sunny")) {
            if (humidity < 40) {
                return "It's sunny and dry. Water crops well to prevent dehydration.";
            } else {
                return "Sunny with moderate humidity. Consider light watering.";
            }
        } else if (condition.contains("rain")) {
            if (humidity > 70) {
                return "Rainy and humid. No need to water; watch for plant diseases.";
            } else {
                return "Rain expected. Delay irrigation and monitor moisture.";
            }
        } else if (condition.contains("cloudy")) {
            if (humidity > 60) {
                return "Cloudy and humid. Provide ventilation in greenhouses.";
            } else {
                return "Cloudy with dry air. Light watering might be necessary.";
            }
        } else if (condition.contains("storm")) {
            return "Storm alert! Secure your crops and farm equipment.";
        } else if (condition.contains("snow")) {
            return "Snowy conditions. Protect plants from frost and cold.";
        } else {
            if (humidity > 80) {
                return "Very humid today. Watch out for mold or mildew.";
            } else {
                return "Check soil and forecast before working the fields.";
            }
        }
    }

    private void updateUI() {
        String result = WeatherService.getInstance(this).getJsonWeatherString();
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONObject main = jsonObject.getJSONObject("main");
                double temperature = main.getDouble("temp");
                double maxTemperature = main.getDouble("temp_max");
                double minTemperature = main.getDouble("temp_min");
                int humidityVal = main.getInt("humidity");
                int pressureVal = main.getInt("pressure");

                JSONObject windObj = jsonObject.getJSONObject("wind");
                double windSpeed = windObj.getDouble("speed");

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

                conditionText.setText(description);
                weatherDesc.setText(mainCondition);
                countryName.setText(fullCountryName);

                Temperature.setText(String.format("%.0f°", temperature));
                maxTemp.setText(String.format("Max: %.0f°", maxTemperature));
                minTemp.setText(String.format("Min: %.0f°", minTemperature));
                humidity.setText(String.format("%d%%", humidityVal));
                pressure.setText(String.format("%d hPa", pressureVal));
                wind.setText(String.format("%.1f km/h", windSpeed));
                sunriseTime.setText(android.text.format.DateFormat.format("hh:mm a", sunrise * 1000));
                sunsetTime.setText(android.text.format.DateFormat.format("hh:mm a", sunset * 1000));

                String tip = generateFarmerTip(mainCondition.toLowerCase(), humidityVal);
                farmersTipText.setText(tip);

            } catch (JSONException e) {
                Log.v("WeatherForecastFetch", "JSON parsing error", e);
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
                        Toast.makeText(WeatherForecast.this, "No articles found", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(WeatherForecast.this, message, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
