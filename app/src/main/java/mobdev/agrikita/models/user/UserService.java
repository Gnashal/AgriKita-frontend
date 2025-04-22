package mobdev.agrikita.models.user;

import android.content.Context;

import mobdev.agrikita.api.RetrofitClient;
import mobdev.agrikita.api.UserServiceApi;
import mobdev.agrikita.models.auth.LoginResponse;
import retrofit2.Callback;

public class UserService {
    private UserServiceApi userServiceApi;

    public UserService(Context context) {
        userServiceApi = RetrofitClient.getClient(context).create(UserServiceApi.class);
    }

    public void fetchUserData(String uid, final FetchUserCallback callback) {
        userServiceApi.getUserData(uid).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(retrofit2.Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to fetch user data. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<UserResponse> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
                call.cancel();
            }
        });
    }


    public interface FetchUserCallback {
        void onSuccess(UserResponse userResponse);
        void onFailure(String errorMessage);
    }
}
