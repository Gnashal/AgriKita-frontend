package mobdev.agrikita.api;

import mobdev.agrikita.models.LoginRequest;
import mobdev.agrikita.models.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthServiceApi {
    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
/*    @POST("auth/signup")*/
}
