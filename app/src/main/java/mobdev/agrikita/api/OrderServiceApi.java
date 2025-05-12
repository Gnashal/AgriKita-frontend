package mobdev.agrikita.api;

import mobdev.agrikita.models.order.request.CreateOrderRequest;
import mobdev.agrikita.models.order.response.CreateOrderResponse;
import mobdev.agrikita.models.order.response.GetOrdersByShopIDResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderServiceApi {
    @GET("service/order/fetch-orders-by-shop")
    Call<GetOrdersByShopIDResponse> getOrdersByShopID(@Query("shopID") String shopID);

    @POST("service/order/create-order")
    Call<CreateOrderResponse> createOrder(@Body CreateOrderRequest createOrderRequest);
}
