package mobdev.agrikita.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.notifications.NotificationList;
import mobdev.agrikita.models.notifications.Notifications;

public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private List<Notifications> notificationsList;

    public NotificationAdapter(Context context) {
        this.context = context;
        this.notificationsList = NotificationList.getInstance().getNotifications();
    }

    @Override
    public int getCount() {
        return notificationsList.size();
    }

    @Override
    public Notifications getItem(int position) {
        return notificationsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.notification_card, null);
        }

        Notifications notifData = getItem(position);

        FrameLayout notifIndicator = convertView.findViewById(R.id.notifcard_indicator);
        TextView notifTitle = convertView.findViewById(R.id.notifcard_title);
        TextView notifContent = convertView.findViewById(R.id.notifcard_content);
        TextView notifDate = convertView.findViewById(R.id.notifcard_date);
        Button notifMarkAllAsRead = convertView.findViewById(R.id.notifcard_markasread);
        Button deleteBtn = convertView.findViewById(R.id.deleteBtn);

        notifTitle.setText(notifData.getTitle());
        notifContent.setText(notifData.getContent());
        notifDate.setText(notifData.getDate());

        if (notifData.isRead_status()) {
            notifIndicator.setBackgroundColor(Color.TRANSPARENT);
            notifMarkAllAsRead.setVisibility(View.GONE);
        } else {
            notifIndicator.setBackgroundColor(context.getResources().getColor(R.color.agrikita_green));
            notifMarkAllAsRead.setVisibility(View.VISIBLE);
        }

        notifMarkAllAsRead.setOnClickListener(v -> {
            notifData.setRead_status(true);
            NotificationList.getInstance().markAsRead(position);
            notifIndicator.setBackgroundColor(Color.TRANSPARENT);
            notifMarkAllAsRead.setVisibility(View.GONE);

            notifyDataSetChanged();
        });
        deleteBtn.setOnClickListener(v -> {
            NotificationList.getInstance().remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
