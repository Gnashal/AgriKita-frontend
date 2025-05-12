package mobdev.agrikita.models.order;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;



public class Orders implements Serializable {

    public Orders(String id, String createdAt, int total, String status) {
        this.orderID = id;
        this.createdAt = createdAt;
        this.total = total;
        this.status = status;
    }

    @SerializedName("orderID")
    private String orderID;

    @SerializedName("shopID")
    private String shopID;

    @SerializedName("buyerID")
    private String buyerID;

    @SerializedName("total")
    private int total;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("status")
    private String status;

    @SerializedName("items")
    private List<OrderItem> items;

    // === Core Getters ===
    public String getOrderId() { return orderID; }
    public String getShopID() { return shopID; }
    public String getBuyerID() { return buyerID; }
    public int getTotal() { return total; }
    public String getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public List<OrderItem> getItems() { return items; }

    // === Display Helpers ===
    public String getOrderDate() {
        return createdAt; // or format if needed
    }

    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public String getItemDetails() {
        if (items == null || items.isEmpty()) return "No items";
        StringBuilder sb = new StringBuilder();
        for (OrderItem item : items) {
            sb.append(item.getName())
              .append(" x")
              .append(item.getQuantity())
              .append(" - ₱")
              .append(item.getPrice() * item.getQuantity())
              .append("\n");
        }
        return sb.toString().trim();
    }

    // === Inner OrderItem Class ===
    public static class OrderItem implements Serializable {
        @SerializedName("productID")
        private String productID;

        @SerializedName("shopID")
        private String shopID;

        @SerializedName("name")
        private String name;

        @SerializedName("price")
        private int price;

        @SerializedName("imageUrl")
        private String imageUrl;

        @SerializedName("quantity")
        private int quantity;

        // === Getters ===
        public String getProductID() { return productID; }
        public String getShopID() { return shopID; }
        public String getName() { return name; }
        public int getPrice() { return price; }
        public String getImageUrl() { return imageUrl; }
        public int getQuantity() { return quantity; }

        // Optional helper
        public String getItemDetails() {
            return name + " x" + quantity + " - ₱" + (price * quantity);
        }
    }
}
