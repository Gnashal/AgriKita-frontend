package mobdev.agrikita.models;

public class Products {
    private String farmName;
    private int imageResId;
    private String rating;
    private String price;
    private String unit;
    private String category;
    private String productName;
    private String description;

    public Products(String farmName, int imageResId, String rating, String price, String unit, String category, String productName, String description) {
        this.farmName = farmName;
        this.imageResId = imageResId;
        this.rating = rating;
        this.price = price;
        this.unit = unit;
        this.category = category;
        this.productName = productName;
        this.description = description;
    }

    // Getters
    public String getFarmName() { return farmName; }
    public int getImageResId() { return imageResId; }
    public String getRating() { return rating; }
    public String getPrice() { return price; }
    public String getUnit() { return unit; }
    public String getCategory() { return category; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
}