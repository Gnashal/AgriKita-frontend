package mobdev.agrikita.api;

import mobdev.agrikita.models.shop.CreateShopResponse;
import mobdev.agrikita.models.shop.GetFeaturedShopsResponse;
import mobdev.agrikita.models.shop.GetShopByShopIDResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ShopServiceApi {
    @GET("service/shop/get-shop-by-id")
    Call<GetShopByShopIDResponse> getShopByShopID(@Query("shopID") String shopID);

    @Multipart
    @POST("service/shop/create-shop")
    Call<CreateShopResponse> createShop(
            @Part MultipartBody.Part file,
            @Part("owner") RequestBody ownerUid,
            @Part("name") RequestBody name,
            @Part("address") RequestBody address,
            @Part("zip_code") RequestBody zipCode,
            @Part("shop_description") RequestBody shopDescription
    );

    @GET("service/shop/get-featured")
    Call<GetFeaturedShopsResponse> getFeaturedShops();
}
