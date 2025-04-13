package mobdev.agrikita.models;

public class Products {
    private String rating;
    private double price;
    private String measuringUnit;
    private String category;
    private String productName;
    private String description;
    private String status;
    private String shopID;
    private int quantity;
    private String quality;
    private String imageUrl;
    private String createdAt;

    public Products(String rating, double price, String measuringUnit, String category, String productName, String description, String status, String shopID, int quantity, String quality, String imageUrl, String createdAt) {
        this.rating = rating;
        this.price = price;
        this.measuringUnit = measuringUnit;
        this.category = category;
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.shopID = shopID;
        this.quantity = quantity;
        this.quality = quality;
        this.createdAt = createdAt;
    }

    // Getters
    public String getRating() { return rating; }
    public double getPrice() { return price; }
    public String getMeasuringUnit() { return measuringUnit; }
    public String getCategory() { return category; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }

    public String getStatus() {
        return status;
    }

    public String getShopID() {
        return shopID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getQuality() {
        return quality;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}