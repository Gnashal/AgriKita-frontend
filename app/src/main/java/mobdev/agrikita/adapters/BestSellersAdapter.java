package mobdev.agrikita.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.ShopService;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.shop.GetShopByShopIDResponse;
import mobdev.agrikita.pages.ProductDetailPage;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_best_sellers_adapter, parent, false);
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

        holder.freshLabel.setVisibility(View.GONE);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.agrikita_logo)
                .error(R.drawable.error_no_image)
                .centerCrop();

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .apply(requestOptions)
                .into(holder.imageProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productRating, freshLabel, shopName, productUnit;
        ImageView imageProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textTitle);
            shopName = itemView.findViewById(R.id.textFarm);
            productPrice = itemView.findViewById(R.id.textPrice);
            productUnit = itemView.findViewById(R.id.textUnit);
            productRating = itemView.findViewById(R.id.rating);
            freshLabel = itemView.findViewById(R.id.freshLabel);
            imageProduct = itemView.findViewById(R.id.imageProduct);
        }
    }
}