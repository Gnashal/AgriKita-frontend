package mobdev.agrikita.controllers;

import android.content.Context;
import android.util.Log;

import mobdev.agrikita.api.ProductServiceApi;
import mobdev.agrikita.api.RetrofitClient;
import mobdev.agrikita.api.ShopServiceApi;
import mobdev.agrikita.models.shop.GetShopByShopIDResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopService {
    private final ShopServiceApi serviceShopApi;
    private final Context context;


    public ShopService(Context context) {
        this.context = context;
        serviceShopApi = RetrofitClient.getClient(context).create(ShopServiceApi.class);
    }

    public void getShopById(String shopId, ShopCallback callback) {
        Call<GetShopByShopIDResponse> call = serviceShopApi.getShopByShopID(shopId);

        call.enqueue(new Callback<GetShopByShopIDResponse>() {
            @Override
            public void onResponse(Call<GetShopByShopIDResponse> call, Response<GetShopByShopIDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GetShopByShopIDResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface ShopCallback {
        void onSuccess(GetShopByShopIDResponse shop);
        void onError(String errorMessage);
    }

}
