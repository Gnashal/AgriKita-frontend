package mobdev.agrikita.api;

import java.util.List;

import mobdev.agrikita.models.Article;

public class NewsApiResponse {
    private List<Article> articles;
    public List<Article> getArticles() {
        return articles;
    }
}

