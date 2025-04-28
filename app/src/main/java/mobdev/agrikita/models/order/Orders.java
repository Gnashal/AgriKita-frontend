package mobdev.agrikita.models.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Orders {
    @SerializedName("id")
    private String id;
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

    public String getId() { return id; }
    public String getShopID() { return shopID; }
    public String getBuyerID() { return buyerID; }
    public int getTotal() { return total; }
    public String getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public List<OrderItem> getItems() { return items; }

    public static class OrderItem {
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

        public String getProductID() { return productID; }
        public String getShopID() { return shopID; }
        public String getName() { return name; }
        public int getPrice() { return price; }
        public String getImageUrl() { return imageUrl; }
        public int getQuantity() { return quantity; }
    }
}

