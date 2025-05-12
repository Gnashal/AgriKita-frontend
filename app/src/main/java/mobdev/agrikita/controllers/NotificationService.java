package mobdev.agrikita.controllers;

import android.content.Context;

import java.util.List;

import mobdev.agrikita.api.NotificationApi;
import mobdev.agrikita.api.client.RetrofitClient;
import mobdev.agrikita.models.notifications.Notifications;
import retrofit2.Callback;

public class NotificationService {
    private final NotificationApi serviceNotificationApi;

    public NotificationService(Context context) {
        serviceNotificationApi = RetrofitClient.getClient(context).create(NotificationApi.class);
    }

    public void getNotifications(Callback<List<Notifications>> callback) {
        serviceNotificationApi.getNotifications().enqueue(callback);
    }
}
