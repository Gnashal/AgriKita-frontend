package mobdev.agrikita.api;

import mobdev.agrikita.pages.Notification;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NotificationApi {
    @GET("service/notification/get-notifications")
    Call<Notification> getNotifications();
}
