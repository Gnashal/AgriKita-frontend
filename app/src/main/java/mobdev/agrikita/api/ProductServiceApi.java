package mobdev.agrikita.api;

import mobdev.agrikita.models.products.CreateProductRequest;
import mobdev.agrikita.models.products.CreateProductResponse;
import mobdev.agrikita.models.products.GetProductsByShopIDResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductServiceApi {
    @POST("service/product/create-products")
    Call<CreateProductResponse> createProduct(@Body CreateProductRequest request);

    @GET("service/product/get-products-by-shopID")
    Call<GetProductsByShopIDResponse> getProductsByShopID(@Query("shopID") String shopID);
}
