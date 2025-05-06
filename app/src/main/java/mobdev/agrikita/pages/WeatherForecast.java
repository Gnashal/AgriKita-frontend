package mobdev.agrikita.pages;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.WeatherService;

public class WeatherForecast extends AppCompatActivity {

    private static final String TAG = "WeatherForecast";

    private EditText location;
    private TextView currentDate, conditionText, Temperature, maxTemp, minTemp, humidity,
            pressure, wind, sunriseTime, sunriseDesc, sunsetTime, sunsetDesc, countryName, weatherDesc;
    private Button RefreshButton;
    private ImageView weatherIcon;

    private WeatherService weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_forecast);
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
        sunriseDesc = findViewById(R.id.sunriseDesc);
        sunsetTime = findViewById(R.id.sunsetTime);
        sunsetDesc = findViewById(R.id.sunsetDesc);
        RefreshButton = findViewById(R.id.fetchWeatherButton);
        weatherIcon = findViewById(R.id.weatherIcon);
        weatherDesc = findViewById(R.id.weatherDescription);
        countryName = findViewById(R.id.countryName);

        showCurrentDate();
        weatherService = new WeatherService(this);

        RefreshButton.setOnClickListener(view -> {
            String cityName = location.getText().toString();
            if (!cityName.isEmpty()) {
                Log.v(TAG, "Fetching weather for: " + cityName);
                fetchWeather();
            }
        });
    }

    private void showCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());
        currentDate.setText(formattedDate);
    }

    private void fetchWeather() {
        weatherService.fetchWeatherData(new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(Boolean ok) {
                if (ok) {
                    runOnUiThread(() -> updateUI());
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(WeatherForecast.this, "Failed to fetch weather data.", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(WeatherForecast.this, "Failed to fetch weather data.", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void updateUI() {
        String result = weatherService.getJsonWeatherString();
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
                humidity.setText(String.format("Humidity: %d%%", humidityVal));
                pressure.setText(String.format("Pressure: %d hPa", pressureVal));
                wind.setText(String.format("Wind: %.1f km/h", windSpeed));
                sunriseTime.setText(android.text.format.DateFormat.format("hh:mm a", sunrise * 1000));
                sunsetTime.setText(android.text.format.DateFormat.format("hh:mm a", sunset * 1000));

            } catch (JSONException e) {
                Log.v(TAG, "JSON parsing error", e);
            }
        }
    }
}
