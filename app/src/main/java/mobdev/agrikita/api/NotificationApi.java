package mobdev.agrikita.api;

import java.util.List;

import mobdev.agrikita.models.notifications.Notifications;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NotificationApi {
    @GET("get-notifications")
    Call<List<Notifications>> getNotifications();
}
