package mobdev.agrikita.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.Product;
import mobdev.agrikita.pages.ShoppingCartPage;
import mobdev.agrikita.utils.ShoppingCartController;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

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
    public int getItemCount() {
        return productList.size();
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

        holder.addToCart_btn.setOnClickListener(v -> {
            ShoppingCartController.getInstance().addToCart(productItem);
            Toast.makeText(context, "Product has been Added to the Cart!", Toast.LENGTH_SHORT).show();
        });

        holder.buyNow_btn.setOnClickListener(v -> {
            ShoppingCartController.getInstance().addToCart(productItem);
            context.startActivity(new Intent(context, ShoppingCartPage.class));
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(productItem);
            }
        });
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, category, price, rating;
        ImageView productImage;
        MaterialButton addToCart_btn, buyNow_btn;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productcard_product_name);
            description = itemView.findViewById(R.id.productcard_product_description);
            category = itemView.findViewById(R.id.productcard_product_category);
            price = itemView.findViewById(R.id.productcard_product_price);
            rating = itemView.findViewById(R.id.productcard_product_rating);
            productImage = itemView.findViewById(R.id.productcard_product_image);
            addToCart_btn = itemView.findViewById(R.id.productcard_addtocart);
            buyNow_btn = itemView.findViewById(R.id.productcard_buynow);
        }
    }
}
