package mobdev.agrikita.api;

import mobdev.agrikita.models.user.UpdateProfileImageResponse;
import mobdev.agrikita.models.user.UpdateUserRequest;
import mobdev.agrikita.models.user.UpdateUserResponse;
import mobdev.agrikita.models.user.UserResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface UserServiceApi {
    @GET("service/user/get-user-data")
    Call<UserResponse> getUserData(@Query("uid") String uid);

    /*TODO: Implement update user api*/
    @POST("auth/update-user")
    Call<UpdateUserResponse> updateUserData (@Body UpdateUserRequest request);
    @Multipart
    @POST("service/user/update-profile-image")
    Call<UpdateProfileImageResponse> updateProfileImage(
            @Query("uid") String uid,
            @Part MultipartBody.Part file
    );
}

