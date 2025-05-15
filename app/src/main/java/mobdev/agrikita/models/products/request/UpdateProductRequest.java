package mobdev.agrikita.models.products.request;

import com.google.gson.annotations.SerializedName;

public class UpdateProductRequest {
    @SerializedName("productId")
    String productId;

    @SerializedName("name")
    String name;

    @SerializedName("price")
    float price;

    @SerializedName("measuringUnit") // Not "unit"
    String measuringUnit;

    @SerializedName("category")
    String category;

    @SerializedName("stockQuantity") // Not "quantity"
    int stockQuantity;

    @SerializedName("originLocation")
    String originLocation;

    @SerializedName("freshnessRate") // Not "freshness"
    String freshnessRate;

    @SerializedName("storage")
    String storage;

    @SerializedName("description")
    String description;

    @SerializedName("isOrganic")
    boolean isOrganic;

    @SerializedName("isFeatured")
    boolean isFeatured;

    @SerializedName("status")
    String status;
    public UpdateProductRequest(String productId, String name, float price, String measuringUnit, String category, int stockQuantity, String originLocation, String freshnessRate, String storage, String description, boolean isOrganic, boolean isFeatured, String status) {
        this.productId = productId;
        this.name = name;
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
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
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

    public String getStatus() {
        return status;
    }
}
