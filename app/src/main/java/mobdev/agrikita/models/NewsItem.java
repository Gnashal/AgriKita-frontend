package mobdev.agrikita.models;

public class NewsItem {
    private String title;
    private String description;
    private String imageUrl;

    // Constructor, getters and setters
    public NewsItem(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
