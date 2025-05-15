package mobdev.agrikita.controllers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.api.client.RetrofitClient;
import mobdev.agrikita.api.ShopServiceApi;
import mobdev.agrikita.models.shop.response.CreateShopResponse;
import mobdev.agrikita.models.shop.response.GetFeaturedShopsResponse;
import mobdev.agrikita.models.shop.response.GetShopByShopIDResponse;
import mobdev.agrikita.models.shop.Shop;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public void createShop(MultipartBody.Part file,
                           RequestBody ownerUid,
                           RequestBody name,
                           RequestBody address,
                           RequestBody zipCode,
                           RequestBody shopDescription,
                           CreateShopCallback callback) {

        Call<CreateShopResponse> call = serviceShopApi.createShop(
                file, ownerUid, name, address, zipCode, shopDescription
        );

        call.enqueue(new Callback<CreateShopResponse>() {
            @Override
            public void onResponse(Call<CreateShopResponse> call, Response<CreateShopResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to create shop. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CreateShopResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getFeaturedShops(FeaturedShopsCallback callback) {
        Call<GetFeaturedShopsResponse> call = serviceShopApi.getFeaturedShops();

        call.enqueue(new Callback<GetFeaturedShopsResponse>() {
            @Override
            public void onResponse(Call<GetFeaturedShopsResponse> call, Response<GetFeaturedShopsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Shop> shops = response.body().getShops();
                    if (shops != null) {
                        Log.d("ShopService", "Retrieved " + shops.size() + " featured shops.");
                    } else {
                        Log.d("ShopService", "No featured shops found.");
                        shops = new ArrayList<>(); // Prevent null issues
                    }
                    callback.onSuccess(shops);
                } else {
                    Log.e("ShopService", "Fetch failed: " + response.code() + " - " + response.message());
                    callback.onError("Failed to fetch featured shops: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFeaturedShopsResponse> call, Throwable t) {
                Log.e("ShopService", "Network error: " + t.getMessage(), t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }


    public interface FeaturedShopsCallback {
        void onSuccess(List<Shop> shops);
        void onError(String errorMessage);
    }


    public interface ShopCallback {
        void onSuccess(GetShopByShopIDResponse shop);
        void onError(String errorMessage);
    }

    public interface CreateShopCallback {
        void onSuccess(CreateShopResponse response);
        void onError(String errorMessage);
    }
}
