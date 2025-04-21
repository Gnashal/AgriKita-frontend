package mobdev.agrikita.pages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mobdev.agrikita.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherForecast extends AppCompatActivity {

    private static final String TAG = "WeatherForecast";
    private  final int apiKeyID = R.string.OPENWEATHER_API_KEY;  // Replace with actual API key

    private EditText location;
    private TextView conditionText, Temperature, maxTemp, minTemp, humidity,
            pressure, wind, sunriseTime, sunriseDesc, sunsetTime, sunsetDesc;
    private Button RefreshButton;

    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        location = findViewById(R.id.location);
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


        RefreshButton.setOnClickListener(view -> {
            String cityName = location.getText().toString();
            if (!cityName.isEmpty()) {
                Log.v(TAG, "Fetching weather for: " + cityName);
                FetchWeatherData(cityName);
            } else {
                Log.v(TAG, "City name is empty.");
                location.setError("Please enter a city name");
            }
        });
    }

    private void FetchWeatherData(String location) {
        String apiKey = getString(apiKeyID);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey + "&units=metric";
        Log.v(TAG, "Request URL: " + url);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                Log.v(TAG, "API Response: " + result);
                runOnUiThread(() -> updateUI(result));
            } catch (IOException e) {
                Log.v(TAG, "Error fetching weather data", e);
            }
        });
    }

    private void updateUI(String result) {
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

                String description = jsonObject.getJSONArray("weather")
                        .getJSONObject(0).getString("description");
                String iconCode = jsonObject.getJSONArray("weather")
                        .getJSONObject(0).getString("icon");

                JSONObject sys = jsonObject.getJSONObject("sys");
                long sunrise = sys.getLong("sunrise");
                long sunset = sys.getLong("sunset");

                Log.v(TAG, "Parsed Weather Data: " +
                        "\nTemp: " + temperature +
                        "\nMax: " + maxTemperature +
                        "\nMin: " + minTemperature +
                        "\nHumidity: " + humidityVal +
                        "\nPressure: " + pressureVal +
                        "\nWind: " + windSpeed +
                        "\nDescription: " + description +
                        "\nIcon: " + iconCode +
                        "\nSunrise: " + sunrise +
                        "\nSunset: " + sunset);

                // Set weather icon
                String resourceName = "ic_" + iconCode;
                int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
                if (resId != 0) {
                    weatherIcon.setImageResource(resId);
                } else {
                    Log.v(TAG, "Icon resource not found for: " + resourceName);
                }

                // Update UI
                conditionText.setText(description);
                Temperature.setText(String.format("%.0f°", temperature));
                maxTemp.setText(String.format("Max: %.0f°", maxTemperature));
                minTemp.setText(String.format("Min: %.0f°", minTemperature));
                humidity.setText(String.format("Humidity: %d%%", humidityVal));
                pressure.setText(String.format("Pressure: %d hPa", pressureVal));
                wind.setText(String.format("Wind: %.1f km/h", windSpeed));

                sunriseTime.setText(android.text.format.DateFormat.format("hh:mm a", sunrise * 1000));
                sunriseDesc.setText("Sunrise");

                sunsetTime.setText(android.text.format.DateFormat.format("hh:mm a", sunset * 1000));
                sunsetDesc.setText("Sunset");

            } catch (JSONException e) {
                Log.v(TAG, "JSON parsing error", e);
            }
        } else {
            Log.v(TAG, "Result is null, cannot update UI.");
        }
    }
}
