package mobdev.agrikita.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import mobdev.agrikita.utils.NewsRepository;

public class LatestNewsViewModel extends ViewModel {
    private final NewsRepository newsRepository = new NewsRepository();
    private final MutableLiveData<NewsResponse> newsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public void fetchNews(String query, String apiKey) {
        newsRepository.getNews(query, apiKey, new NewsRepository.NewsCallback() {
            @Override
            public void onSuccess(NewsResponse response) {
                // Limit the articles to 4
                List<NewsArticle> limitedArticles = response.getArticles();
                if (limitedArticles != null && limitedArticles.size() > 4) {
                    limitedArticles = limitedArticles.subList(0, 4); // Limit to first 4
                }
                response.setArticles(limitedArticles);
                newsLiveData.postValue(response);
            }

            @Override
            public void onFailure(String errorMessage) {
                errorLiveData.postValue(errorMessage);
            }
        });
    }

    public LiveData<NewsResponse> getNewsLiveData() {
        return newsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
