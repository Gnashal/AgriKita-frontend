package mobdev.agrikita.models.order.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mobdev.agrikita.models.address.Address;
import mobdev.agrikita.models.order.OrderItem;

public class CreateOrderRequest {
    @SerializedName("buyer_id")
    private String buyerID;
    @SerializedName("buyer_name")
    private String buyerName;
    @SerializedName("logistics")
    private String logistics;
    @SerializedName("payment_method")
    private String paymentMethod;
    @SerializedName("total")
    private double  total;
    @SerializedName("address")
    private Address address;
    @SerializedName("items")
    private List<OrderItem> items;

    public CreateOrderRequest(String buyerID, String buyerName, String logistics, String paymentMethod, double total, List<OrderItem> items, Address address) {
        this.buyerID = buyerID;
        this.buyerName = buyerName;
        this.logistics = logistics;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.items = items;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public double getTotal() {
        return total;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getLogistics() {
        return logistics;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
