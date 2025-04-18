package mobdev.agrikita.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String name, description, category, seller;
    private double price, rating;
    private int quantity, imageResId;

    public Product(String name, String description, String category, String seller, double price, double rating, int quantity, int imageResId) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.seller = seller;
        this.price = price;
        this.rating = rating;
        this.quantity = quantity;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
