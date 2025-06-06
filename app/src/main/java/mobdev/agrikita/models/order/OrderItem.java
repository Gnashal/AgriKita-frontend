package mobdev.agrikita.models.order;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
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

    public OrderItem(String productID, String shopID, String name, float price, String imageUrl, int quantity){
        this.productID = productID;
        this.shopID = shopID;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductID() {
        return productID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getShopID() {
        return shopID;
    }
}
