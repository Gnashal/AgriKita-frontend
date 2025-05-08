package mobdev.agrikita.pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import mobdev.agrikita.R;

public class Module extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module); // Make sure you have this layout file

        CardView cropPlanningCard = findViewById(R.id.cropPlanningCard);
        CardView weatherResistantCard = findViewById(R.id.weatherResistantCard);
        CardView marketAccessCard = findViewById(R.id.marketAccessCard);
        CardView digitalToolsCard = findViewById(R.id.digitalToolsCard);

        // Set click listeners with specific URLs for each module
        cropPlanningCard.setOnClickListener(v -> openWebPage("https://www.sierraflowerfarm.com/blog/2022/1/15/crop-planning-for-the-flower-farmer"));
        weatherResistantCard.setOnClickListener(v -> openWebPage("https://www.climatehubs.usda.gov/topics/climate-smart-agriculture"));
        marketAccessCard.setOnClickListener(v -> openWebPage("https://www.ams.usda.gov/local-food-directories/farmersmarkets"));
        digitalToolsCard.setOnClickListener(v -> openWebPage("https://www.digitalgreen.org/farmer-tools/"));
    }

    private void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(webpage);

            // Force browser apps only
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No web browser app found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open the link", Toast.LENGTH_SHORT).show();
        }
    }

}