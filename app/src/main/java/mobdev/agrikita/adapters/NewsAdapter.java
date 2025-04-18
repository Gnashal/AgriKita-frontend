package mobdev.agrikita.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.news.Article; // Updated Article model

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<Article> articles = new ArrayList<>();

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articles.get(position);

        String title = article.getTitle() != null ? article.getTitle() : "No title available";
        String description = article.getDescription() != null ? article.getDescription() : "No description available";

        holder.titleTextView.setText(title);
        holder.descriptionTextView.setText(description);

        // Check if the image URL is available
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            holder.newsImageView.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(article.getUrlToImage())
                    .into(holder.newsImageView);
        } else {
            holder.newsImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(articles.size(), 4); // Limit to 4 items
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        Log.d("NewsAdapter", "Articles size: " + articles.size());
        notifyDataSetChanged();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImageView = itemView.findViewById(R.id.newsImage);
            titleTextView = itemView.findViewById(R.id.newsTitle);
            descriptionTextView = itemView.findViewById(R.id.newsDescription);
        }
    }
}
