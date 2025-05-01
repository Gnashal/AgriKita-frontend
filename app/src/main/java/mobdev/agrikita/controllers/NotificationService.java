package mobdev.agrikita.controllers;

import android.content.Context;

import mobdev.agrikita.api.NotificationApi;
import mobdev.agrikita.api.RetrofitClient;

public class NotificationService {
    private final NotificationApi serviceNotificationApi;
    private final Context context;


    public NotificationService(Context context) {
        serviceNotificationApi = RetrofitClient.getClient(context).create(NotificationApi.class);
        this.context = context;
    }

    public void getNotifications() {
        return;
    }
}
