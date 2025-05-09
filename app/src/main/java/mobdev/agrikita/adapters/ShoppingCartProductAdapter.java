package mobdev.agrikita.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.products.Products;

public class ShoppingCartProductAdapter extends BaseAdapter {
    private Context context;
    private List<Products> productList;

    public ShoppingCartProductAdapter(Context context, List<Products> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Products getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        LinearLayout remove_btn;
        ImageView coutprod_imgv;
        TextView coutprod_name, coutprod_price, coutprod_seller, coutprod_perprice, coutprod_quantity;
        MaterialButton add_btn, sub_btn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shoppingcart_product_card, parent, false);

            holder = new ViewHolder();

            holder.coutprod_imgv = convertView.findViewById(R.id.sc_prodimg_display);
            holder.coutprod_name = convertView.findViewById(R.id.sc_prodname_display);
            holder.coutprod_price = convertView.findViewById(R.id.sc_prodcost_display);
            holder.coutprod_seller = convertView.findViewById(R.id.sc_prodsellername_display);
            holder.coutprod_perprice = convertView.findViewById(R.id.sc_prodper_display);
            holder.coutprod_quantity = convertView.findViewById(R.id.sc_prodquantity_display);
            holder.add_btn = convertView.findViewById(R.id.sc_prodadd_btn);
            holder.sub_btn = convertView.findViewById(R.id.sc_prodsub_btn);
            holder.remove_btn = convertView.findViewById(R.id.sc_prodremove_btn);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Products singleProd = getItem(position);

        holder.coutprod_name.setText(singleProd.getProductName());
        holder.coutprod_price.setText("₱ "+String.format("%.2f", singleProd.getPrice() * singleProd.getQuantityToBuy()));
        holder.coutprod_perprice.setText("₱ "+String.format("%.2f", singleProd.getPrice()));
        holder.coutprod_seller.setText(singleProd.getShopID());
        holder.coutprod_quantity.setText(String.valueOf(singleProd.getQuantityToBuy()));

        String imageURL = singleProd.getImageUrl();

        Glide.with(context)
                .load(imageURL)
                .placeholder(R.drawable.agrikita_logo)
                .error(R.drawable.agrikita_logo)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(holder.coutprod_imgv);

        holder.add_btn.setOnClickListener(v -> {
            singleProd.setQuantityToBuy(singleProd.getQuantityToBuy() + 1);
            notifyDataSetChanged();
        });

        holder.sub_btn.setOnClickListener(v -> {
            if (singleProd.getQuantityToBuy() > 1) {
                singleProd.setQuantityToBuy(singleProd.getQuantityToBuy() - 1);
                notifyDataSetChanged();
            }
        });

        holder.remove_btn.setOnClickListener(v -> {
            productList.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
