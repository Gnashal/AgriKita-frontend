package mobdev.agrikita.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.controllers.UserService;
import mobdev.agrikita.models.order.Orders;
import mobdev.agrikita.models.user.response.FetchUserByIDResponse;

public class CustomerOrdersAdapter extends RecyclerView.Adapter<CustomerOrdersAdapter.OrderViewHolder> implements Filterable  {
    private List<Orders> orderList;
    private List<Orders> orderListFull;
    String userName;
    Context context;
    UserService userService;

    public CustomerOrdersAdapter(Context context, List<Orders> orderList) {
        this.context = context;
        this.userService = new UserService(context);
        this.orderList = orderList;
        this.orderListFull = new ArrayList<>(orderList);
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
        String customerUid = orders.getBuyerID();

        String cleanedDate = cleanDate(orders.getCreatedAt());
        userService.fetchUserByID(customerUid, new UserService.FetchUserByIDCallback() {
            @Override
            public void onSuccess(FetchUserByIDResponse response) {
                if (response.getData() != null) {
                    holder.textCustomer.setText(response.getData().getName());
                } else {
                    holder.textCustomer.setText("Unknown");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                holder.textCustomer.setText("Error");
                Toast.makeText(holder.textCustomer.getContext(), "Failed to fetch user: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        holder.textOrderID.setText(orders.getOrderId());
        holder.textDate.setText(cleanedDate);
        holder.textQuantity.setText(String.valueOf(orders.getTotal()));
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

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Orders> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(orderListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Orders item : orderListFull) {
                    if (item.getOrderId().toLowerCase().contains(filterPattern) ||
                            item.getBuyerID().toLowerCase().contains(filterPattern)) {
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
            orderList.clear();
            orderList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateData(List<Orders> newOrderList) {
        orderList.clear();
        orderList.addAll(newOrderList);
        orderListFull.clear();
        orderListFull.addAll(newOrderList);
        notifyDataSetChanged();
    }

    private String cleanDate(String timestamp) {
        try {
            // Parse the timestamp to Date object
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date date = originalFormat.parse(timestamp);

            // Format the Date object to a cleaner format (e.g., "April 27, 2025, 10:25 PM")
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy, hh:mm a");
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return timestamp;  // In case of error, return the original timestamp
        }
    }
}
