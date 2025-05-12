package mobdev.agrikita.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.ShopService;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.shop.response.GetShopByShopIDResponse;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Products> productList;
    private OnItemClickListener listener;
    ShopService shopService;

    public interface OnItemClickListener {
        void onItemClick(Products product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(Context context, List<Products> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void updateList(List<Products> newList) {
        this.productList = newList;
        notifyDataSetChanged();
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
        Products productItem = productList.get(position);

        holder.productName.setText(productItem.getProductName());
        holder.category.setText(productItem.getCategory());
        holder.price.setText("₱ "+String.format("%.2f", productItem.getPrice()));
        holder.rating.setText(String.valueOf(productItem.getRating()));

        shopService = new ShopService(context);

        shopService.getShopById(productItem.getShopID(), new ShopService.ShopCallback() {
            @Override
            public void onSuccess(GetShopByShopIDResponse shop) {
                holder.seller_name.setText(shop.getName());
            }

            @Override
            public void onError(String errorMessage) {
                holder.seller_name.setText("Fail to fetch");
            }
        });

        holder.seller_name.setText(productItem.getShopID());

        String imgaeURL = productItem.getImageUrl();

        Glide.with(context)
                .load(imgaeURL)
                .placeholder(R.drawable.agrikita_logo)
                .error(R.drawable.agrikita_logo)
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(productItem);
            }
        });
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView category, price, rating, seller_name, productName;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.productcard_product_category);
            price = itemView.findViewById(R.id.productcard_product_price);
            rating = itemView.findViewById(R.id.productcard_product_rating);
            seller_name = itemView.findViewById(R.id.productcard_seller_name);
            productImage = itemView.findViewById(R.id.productcard_product_image);
            productName = itemView.findViewById(R.id.pdp_product_name);
        }
    }
}
