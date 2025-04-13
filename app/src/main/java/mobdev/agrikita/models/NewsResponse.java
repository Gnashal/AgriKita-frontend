package mobdev.agrikita.models;

import java.util.List;

public class NewsResponse {
    private String status;
    private int totalResults;
    private List<NewsArticle> articles;

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<NewsArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsArticle> limitedArticles) {
        this.articles = articles;
    }
}