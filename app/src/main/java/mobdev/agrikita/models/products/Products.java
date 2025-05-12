package mobdev.agrikita.models.products;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Products implements Serializable {

    @SerializedName("id")
    private String productID;

    @SerializedName("shopID")
    private String shopID;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("name")
    private String productName;

    @SerializedName("price")
    private float price;

    @SerializedName("measuringUnit")
    private String measuringUnit;

    @SerializedName("category")
    private String category;

    @SerializedName("stockQuantity")
    private int stockQuantity;

    @SerializedName("originLocation")
    private String originLocation;

    @SerializedName("freshnessRate")
    private String freshnessRate;

    @SerializedName("storage")
    private String storage;

    @SerializedName("description")
    private String description;

    @SerializedName("isOrganic")
    private boolean isOrganic;

    @SerializedName("isFeatured")
    private boolean isFeatured;

    @SerializedName("rating")
    private String rating;

    @SerializedName("status")
    private String status;

    @SerializedName("createdAt")
    private String createdAt;

    private int quantityToBuy = 1;

    // Constructor
    public Products(String productID, String shopID, String imageUrl, String productName, float price,
                    String measuringUnit, String category, int stockQuantity, String originLocation,
                    String freshnessRate, String storage, String description, boolean isOrganic,
                    boolean isFeatured, String rating, String status, String createdAt) {
        this.productID = productID;
        this.shopID = shopID;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.price = price;
        this.measuringUnit = measuringUnit;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.originLocation = originLocation;
        this.freshnessRate = freshnessRate;
        this.storage = storage;
        this.description = description;
        this.isOrganic = isOrganic;
        this.isFeatured = isFeatured;
        this.rating = rating;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters
    public String getProductID() {
        return productID;
    }

    public String getShopID() {
        return shopID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public float getPrice() {
        return price;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public String getCategory() {
        return category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public String getFreshnessRate() {
        return freshnessRate;
    }

    public String getStorage() {
        return storage;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOrganic() {
        return isOrganic;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public String getRating() {
        return rating;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // The amount of product/s user want to buy!
    public int getQuantityToBuy() {
        return this.quantityToBuy;
    }

    public void setQuantityToBuy(int quantity) {
        this.quantityToBuy = quantity;
    }
}
