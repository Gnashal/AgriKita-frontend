package mobdev.agrikita.api;

import java.util.List;

import mobdev.agrikita.models.products.CreateProductRequest;
import mobdev.agrikita.models.products.CreateProductResponse;
import mobdev.agrikita.models.products.GetAllProductsResponse;
import mobdev.agrikita.models.products.GetProductsByShopIDResponse;
import mobdev.agrikita.models.products.Products;
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

    @GET("service/product/get-all-products")
    Call<GetAllProductsResponse> getAllProducts();

}
