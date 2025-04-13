package mobdev.agrikita.models;

public class Orders {
    private String orderID;
    private String productID;
    private String buyerID;
    private int quantity;
    private String createdAt;

    public Orders(String orderID, String productID, String buyerID, int quantity, String createdAt) {
        this.orderID = orderID;
        this.productID = productID;
        this.buyerID = buyerID;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getProductID() {
        return productID;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
