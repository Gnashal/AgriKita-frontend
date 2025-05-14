package mobdev.agrikita.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.models.notifications.NotificationList;
import mobdev.agrikita.models.notifications.Notifications;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class NotificationService {
    private static NotificationService instance;
    private final Context context;
    private final String wsURL = "wss://agrikita.leapcell.app/api/service/notification/get-notifications";
    private WebSocket webSocket;

    public interface NotificationListener {
        void onNewNotification();
    }

    private NotificationListener listener;

    private NotificationService(Context context) {
        this.context = context;
        connectWebSocket();
    }

    public static synchronized NotificationService getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationService(context);
        }
        return instance;
    }

    public void setListener(NotificationListener listener) {
        this.listener = listener;
    }

    private void connectWebSocket() {
        Log.d("WebSocket", "connectWebSocket() CALLED");

        SharedPreferences sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("idToken", null);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer "+ token)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Request request = new Request.Builder()
                .url(wsURL)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                webSocket.send("Connected, server!");
                Log.d("WebSocket", "Connected");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("WebSocket", "Received message: " + text);
                try {
                    Gson gson = new Gson();
                    Notifications notification = gson.fromJson(text, Notifications.class);
                    NotificationList.getInstance().add(notification);
                } catch (Exception e) {
                    Log.e("WebSocket", "Failed to parse message", e);
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e("WebSocket", "Connection failed", t);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                Log.d("WebSocket", "Closing: " + reason);
            }
        });
    }
}
