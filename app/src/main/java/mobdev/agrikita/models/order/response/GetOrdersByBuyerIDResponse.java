package mobdev.agrikita.models.order.response;

import java.util.List;

import mobdev.agrikita.models.order.Orders;

public class GetOrdersByBuyerIDResponse {
    private boolean success;
    private String message;
    private List<Orders> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Orders> getData() {
        return data;
    }
}
