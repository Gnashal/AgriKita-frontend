package mobdev.agrikita.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.NewsItem;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsItems;

    public NewsAdapter(List<NewsItem> newsItems) {
        this.newsItems = newsItems;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = newsItems.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        // Load image using Glide/Picasso if needed
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            description = itemView.findViewById(R.id.newsDescription);
            image = itemView.findViewById(R.id.newsImage);
        }
    }
}
