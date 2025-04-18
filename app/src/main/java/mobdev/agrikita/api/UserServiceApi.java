package mobdev.agrikita.api;

import mobdev.agrikita.models.user.UserResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface UserServiceApi {
    @GET("service/user/get-user-data")
    Call<UserResponse> getUserData(@Query("uid") String uid);
}
