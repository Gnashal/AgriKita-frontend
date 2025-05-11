package mobdev.agrikita.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mobdev.agrikita.models.order.Orders;
import mobdev.agrikita.R;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {

    private static final String ARG_STATUS = "status";
    private String status;
    private RecyclerView recyclerView;
    private mobdev.agrikita.adapters.OrderAdapter adapter;
    private List<Orders> allOrders;

    public static OrderListFragment newInstance(String status) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(ARG_STATUS);
        }
        allOrders = generateSampleOrders(); // Sample data
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new mobdev.agrikita.adapters.OrderAdapter(getContext(), filterOrdersByStatus(allOrders, status), getParentFragmentManager());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<Orders> filterOrdersByStatus(List<Orders> orders, String status) {
        if (status.equalsIgnoreCase("All")) return orders;
        List<Orders> filtered = new ArrayList<>();
        for (Orders order : orders) {
            if (order.getStatus().equalsIgnoreCase(status)) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    private List<Orders> generateSampleOrders() {
        List<Orders> list = new ArrayList<>();

        list.add(createOrder("ORD-2025-001", "2025-04-16", 0, "Delivered"));
        list.add(createOrder("ORD-2025-002", "2025-04-20", 0, "In Transit"));
        list.add(createOrder("ORD-2025-003", "2025-04-21", 0, "Processing"));
        list.add(createOrder("ORD-2025-004", "2025-04-22", 0, "Delivered"));
        list.add(createOrder("ORD-2025-005", "2025-04-23", 0, "In Transit"));

        return list;
    }

    private Orders createOrder(String id, String createdAt, int total, String status) {
        Orders order = new Orders(id, createdAt, total, status);

        // Use reflection or a custom subclass if the fields are private and no setters exist.
        try {
            java.lang.reflect.Field fId = Orders.class.getDeclaredField("orderIDid");
            java.lang.reflect.Field fCreatedAt = Orders.class.getDeclaredField("createdAt");
            java.lang.reflect.Field fTotal = Orders.class.getDeclaredField("total");
            java.lang.reflect.Field fStatus = Orders.class.getDeclaredField("status");

            fId.setAccessible(true);
            fCreatedAt.setAccessible(true);
            fTotal.setAccessible(true);
            fStatus.setAccessible(true);

            fId.set(order, id);
            fCreatedAt.set(order, createdAt);
            fTotal.set(order, total);
            fStatus.set(order, status);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

}
