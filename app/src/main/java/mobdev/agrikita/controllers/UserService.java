package mobdev.agrikita.controllers;

import android.content.Context;
import android.util.Log;

import java.io.File;

import mobdev.agrikita.api.RetrofitClient;
import mobdev.agrikita.api.UserServiceApi;
import mobdev.agrikita.models.user.UpdatePasswordRequest;
import mobdev.agrikita.models.user.UpdatePasswordResponse;
import mobdev.agrikita.models.user.UpdateProfileImageResponse;
import mobdev.agrikita.models.user.UpdateUserRequest;
import mobdev.agrikita.models.user.UpdateUserResponse;
import mobdev.agrikita.models.user.UserResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void updateUserData(String uid, String firstName, String lastName, String email, String phone, final UpdateUserCallback callback) {
        UpdateUserRequest request = new UpdateUserRequest(uid, firstName, lastName, email, phone);
        Call<UpdateUserResponse> call =  userServiceApi.updateUserData(request);

        call.enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                if (response.isSuccessful()) {
                    UpdateUserResponse updateUserResponse = response.body();
                    if (updateUserResponse != null) {
                        callback.onSuccess(updateUserResponse);
                    } else {
                        callback.onFailure("Update failed: Empty response");
                    }
                } else {
                    callback.onFailure("Update failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
                callback.onFailure("Network failure: " + t.getMessage());
            }
        });
    }
    public void updateProfileImage(String uid, File imageFile, final UpdateProfileImageCallback callback) {
        Log.v("UploadImage", "Upload Image Starting");
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), imageRequestBody);

        Call<UpdateProfileImageResponse> call = userServiceApi.updateProfileImage(uid, filePart);
        call.enqueue(new Callback<UpdateProfileImageResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileImageResponse> call, Response<UpdateProfileImageResponse> response) {
                if (response.isSuccessful()) {
                    Log.v("UploadImage", "Upload Image OK");
                    UpdateProfileImageResponse updateProfileImageResponse = response.body();
                    if (updateProfileImageResponse != null) {
                        callback.onSuccess(updateProfileImageResponse);
                    } else {
                        callback.onFailure("Update failed: Empty response");}
                } else {
                    Log.v("UploadImage", "could not upload: "+ response.message());
                    callback.onFailure("Update failed: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<UpdateProfileImageResponse> call, Throwable t) {
                Log.v("UploadImage", "Error uploading: "+ t.getMessage());
                callback.onFailure("Network failure: " + t.getMessage());
            }
        });
    }

    public void updatePassword(String uid, String password, final UpdatePasswordCallback callback) {
        UpdatePasswordRequest request = new UpdatePasswordRequest(uid, password);
        Call<UpdatePasswordResponse> call = userServiceApi.updatePassword(request);

        call.enqueue(new Callback<UpdatePasswordResponse>() {
            @Override
            public void onResponse(Call<UpdatePasswordResponse> call, Response<UpdatePasswordResponse> response) {
                if (response.isSuccessful()) {
                    UpdatePasswordResponse updatePasswordResponse = response.body();
                    if (updatePasswordResponse != null) {
                        callback.onSuccess(updatePasswordResponse);
                    } else {
                        callback.onFailure("Update failed: Empty response");
                    }
                } else  {
                    callback.onFailure("Update failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UpdatePasswordResponse> call, Throwable t) {
                Log.v("UpdatePassword", "Error updating: "+ t.getMessage());
                callback.onFailure("Network failure: " + t.getMessage());
            }
        });
    }

    public interface FetchUserCallback {
        void onSuccess(UserResponse userResponse);
        void onFailure(String errorMessage);
    }
    public interface UpdateUserCallback {
        void onSuccess(UpdateUserResponse updateUserResponse);
        void onFailure(String errorMessage);
    }
    public interface UpdateProfileImageCallback {
        void onSuccess(UpdateProfileImageResponse updateProfileImageResponse);
        void onFailure(String errorMessage);
    }
    public interface UpdatePasswordCallback {
        void onSuccess(UpdatePasswordResponse updatePasswordResponse);
        void onFailure(String errorMessage);
    }
}
