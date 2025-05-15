package mobdev.agrikita.api;

import mobdev.agrikita.models.products.request.CreateProductRequest;
import mobdev.agrikita.models.products.request.RateProductRequest;
import mobdev.agrikita.models.products.request.UpdateProductRequest;
import mobdev.agrikita.models.products.response.CreateProductResponse;
import mobdev.agrikita.models.products.response.GetAllProductsResponse;
import mobdev.agrikita.models.products.response.GetFeaturedProductsResponse;
import mobdev.agrikita.models.products.response.GetProductsByShopIDResponse;
import mobdev.agrikita.models.products.response.RateProductResponse;
import mobdev.agrikita.models.products.response.UpdateProductImageResponse;
import mobdev.agrikita.models.products.response.UpdateProductResponse;
import mobdev.agrikita.models.products.response.UploadProductImageResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ProductServiceApi {
    @Multipart
    @POST("service/product/create-product-image")
    Call<UploadProductImageResponse> uploadImage(
            @Part MultipartBody.Part image
    );
    @POST("service/product/create-products")
    Call<CreateProductResponse> createProduct(@Body CreateProductRequest request);

    @GET("service/product/get-products-by-shopID")
    Call<GetProductsByShopIDResponse> getProductsByShopID(@Query("shopID") String shopID);

    @GET("service/product/get-all-products")
    Call<GetAllProductsResponse> getAllProducts(@Query("shopID") String shopID);

    @GET("service/product/get-best-sellers")
    Call<GetFeaturedProductsResponse> getBestSellers();

    @PATCH("service/product/review-product")
    Call<RateProductResponse> rateProducts(@Body RateProductRequest request);

    @PUT("service/product/update-product")
    Call<UpdateProductResponse> updateProduct(@Body UpdateProductRequest request);

    @Multipart
    @POST("service/product/update-product-image")
    Call<UpdateProductImageResponse> updateProductImage(
            @Part("productId") RequestBody productId,
            @Part("oldImageUrl") RequestBody oldImageUrl,
            @Part MultipartBody.Part newImage
    );
}
