package mobdev.agrikita.api;

import mobdev.agrikita.models.auth.ForgotPasswordRequest;
import mobdev.agrikita.models.auth.ForgotPasswordResponse;
import mobdev.agrikita.models.auth.LoginRequest;
import mobdev.agrikita.models.auth.LoginResponse;
import mobdev.agrikita.models.auth.LoginResponseWrapper;
import mobdev.agrikita.models.auth.SignupRequest;
import mobdev.agrikita.models.auth.SignupResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthServiceApi {
    @POST("auth/login")
    Call<LoginResponseWrapper> loginUser(@Body LoginRequest request);
    @POST("auth/signup")
    Call<SignupResponse>registerUser(@Body SignupRequest request);
    @POST("auth/forgot-password")
    Call<ForgotPasswordResponse>forgotPassword(@Body ForgotPasswordRequest request);
    @GET("auth/refresh") // Replace with your actual route
    Call<Void> validateToken();
}
