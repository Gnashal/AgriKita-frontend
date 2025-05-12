package mobdev.agrikita.pages.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import  mobdev.agrikita.models.order.Orders;
import  mobdev.agrikita.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;

public class OrderDetailsBottomSheet extends DialogFragment {

    private static final String ARG_ORDER = "arg_order";
    private Orders order;

    /** Required empty public constructor */
    public OrderDetailsBottomSheet() {}

    /** Factory to create a new instance, passing the Order via Bundle */
    public static OrderDetailsBottomSheet newInstance(Orders order) {
        OrderDetailsBottomSheet bs = new OrderDetailsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, (Serializable) order);
        bs.setArguments(args);
        return bs;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the Order from arguments
        if (getArguments() != null) {
            order = (Orders) getArguments().getSerializable(ARG_ORDER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_order_details, container, false);

        // Bind views
        TextView tvOrderId    = view.findViewById(R.id.tvOrderId);
        TextView tvOrderDate  = view.findViewById(R.id.tvOrderDate);
        TextView tvStatus     = view.findViewById(R.id.tvStatus);
        LinearLayout itemsContainer = view.findViewById(R.id.itemsContainer);
        TextView tvTotal      = view.findViewById(R.id.tvTotal);

        // Populate header fields
        tvOrderId.setText(order.getOrderId());
        tvOrderDate.setText("Ordered on " + order.getOrderDate());
        tvStatus.setText(order.getStatus());

        // Handle empty item details
        String itemDetails = order.getItemDetails();
        if (itemDetails == null || itemDetails.trim().isEmpty()) {
            // No items
            tvTotal.setText("₱0");

            TextView emptyText = new TextView(requireContext());
            emptyText.setText("No items in this order.");
            emptyText.setTextSize(16f);
            itemsContainer.addView(emptyText);
        } else {
            // Add item details
            TextView tvDetails = new TextView(requireContext());
            tvDetails.setText(itemDetails);
            tvDetails.setTextSize(16f);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            lp.topMargin = (int) (8 * getResources().getDisplayMetrics().density);
            tvDetails.setLayoutParams(lp);
            itemsContainer.addView(tvDetails);

            if (order == null) {
                throw new IllegalStateException("Order is null!");
            }

            // Set actual total
            if (order.getItems() == null || order.getItems().isEmpty()) {
                tvTotal.setText("₱0");
            } else {
                int total = 0;
                for (Orders.OrderItem item : order.getItems()) {
                    total += item.getPrice() * item.getQuantity();
                    // add display code here if needed
                }
                tvTotal.setText("₱" + total);
            }

        }

        return view;
    }
}
