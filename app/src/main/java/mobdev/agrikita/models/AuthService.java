package mobdev.agrikita.models;

import android.widget.Toast;

import mobdev.agrikita.api.AuthServiceApi;
import mobdev.agrikita.api.RetrofitClient;
import mobdev.agrikita.pages.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private AuthServiceApi serviceApi;

    public AuthService() {
        serviceApi = RetrofitClient.getClient().create(AuthServiceApi.class);
    }
    public void loginUser(String email, String password, final AuthServiceCallback callback) {
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
    public interface AuthServiceCallback {
        void onSuccess(LoginResponse loginResponse);
        void onFailure(String errorMessage);
    }
}
