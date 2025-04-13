package mobdev.agrikita.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.models.Orders;

public class CustomerOrdersAdapter extends RecyclerView.Adapter<CustomerOrdersAdapter.OrderViewHolder> {
    private List<Orders> orderList;

    public CustomerOrdersAdapter(List<Orders> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public CustomerOrdersAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_order_adapter, parent, false);
        return new CustomerOrdersAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrdersAdapter.OrderViewHolder holder, int position) {
        Orders orders = orderList.get(position);
        holder.textOrderID.setText(orders.getOrderID());
        holder.textCustomer.setText(orders.getBuyerID());
        holder.textDate.setText(orders.getCreatedAt());
        holder.textQuantity.setText(String.valueOf(orders.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderID, textCustomer, textDate, textQuantity;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderID = itemView.findViewById(R.id.orderID);
            textCustomer = itemView.findViewById(R.id.orderCustomer);
            textDate = itemView.findViewById(R.id.orderDate);
            textQuantity = itemView.findViewById(R.id.orderQuantity);
        }
    }
}
