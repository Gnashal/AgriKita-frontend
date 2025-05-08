package mobdev.agrikita.api;

import mobdev.agrikita.models.user.response.FetchUserByIDResponse;
import mobdev.agrikita.models.user.request.UpdatePasswordRequest;
import mobdev.agrikita.models.user.response.UpdatePasswordResponse;
import mobdev.agrikita.models.user.response.UpdateProfileImageResponse;
import mobdev.agrikita.models.user.request.UpdateUserRequest;
import mobdev.agrikita.models.user.response.UpdateUserResponse;
import mobdev.agrikita.models.user.response.UserResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface UserServiceApi {
    @GET("service/user/get-user-data")
    Call<UserResponse> getUserData(@Query("uid") String uid);

    @POST("auth/update-user")
    Call<UpdateUserResponse> updateUserData (@Body UpdateUserRequest request);
    @Multipart
    @POST("service/user/update-profile-image")
    Call<UpdateProfileImageResponse> updateProfileImage(
            @Query("uid") String uid,
            @Part MultipartBody.Part file
    );
    @PATCH("auth/update-password")
    Call<UpdatePasswordResponse>updatePassword(@Body UpdatePasswordRequest request);

    @GET("service/user/fetch-user-by-id")
    Call<FetchUserByIDResponse> fetchUserByID (@Query("uid") String uid);

}

