package mobdev.agrikita.models.products.request;

public class CreateProductRequest {

    private String shopId;
    private String imageUrl;
    private String name;
    private double price;
    private String measuringUnit;
    private String category;
    private int stockQuantity;
    private String originLocation;
    private String freshnessRate;
    private String storage;
    private String description;
    private boolean isOrganic;
    private boolean isFeatured;
    private String status;

    // Constructor
    public CreateProductRequest() {
    }

    public CreateProductRequest(String shopId, String imageUrl, String name, double price,
                                String measuringUnit, String category, int stockQuantity,
                                String originLocation, String freshnessRate, String storage,
                                String description, boolean isOrganic, boolean isFeatured,
                                String status) {
        this.shopId = shopId;
        this.imageUrl = imageUrl;
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

    // Getters and setters
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getFreshnessRate() {
        return freshnessRate;
    }

    public void setFreshnessRate(String freshnessRate) {
        this.freshnessRate = freshnessRate;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOrganic() {
        return isOrganic;
    }

    public void setOrganic(boolean organic) {
        isOrganic = organic;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
