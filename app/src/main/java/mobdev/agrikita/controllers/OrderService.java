package mobdev.agrikita.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.SocketTimeoutException;
import java.util.List;

import mobdev.agrikita.api.OrderServiceApi;
import mobdev.agrikita.api.client.RetrofitClient;
import mobdev.agrikita.models.order.Orders;
import mobdev.agrikita.models.order.request.CreateOrderRequest;
import mobdev.agrikita.models.order.response.CreateOrderResponse;
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
    public void createOrder(CreateOrderRequest request, final CreateOrderCallback callback) {
        // Validate request before sending
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            callback.onFailure("Order items cannot be empty");
            return;
        }

        if (request.getAddress() == null) {
            callback.onFailure("Shipping address is required");
            return;
        }

        Call<CreateOrderResponse> call = serviceOrdersApi.createOrder(request);

        call.enqueue(new Callback<CreateOrderResponse>() {
            @Override
            public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                if (response.isSuccessful()) {
                    CreateOrderResponse createOrderResponse = response.body();
                    if (createOrderResponse != null && createOrderResponse.getOrderRef() != null) {
                        // Success case
                        Log.d("OrderService", "Order created with ID: " + createOrderResponse.getOrderRef());
                        callback.onSuccess(
                                createOrderResponse.getMessage() != null ?
                                        createOrderResponse.getMessage() : "Order created successfully",
                                createOrderResponse.getOrderRef()
                        );
                    } else {
                        Log.e("OrderService", "Empty response body");
                        callback.onFailure("Server returned empty response");
                    }
                } else {
                    try {
                        CreateOrderResponse errorResponse = new Gson().fromJson(
                                response.errorBody().charStream(),
                                CreateOrderResponse.class
                        );

                        String errorMessage = errorResponse.getError() != null ?
                                errorResponse.getError() : "Order failed (HTTP " + response.code() + ")";

                        Log.e("OrderService", errorMessage);
                        callback.onFailure(errorMessage);
                    } catch (Exception e) {
                        Log.e("OrderService", "Failed to parse error", e);
                        callback.onFailure("Order failed (HTTP " + response.code() + ")");
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                String errorMessage = "Network error: " + t.getMessage();
                if (t instanceof SocketTimeoutException) {
                    errorMessage = "Request timed out. Please check your connection";
                }
                Log.e("OrderService", errorMessage, t);
                callback.onFailure(errorMessage);
            }
        });
    }

    public interface OrderCallback {
        void onOrdersFetched(List<Orders> orders);
        void onFailure(Exception e);
    }

    public interface CreateOrderCallback {
        void onSuccess(String message, String orderId);
        void onFailure(String error);
    }


}