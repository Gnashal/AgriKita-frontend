package mobdev.agrikita.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import mobdev.agrikita.api.RetrofitClient;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mobdev.agrikita.api.OrderServiceApi;
import mobdev.agrikita.models.order.Orders;
import mobdev.agrikita.R;
import mobdev.agrikita.models.order.response.GetOrdersByBuyerIDResponse;

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
        allOrders = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new mobdev.agrikita.adapters.OrderAdapter(getContext(), filterOrdersByStatus(allOrders, status), getParentFragmentManager());
        recyclerView.setAdapter(adapter);

        fetchOrdersFromApi();
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

    private void fetchOrdersFromApi() {
        String buyerId = getContext()
                .getSharedPreferences("prefs", getContext().MODE_PRIVATE)
                .getString("buyer_id", ""); // Replace this with your actual buyer ID logic

        OrderServiceApi orderServiceApi = RetrofitClient.getClient(requireContext()).create(OrderServiceApi.class);
        Call<GetOrdersByBuyerIDResponse> call = orderServiceApi.getOrdersByBuyerID(buyerId);

        call.enqueue(new Callback<GetOrdersByBuyerIDResponse>() {
            @Override
            public void onResponse(Call<GetOrdersByBuyerIDResponse> call, Response<GetOrdersByBuyerIDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!isAdded()) return;
                    allOrders = response.body().getData();
                    updateRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<GetOrdersByBuyerIDResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateRecyclerView() {
        if (adapter != null) {
            adapter.updateData(filterOrdersByStatus(allOrders, status));
        }
    }




//    private List<Orders> generateSampleOrders() {
//        List<Orders> list = new ArrayList<>();
//
//        list.add(createOrder("ORD-2025-001", "2025-04-16", 0, "Delivered"));
//        list.add(createOrder("ORD-2025-002", "2025-04-20", 0, "In Transit"));
//        list.add(createOrder("ORD-2025-003", "2025-04-21", 0, "Processing"));
//        list.add(createOrder("ORD-2025-004", "2025-04-22", 0, "Delivered"));
//        list.add(createOrder("ORD-2025-005", "2025-04-23", 0, "In Transit"));
//
//        return list;
//    }
//
//    private Orders createOrder(String id, String createdAt, int total, String status) {
//        Orders order = new Orders(id, createdAt, total, status);
//
//        // Use reflection or a custom subclass if the fields are private and no setters exist.
//        try {
//            java.lang.reflect.Field fId = Orders.class.getDeclaredField("orderIDid");
//            java.lang.reflect.Field fCreatedAt = Orders.class.getDeclaredField("createdAt");
//            java.lang.reflect.Field fTotal = Orders.class.getDeclaredField("total");
//            java.lang.reflect.Field fStatus = Orders.class.getDeclaredField("status");
//
//            fId.setAccessible(true);
//            fCreatedAt.setAccessible(true);
//            fTotal.setAccessible(true);
//            fStatus.setAccessible(true);
//
//            fId.set(order, id);
//            fCreatedAt.set(order, createdAt);
//            fTotal.set(order, total);
//            fStatus.set(order, status);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return order;
//    }

}
