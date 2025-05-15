package mobdev.agrikita.pages.addons;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.NotificationAdapter;
import mobdev.agrikita.controllers.NotificationService;
import mobdev.agrikita.models.notifications.NotificationList;
import mobdev.agrikita.models.notifications.Notifications;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {

    ImageButton go_back;
    MaterialButton notif_new, notif_markallasread;
    ListView notif_view;
    NotificationAdapter notifAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
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

        go_back = findViewById(R.id.notif_back_btn);
        notif_view =  findViewById(R.id.notif_list_view);
        notif_markallasread = findViewById(R.id.notif_markallasread_btn);


        notifAdapter = new NotificationAdapter(this);
        notif_view.setAdapter(notifAdapter);

        NotificationService.getInstance(this).setListener(() -> runOnUiThread(() -> {
            notifAdapter.notifyDataSetChanged();
        }));

        go_back.setOnClickListener(v -> finish());

        notif_markallasread.setOnClickListener(v -> {
            NotificationList.getInstance().markAllAsRead();
            notifAdapter.notifyDataSetChanged();
        });
    }
}