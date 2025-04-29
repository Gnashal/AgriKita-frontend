package mobdev.agrikita.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mobdev.agrikita.R;

public class About extends AppCompatActivity {

    private Button modulebutton;
    private LinearLayout marketplace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Make sure R.id.main exists in your XML
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views (make sure the IDs exist in XML)
        modulebutton = findViewById(R.id.module_btn);
        marketplace = findViewById(R.id.mplace_layout);

        // Do not call clicklisteners() to avoid null crashes
         clicklisteners();
    }

    private void clicklisteners() {
//         modulebutton.setOnClickListener(v ->
////                 startActivity(new Intent(About.this, ModuleActivity.class))
//         );

         marketplace.setOnClickListener(v ->
                 startActivity(new Intent(About.this, Marketplace.class))
         );
    }
}
