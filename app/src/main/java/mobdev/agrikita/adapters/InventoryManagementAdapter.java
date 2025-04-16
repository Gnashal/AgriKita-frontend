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
        holder.textProductName.setText(product.getProductName());
        holder.textPrice.setText(String.format("%.2f", product.getPrice()));
        holder.textProductStock.setText(String.valueOf(product.getQuantity()));
//        holder.imageProduct.setImageResource(product.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName, textPrice, textProductStock;
        ImageView imageProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.productName);
            textPrice = itemView.findViewById(R.id.productPrice);
            textProductStock = itemView.findViewById(R.id.productStock);
//            imageProduct = itemView.findViewById(R.id.productImage);
        }
    }
}