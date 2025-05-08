package mobdev.agrikita.models.auth.response;

import java.util.List;

import mobdev.agrikita.models.news.Article;

public class NewsApiResponse {
    private List<Article> articles;
    public List<Article> getArticles() {
        return articles;
    }
}

