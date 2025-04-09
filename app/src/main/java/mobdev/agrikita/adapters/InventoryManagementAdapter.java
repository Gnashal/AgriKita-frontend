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
import mobdev.agrikita.models.Products;

public class InventoryManagementAdapter extends RecyclerView.Adapter<InventoryManagementAdapter.ProductViewHolder> {
    private List<Products> productList;

    public InventoryManagementAdapter(List<Products> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_management_adapter, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productList.get(position);
        holder.textFarm.setText(product.getFarmName());
        holder.imageProduct.setImageResource(product.getImageResId());
        holder.textRating.setText(product.getRating());
        holder.textPrice.setText(product.getPrice());
        holder.textUnit.setText(product.getUnit());
        holder.textCategory.setText(product.getCategory());
        holder.textProductName.setText(product.getProductName());
        holder.textDescription.setText(product.getDescription());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textFarm, textRating, textPrice, textUnit, textCategory, textProductName, textDescription;
        ImageView imageProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textFarm = itemView.findViewById(R.id.text_farm);
            imageProduct = itemView.findViewById(R.id.image_product);
            textRating = itemView.findViewById(R.id.text_rating);
            textPrice = itemView.findViewById(R.id.text_price);
            textUnit = itemView.findViewById(R.id.text_unit);
            textCategory = itemView.findViewById(R.id.text_category);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textDescription = itemView.findViewById(R.id.text_product_desc);
        }
    }
}
