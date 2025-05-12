package mobdev.agrikita.models.order.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mobdev.agrikita.models.order.Orders;

public class GetOrdersByBuyerIDResponse {
    @SerializedName("error")
    private String error;
    @SerializedName("orders")
    private List<Orders> data;



    public String getMessage() {
        return error;
    }

    public List<Orders> getData() {
        return data;
    }
}
