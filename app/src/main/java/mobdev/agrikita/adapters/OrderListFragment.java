package mobdev.agrikita.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mobdev.agrikita.controllers.OrderService;
import mobdev.agrikita.models.user.CurrentUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import mobdev.agrikita.api.client.RetrofitClient;

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
        Context context = getContext();
        if (context == null) return; // or show error
        String buyerId = CurrentUser.getInstance(context).getUid();
        Log.v("OrderListFragment", "Calling getOrdersByBuyerID for buyer: " + buyerId);
        if (this.getContext() == null) {
            Toast.makeText(getContext(), "Context is null", Toast.LENGTH_SHORT).show();
            Log.v("OrderListFragment", "Context is null");
            return;
        };
        OrderService.getInstance(this.getContext()).getOrdersByBuyerID(buyerId, new OrderService.OrderCallback() {
            @Override
            public void onOrdersFetched(List<Orders> orders) {
                allOrders.clear();
                allOrders.addAll(orders);
                updateRecyclerView();
            }
            @Override
            public void onFailure(Exception e) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failed to fetch orders: ", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        );
    }

    private void updateRecyclerView() {
        if (adapter != null) {
            adapter.updateData(filterOrdersByStatus(allOrders, status));
        }
    }

}
