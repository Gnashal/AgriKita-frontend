package mobdev.agrikita.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import mobdev.agrikita.api.OrderServiceApi;
import mobdev.agrikita.api.RetrofitClient;
import mobdev.agrikita.models.order.Orders;
import mobdev.agrikita.models.order.response.GetOrdersByShopIDResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService {
    private final OrderServiceApi serviceOrdersApi;
    private final Context context;

    public OrderService(Context context) {
        this.serviceOrdersApi = RetrofitClient.getClient(context).create(OrderServiceApi.class);
        this.context = context;
    }

    public interface OrderCallback {
        void onOrdersFetched(List<Orders> orders);
        void onFailure(Exception e);
    }

    public void getOrdersByShopID(String shopID, final OrderCallback callback) {
        Call<GetOrdersByShopIDResponse> call = serviceOrdersApi.getOrdersByShopID(shopID);

        call.enqueue(new Callback<GetOrdersByShopIDResponse>() {
            @Override
            public void onResponse(Call<GetOrdersByShopIDResponse> call, Response<GetOrdersByShopIDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Orders> orders = response.body().getOrders();
                    Log.d("OrderService", "Retrieved " + orders.size() + " orders.");
                    callback.onOrdersFetched(orders);
                } else {
                    Log.e("OrderService", "Fetch failed: " + response.code() + " - " + response.message());
                    Toast.makeText(context, "Failed to fetch orders: " + response.message(), Toast.LENGTH_SHORT).show();
                    callback.onFailure(new Exception("Failed to fetch orders: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<GetOrdersByShopIDResponse> call, Throwable t) {
                Log.e("OrderService", "Error fetching orders: ", t);
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onFailure(new Exception(t));
            }
        });
    }
}