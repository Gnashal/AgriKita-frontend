package mobdev.agrikita.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.IOException;

import mobdev.agrikita.pages.Login;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    /*INFO:
    * When running locally use localIP, but when wirelessly
    * emulating use wirelessIP
    * */
    private static  final String localIP = "http://10.0.2.2:4040/api/";
    private static final String wirelessIP = "http://192.168.180.240:4040/api/"; /*Replace with local ip*/
    private static final String BACKEND_URL = "https://griita-backend-gnashal6914-x2n9tdsh.leapcell.dev/api/"; /*Deployed backend*/
    private static final String BASE_URL = wirelessIP;
    private static boolean isRefreshingToken = false;
    private static final Object lock = new Object();
  
    private static Retrofit retrofit = null;
    
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
                        SharedPreferences userPrefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        String idToken = prefs.getString("idToken", null);
                        String refreshToken = prefs.getString("refreshToken", null);

                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder();

                        if (idToken != null && !idToken.isEmpty()) {
                            builder.addHeader("Authorization", "Bearer " + idToken);
                        }

                        Request requestWithAuth = builder.build();
                        Response response = chain.proceed(requestWithAuth);

                       /*If unauthorized, refresh using new refresh route*/
                        if (response.code() == 401 && refreshToken != null) {
                            response.close();

                            synchronized (lock) {
                                if (!isRefreshingToken) {
                                    isRefreshingToken = true;

                                    try {
                                        Request refreshRequest = new Request.Builder()
                                                .url(BASE_URL + "auth/refresh")
                                                .post(okhttp3.RequestBody.create(new byte[0]))
                                                .addHeader("X-Ref-Tok", refreshToken)
                                                .build();

                                        Response refreshResponse = chain.proceed(refreshRequest);

                                        if (refreshResponse.isSuccessful()) {
                                            String newIdToken = refreshResponse.header("X-New-Tok");

                                            if (newIdToken != null) {
                                                prefs.edit().putString("idToken", newIdToken).apply();

                                                Request retryRequest = originalRequest.newBuilder()
                                                        .removeHeader("Authorization")
                                                        .addHeader("Authorization", "Bearer " + newIdToken)
                                                        .build();

                                                return chain.proceed(retryRequest);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    prefs.edit().clear().apply();
                                    userPrefs.edit().clear().apply();
                                    Intent intent = new Intent(context, Login.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                    Toast.makeText(context, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
                                    throw new IOException("Unauthorized - Redirecting to Login");
                                } else {
                                    while (isRefreshingToken) {
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    return chain.proceed(originalRequest.newBuilder()
                                            .removeHeader("Authorization")
                                            .addHeader("Authorization", "Bearer " + prefs.getString("idToken", ""))
                                            .build());
                                }
                            }
                        }

                        return response;
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
}
