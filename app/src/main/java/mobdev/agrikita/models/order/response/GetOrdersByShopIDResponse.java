package mobdev.agrikita.models.order.response;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import mobdev.agrikita.models.order.Orders;

public class GetOrdersByShopIDResponse {
    @SerializedName("orders")
    private List<Orders> orders;

    public List<Orders> getOrders() {
        return orders;
    }
}
