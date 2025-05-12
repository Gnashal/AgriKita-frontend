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
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.ShopService;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.shop.Shop;

public class FeaturedFarmsAdapter extends RecyclerView.Adapter<FeaturedFarmsAdapter.ShopViewHolder> {
    private final List<Shop> shopList;
    ShopService shopService;
    private FeaturedFarmsAdapter.OnItemClickListener listener;

    public FeaturedFarmsAdapter(Context context, List<Shop> shopList) {
        this.shopList = shopList;
        this.shopService = new ShopService(context);
    }

    @NonNull
    @Override
    public FeaturedFarmsAdapter.ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_farms_adapter, parent, false);
        return new FeaturedFarmsAdapter.ShopViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop = shopList.get(position);

        holder.shopName.setText(shop.getName());
        holder.shopLocation.setText(shop.getAddr());
        holder.shopRating.setText("0");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.agrikita_logo)
                .error(R.drawable.error_no_image)
                .centerCrop();

        Glide.with(holder.itemView.getContext())
                .load(shop.getShopImgUrl())
                .apply(requestOptions)
                .into(holder.imageShop);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onItemClick(shopList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }


    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopLocation, shopRating;
        ImageView imageShop;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.textTitle);
            shopLocation = itemView.findViewById(R.id.textFarmLocation);
            shopRating = itemView.findViewById(R.id.rating);
            imageShop = itemView.findViewById(R.id.imageShop);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Shop product);
    }

    public void setOnItemClickListener(FeaturedFarmsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}