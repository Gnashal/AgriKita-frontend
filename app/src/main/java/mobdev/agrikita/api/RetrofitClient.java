package mobdev.agrikita.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Tasks;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import mobdev.agrikita.pages.Login;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String WIRELESS_URL = "http://192.168.68.115:4040/api/";
    private static final String LOCAL_URL  = "http://10.0.2.2:4040/api/";
    private static final String BACKEND_URL = "https://agrikita.leapcell.app/api/";
    private static final String BASE_URL = BACKEND_URL;
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {
                            try {
                                String idToken = Tasks.await(user.getIdToken(true)).getToken(); // force refresh

                                if (idToken != null && !idToken.isEmpty()) {
                                    builder.addHeader("Authorization", "Bearer " + idToken);

                                    // Optionally store token for future use
                                    SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
                                    prefs.edit().putString("idToken", idToken).apply();
                                }
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                throw new IOException("Failed to refresh Firebase token", e);
                            }
                        } else {
                            redirectToLogin(context);
                            throw new IOException("User not logged in. Redirecting.");
                        }

                        Request authRequest = builder.build();
                        return chain.proceed(authRequest);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static void redirectToLogin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        SharedPreferences userPrefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        userPrefs.edit().clear().apply();

        Intent intent = new Intent(context, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        Toast.makeText(context, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
    }
}
