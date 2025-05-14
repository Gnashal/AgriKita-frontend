package mobdev.agrikita.models.notifications;

import java.util.ArrayList;
import java.util.List;


public class NotificationList {
    private static NotificationList intance;
    private List<Notifications> notifications;
    public NotificationList () {
        this.notifications = new ArrayList<>();
    }
    public static NotificationList getInstance() {
        if (intance == null) {
            intance = new NotificationList();
        }
        return intance;
    }
    public List<Notifications> getNotifications() {
        return notifications;
    }
    public void add(Notifications notification) {
        notifications.add(0, notification);
    }

    public boolean hasUnreadNotifications() {
        for (Notifications notification: notifications) {
            if (!notification.isRead_status()) {
                return true;
            }
        }
        return false;
    }
    public void markAllAsRead() {
        for (Notifications notification: notifications) {
            notification.setRead_status(true);
        }
    }
    public void remove(int position) {
        notifications.remove(position);
    }
    public void markAsRead(int position) {
        notifications.get(position).setRead_status(true);
    }
}
