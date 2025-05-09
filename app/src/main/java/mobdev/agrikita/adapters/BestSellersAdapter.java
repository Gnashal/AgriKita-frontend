package mobdev.agrikita.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.ShopService;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.shop.response.GetShopByShopIDResponse;

public class BestSellersAdapter extends RecyclerView.Adapter<BestSellersAdapter.ProductViewHolder> {
    private final List<Products> productList;
    ShopService shopService;

    public BestSellersAdapter(Context context, List<Products> productList) {
        this.productList = productList;
        this.shopService = new ShopService(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_sellers_adapter, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productList.get(position);

        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(String.format("₱%.2f", product.getPrice()));
        holder.productUnit.setText(product.getMeasuringUnit());
        holder.productRating.setText(String.valueOf(product.getRating()));
        holder.freshLabel.setText(product.getFreshnessRate());
        holder.container.setBackgroundResource(getFreshnessBackground(product.getFreshnessRate()));

        shopService.getShopById(product.getShopID(), new ShopService.ShopCallback() {
            @Override
            public void onSuccess(GetShopByShopIDResponse shop) {
                holder.shopName.setText(shop.getName());
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onError(String errorMessage) {
                holder.shopName.setText("Unknown");
            }
        });


        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.agrikita_logo)
                .error(R.drawable.error_no_image)
                .centerCrop();

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .apply(requestOptions)
                .into(holder.imageProduct);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onItemClick(productList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productRating, freshLabel, shopName, productUnit;
        ImageView imageProduct;
        LinearLayout container;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.textTitle);
            shopName = itemView.findViewById(R.id.textFarm);
            productPrice = itemView.findViewById(R.id.textPrice);
            productUnit = itemView.findViewById(R.id.textUnit);
            productRating = itemView.findViewById(R.id.rating);
            freshLabel = itemView.findViewById(R.id.freshLabel);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            container = itemView.findViewById(R.id.freshLabelContainer);
        }
    }

    private int getFreshnessBackground(String freshness) {
        if (freshness == null) return R.drawable.round_gray_background_latest;

        String normalized = freshness.trim().toLowerCase();

        switch (normalized) {
            case "harvested today":
            case "fresh from the farm":
            case "1–2 days old":
            case "fresh":
                return R.drawable.round_green_background_latest;

            case "moderate":
            case "overripe":
                return R.drawable.round_yellow_background_latest;

            case "stale":
                return R.drawable.round_orange_background_latest;

            case "rotten":
                return R.drawable.round_dark_red_background_latest;

            default:
                return R.drawable.round_gray_background_latest;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Products product);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}