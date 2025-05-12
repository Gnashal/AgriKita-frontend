package mobdev.agrikita.adapters;
//HERE HERE HERE

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import mobdev.agrikita.R;
import java.util.List;
import mobdev.agrikita.pages.addons.BuyAgainDialog;

import mobdev.agrikita.models.order.Orders;
import mobdev.agrikita.pages.shop.OrderDetailsBottomSheet;
import mobdev.agrikita.utils.DateUtil;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Orders> orderList;
    private FragmentManager fragmentManager;
    private Context context;

    public OrderAdapter(Context context, List<Orders> orderList, FragmentManager fragmentManager) {
        this.context = context;
        this.orderList = orderList;
        this.fragmentManager = fragmentManager;
    }

    public void updateData(List<Orders> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Orders order = orderList.get(position);

        if (order == null) return; // Safeguard
        String readableDate = DateUtil.formatOrderDate(order.getOrderDate());
        holder.tvOrderId.setText(order.getOrderId());
        holder.tvOrderDate.setText("Ordered on " + readableDate);
        holder.tvItems.setText("Items (" + order.getItemCount() + ")");
        holder.tvDetails.setText(order.getItemDetails());
        holder.tvTotal.setText("₱" + order.getTotal());
        holder.tvStatus.setText(order.getStatus());

        switch (order.getStatus()) {
            case "Delivered":
                holder.tvStatus.setTextColor(Color.parseColor("#000000"));
                holder.imageView.setImageResource(R.drawable.delivered_check);
                holder.btnBuyAgain.setVisibility(View.VISIBLE);
                break;
            case "Pending":
                holder.tvStatus.setTextColor(Color.parseColor("#000000"));
                holder.imageView.setImageResource(R.drawable.box);
                holder.btnBuyAgain.setVisibility(View.GONE);
                break;
            default:
                holder.tvStatus.setTextColor(Color.parseColor("#000000"));
                holder.imageView.setImageResource(R.drawable.van);
                holder.btnBuyAgain.setVisibility(View.GONE);
                break;
        }

        holder.btnViewDetails.setOnClickListener(v -> {
            if (fragmentManager != null && order != null) {
                OrderDetailsBottomSheet bottomSheet = OrderDetailsBottomSheet.newInstance(order);
                bottomSheet.show(fragmentManager, "OrderDetailsBottomSheet");
            } else {
                Toast.makeText(context, "Error loading details", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnBuyAgain.setOnClickListener(v -> {
            if (fragmentManager != null && order != null) {
                BuyAgainDialog dialog = BuyAgainDialog.newInstance(order);
                dialog.show(fragmentManager, "BuyAgainDialog");
            } else {
                Toast.makeText(context, "Error: order data missing!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvItems, tvDetails, tvTotal, tvStatus;
        ImageView imageView;
        Button btnViewDetails, btnBuyAgain;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvItems = itemView.findViewById(R.id.tvItems);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imageView = itemView.findViewById(R.id.imageView);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnBuyAgain = itemView.findViewById(R.id.btnBuyAgain);
        }
    }
}
