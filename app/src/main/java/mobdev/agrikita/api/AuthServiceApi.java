package mobdev.agrikita.api;

import mobdev.agrikita.models.auth.LoginRequest;
import mobdev.agrikita.models.auth.LoginResponse;
import mobdev.agrikita.models.auth.SignupRequest;
import mobdev.agrikita.models.auth.SignupResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthServiceApi {
    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
    @POST("auth/signup")
    Call<SignupResponse>registerUser(@Body SignupRequest request);
}
