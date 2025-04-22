package mobdev.agrikita.models.products;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobdev.agrikita.api.ProductServiceApi;
import mobdev.agrikita.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductService {
    private final ProductServiceApi serviceProductsApi;
    private final Context context;

    public interface ProductCallback {
        void onProductsFetched(List<Products> products);
        void onFailure(Throwable t);
    }

    public ProductService(Context context) {
        this.context = context;
        serviceProductsApi = RetrofitClient.getClient(context).create(ProductServiceApi.class);
    }

    public void createProduct(CreateProductRequest request) {
        Call<CreateProductResponse> call = serviceProductsApi.createProduct(request);

        call.enqueue(new Callback<CreateProductResponse>() {
            @Override
            public void onResponse(Call<CreateProductResponse> call, Response<CreateProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    String productId = response.body().getProductId();
                    Toast.makeText(context, "Product Created! ID: " + productId, Toast.LENGTH_LONG).show();
                } else {
                    Log.e("ProductService", "Create failed: " + response.code());
                    Toast.makeText(context, "Failed to create product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateProductResponse> call, Throwable t) {
                Log.e("ProductService", "Error: ", t);
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProductsByShopID(String shopID, final ProductCallback callback) {
        Call<GetProductsByShopIDResponse> call = serviceProductsApi.getProductsByShopID(shopID);

        call.enqueue(new Callback<GetProductsByShopIDResponse>() {
            @Override
            public void onResponse(Call<GetProductsByShopIDResponse> call, Response<GetProductsByShopIDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Products> products = response.body().getProducts();
                    Log.d("ProductService", "Retrieved " + products.size() + " products: " + response.body().getMessage());
                    callback.onProductsFetched(products);
                } else {
                    Log.e("ProductService", "Fetch failed: " + response.code() + " - " + response.message());
                    Toast.makeText(context, "Failed to fetch products: " + response.message(), Toast.LENGTH_SHORT).show();
                    callback.onFailure(new Exception("Failed to fetch products: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<GetProductsByShopIDResponse> call, Throwable t) {
                Log.e("ProductService", "Error fetching products: ", t);
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}