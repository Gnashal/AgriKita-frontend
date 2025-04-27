package mobdev.agrikita.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/*Found this online, contacts backend api*/
public class RetrofitClient {
    /*INFO:
    * When running locally use localIP, but when wirelessly
    * emulating use wirelessIP
    * */
    private static  final String localIP = "10.0.2.2:4040";
    private static final String wirelessIP = "192.168.153.240:4040"; /*Replace with local ip*/
    private static final String BASE_URL = "http://"+ wirelessIP + "/api/";
    private static Retrofit retrofit = null;
    
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
                        String idToken = prefs.getString("idToken", null);

                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder();

                        // Only add header if token is not null
                        if (idToken != null && !idToken.isEmpty()) {
                            builder.addHeader("Authorization", "Bearer " + idToken);
                        }

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
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
