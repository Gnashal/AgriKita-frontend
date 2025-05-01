package mobdev.agrikita.api;

import mobdev.agrikita.models.products.GetProductsByShopIDResponse;
import mobdev.agrikita.models.shop.GetShopByShopIDResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShopServiceApi {
    @GET("service/shop/get-shop-by-id")
    Call<GetShopByShopIDResponse> getShopByShopID(@Query("shopID") String shopID);
}
