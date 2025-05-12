package mobdev.agrikita.models.order.response;

import com.google.gson.annotations.SerializedName;


public class CreateOrderResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("orderId")
    private String orderRef;
    @SerializedName("error")
    private String error;

    public CreateOrderResponse(String message, String orderRef) {
        this.message = message;
        this.orderRef = orderRef;
    }

    public CreateOrderResponse(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public String getOrderRef() {
        return orderRef;
    }
}
