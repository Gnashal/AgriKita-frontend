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
import mobdev.agrikita.models.notifications.Notifications;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {

    ImageButton go_back;
    MaterialButton notif_new, notif_markallasread;
    ListView notif_view;

    List<Notifications> notificationsList;

    NotificationAdapter notifAdapter;
    NotificationService notificationService;

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

        notif_new = findViewById(R.id.notif_new_btn);
        notif_markallasread = findViewById(R.id.notif_markallasread_btn);

        notificationsList = new ArrayList<>();

        notificationService = new NotificationService(this);

        notificationService.getNotifications(new Callback<List<Notifications>>() {
            @Override
            public void onResponse(Call<List<Notifications>> call, Response<List<Notifications>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notificationsList.clear();
                    notificationsList.addAll(response.body());
                    notifAdapter.notifyDataSetChanged();
                    notif_new.setText(String.format("%d", countUnRead(notificationsList))+" New");
                } else {
                    Toast.makeText(Notification.this, "No notifications available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Notifications>> call, Throwable t) {
                Toast.makeText(Notification.this, "Error fetching notifications", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

        notifAdapter = new NotificationAdapter(this, notificationsList);
        notif_view.setAdapter(notifAdapter);

        go_back.setOnClickListener(v -> finish());

        notif_markallasread.setOnClickListener(v -> {
            for (Notifications item : notificationsList) {
                item.setRead_status(true);
                notif_new.setText("0 New");
            }
            notifAdapter.notifyDataSetChanged();
        });
    }

    private int countUnRead(List<Notifications> notificationsList) {
        int count = 0;
        for (Notifications item: notificationsList) {
            if (!item.isRead_status()) {
                count += 1;
            }
        }

        return count;
    }
}