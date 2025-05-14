package mobdev.agrikita.models.order;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;



public class Orders implements Serializable {
    @SerializedName("id")
    private String orderID;


    @SerializedName("buyerID")
    private String buyerID;

    @SerializedName("total")
    private float  total;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("status")
    private String status;

    @SerializedName("items")
    private List<OrderItem> items;


    public Orders(String id, String createdAt, float total, String status) {
        this.orderID = id;
        this.createdAt = createdAt;
        this.total = total;
        this.status = status;
    }

    // === Core Getters ===
    public String getOrderId() { return orderID; }
    public String getBuyerID() { return buyerID; }
    public float  getTotal() { return total; }
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
        private float price;

        @SerializedName("imageUrl")
        private String imageUrl;

        @SerializedName("quantity")
        private int quantity;

        // === Getters ===
        public String getProductID() { return productID; }
        public String getShopID() { return shopID; }
        public String getName() { return name; }
        public float getPrice() { return price; }
        public String getImageUrl() { return imageUrl; }
        public int getQuantity() { return quantity; }

        // Optional helper
        public String getItemDetails() {
            return name + " x" + quantity + " - ₱" + (price * quantity);
        }
    }
}
