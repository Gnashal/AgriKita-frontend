package mobdev.agrikita.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.products.Products;

public class InventoryManagementAdapter extends RecyclerView.Adapter<InventoryManagementAdapter.ProductViewHolder> implements Filterable {
    private List<Products> productList;
    private List<Products> productListFull;

    public InventoryManagementAdapter(List<Products> productList) {
        this.productList = productList;
        this.productListFull = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_management_adapter, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productList.get(position);
        holder.textProductName.setText(product.getProductName());
        holder.textPrice.setText(String.format("%.2f", product.getPrice()));
        holder.textProductStock.setText(String.valueOf(product.getStockQuantity()));

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.agrikita_logo)
                .error(R.drawable.error_no_image)
                .circleCrop();

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
        TextView textProductName, textPrice, textProductStock;
        ImageView imageProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.productName);
            textPrice = itemView.findViewById(R.id.productPrice);
            textProductStock = itemView.findViewById(R.id.productStock);
            imageProduct = itemView.findViewById(R.id.productImage);
        }
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Products> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Products item : productListFull) {
                    if (item.getProductName().toLowerCase().contains(filterPattern) ||
                            item.getCategory().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((List<Products>) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateData(List<Products> newProductList) {
        productList.clear();
        productList.addAll(newProductList);
        productListFull.clear();
        productListFull.addAll(newProductList);
        notifyDataSetChanged();
    }
}