package mobdev.agrikita.pages;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.NotificationAdapter;
import mobdev.agrikita.models.notifications.Notifications;

public class Notification extends AppCompatActivity {

    ImageButton go_back;
    MaterialButton notif_new, notif_markallasread;
    ListView notif_view;

    List<Notifications> notificationsList;

    NotificationAdapter notifAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
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

        notificationsList.add(new Notifications("Order Delivered", "Your order #ORD-2025-001 has been delivered.", "2025-04-18", true));
        notificationsList.add(new Notifications("Order Shipped", "Your order #ORD-2023-002 has been shipped!", "2025-04-20", false));
        notificationsList.add(new Notifications("Payment Confirmed", "Your payment has been confirmed.", "2025-04-18", false));
        notificationsList.add(new Notifications("Special Offer!", "Get 15% off this week. Use code ORGANIC15.", "2025-04-20", false));

        notifAdapter = new NotificationAdapter(this, notificationsList);
        notif_view.setAdapter(notifAdapter);

        go_back.setOnClickListener(v -> finish());

        notif_new.setText(String.format("%d", countUnRead(notificationsList))+" New");

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