package mobdev.agrikita.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/*Found this online, contacts backend api*/
public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:4040/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
