package mobdev.agrikita.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product productItem = productList.get(position);

        holder.name.setText(productItem.getName());
        holder.description.setText(productItem.getDescription());
        holder.category.setText(productItem.getCategory());
        holder.price.setText("P " + productItem.getPrice());
        holder.rating.setText(String.valueOf(productItem.getRating()));
        holder.productImage.setImageResource(productItem.getImageResId());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, category, price, rating;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productcard_product_name);
            description = itemView.findViewById(R.id.productcard_product_description);
            category = itemView.findViewById(R.id.productcard_product_category);
            price = itemView.findViewById(R.id.productcard_product_price);
            rating = itemView.findViewById(R.id.productcard_product_rating);
            productImage = itemView.findViewById(R.id.productcard_product_image);
        }
    }
}
