package mobdev.agrikita.api;

import mobdev.agrikita.models.auth.request.ForgotPasswordRequest;
import mobdev.agrikita.models.auth.response.ForgotPasswordResponse;
import mobdev.agrikita.models.auth.request.SignupRequest;
import mobdev.agrikita.models.auth.response.SignupResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthServiceApi {
    @POST("auth/signup")
    Call<SignupResponse>registerUser(@Body SignupRequest request);
    @POST("auth/forgot-password")
    Call<ForgotPasswordResponse>forgotPassword(@Body ForgotPasswordRequest request);
}
