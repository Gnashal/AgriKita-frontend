package mobdev.agrikita.controllers;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mobdev.agrikita.api.AuthServiceApi;
import mobdev.agrikita.api.client.RetrofitClient;
import mobdev.agrikita.models.auth.request.ForgotPasswordRequest;
import mobdev.agrikita.models.auth.response.ForgotPasswordResponse;
import mobdev.agrikita.models.auth.request.SignupRequest;
import mobdev.agrikita.models.auth.response.SignupResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private AuthServiceApi serviceApi;
    private static AuthService instance;


    public AuthService(Context context) {
        serviceApi = RetrofitClient.getClient(context).create(AuthServiceApi.class);
    }

    public static AuthService getInstance(Context context) {
        if (instance == null) {
            instance = new AuthService(context);
        }
        return instance;
    }

    public void loginUser(String email, String password, final LoginCallback callback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            callback.onSuccess(uid);
                        } else {
                            callback.onFailure("User object is null after login.");
                        }
                    } else {
                        callback.onFailure("Firebase login failed: " + task.getException().getMessage());
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

    public void sendEmail(String email, final ForgotPasswordCallback callback) {
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        Call<ForgotPasswordResponse> call = serviceApi.forgotPassword(request);

        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.isSuccessful()){
                    ForgotPasswordResponse res = response.body();
                    if (res != null) {
                        callback.onSuccess(res);
                    } else {
                        callback.onFailure("Sending email failed: Empty response from backend");
                    }
                } else {
                    callback.onFailure("Sending email failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                callback.onFailure("Network failure: " + t.getMessage());
            }
        });
    }

    public void validateAndNavigate(final RefreshTokenCallback callback) {
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       if (user == null) {
           callback.onFailure("User not logged in");
           return;
       }

        user.getIdToken(true).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               callback.onSuccess(true);
           } else {
               callback.onFailure("Failed to refresh token");
           }
       });
    }


    public interface RefreshTokenCallback {
        void onSuccess(boolean ok);
        void onFailure(String error);
    }
    public interface ForgotPasswordCallback{
       void onSuccess(ForgotPasswordResponse response);
       void onFailure(String error);
    }

    public interface LoginCallback {
        void onSuccess(String uid);
        void onFailure(String errorMessage);
    }
    public interface SignupCallback {
        void onSuccess(SignupResponse signupResponse);
        void onFailure(String errorMessage);
    }
}
