package mobdev.agrikita.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.Product;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false);
        }

        Product productItem = productList.get(position);

        TextView name = convertView.findViewById(R.id.productcard_product_name);
        TextView description = convertView.findViewById(R.id.productcard_product_description);
        TextView category = convertView.findViewById(R.id.productcard_product_category);
        TextView price = convertView.findViewById(R.id.productcard_product_price);
        TextView rating = convertView.findViewById(R.id.productcard_product_rating);
        ImageView productImage = convertView.findViewById(R.id.productcard_product_image);

        // Set the data
        name.setText(productItem.getName());
        description.setText(productItem.getDescription());
        category.setText(productItem.getCategory());
        price.setText("Php " + productItem.getPrice());
        rating.setText(String.valueOf(productItem.getRating()));

        // Set a placeholder image or dynamic image here
        productImage.setImageResource(productItem.getImageResId());

        return convertView;
    }
}
