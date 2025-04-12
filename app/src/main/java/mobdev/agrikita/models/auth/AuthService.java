package mobdev.agrikita.models.auth;

import android.content.Context;

import mobdev.agrikita.api.AuthServiceApi;
import mobdev.agrikita.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private AuthServiceApi serviceApi;

    public AuthService(Context context) {
        serviceApi = RetrofitClient.getClient(context).create(AuthServiceApi.class);
    }
    public void loginUser(String email, String password, final LoginCallback callback) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = serviceApi.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        callback.onSuccess(loginResponse);
                    } else {
                        callback.onFailure("Login failed: Empty response");
                    }
                } else {
                    callback.onFailure("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure("Network failure: " + t.getMessage());
            }
        });
    }

    public void signupUser(String email, String username, String name, String phone, String password, final SignupCallback callback) {
        SignupRequest signupRequest = new SignupRequest(email, password, phone, username, name);
        Call<SignupResponse> call = serviceApi.registerUser(signupRequest);

        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.isSuccessful()) {
                    SignupResponse signupResponse = response.body();
                    if (signupResponse != null) {
                        callback.onSuccess(signupResponse);
                    } else {
                        callback.onFailure("Signup failed: Empty response");
                    }
                } else {
                    callback.onFailure("Signup failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                callback.onFailure("Network failure: " + t.getMessage());
            }
        });
    }
    public interface LoginCallback {
        void onSuccess(LoginResponse loginResponse);
        void onFailure(String errorMessage);
    }
    public interface SignupCallback {
        void onSuccess(SignupResponse signupResponse);
        void onFailure(String errorMessage);
    }
}
