package mobdev.agrikita.models.order;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOrdersByShopIDResponse {
    @SerializedName("orders")
    private List<Orders> orders;

    public List<Orders> getOrders() {
        return orders;
    }
}
