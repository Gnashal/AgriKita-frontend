package mobdev.agrikita.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.CustomerOrdersAdapter;
import mobdev.agrikita.adapters.InventoryManagementAdapter;
import mobdev.agrikita.models.Orders;
import mobdev.agrikita.models.Products;

public class InventoryManagement extends AppCompatActivity {
    private RecyclerView recyclerProductView;
    private RecyclerView recyclerOrderView;
    private InventoryManagementAdapter adapterProducts;
    private CustomerOrdersAdapter adapterOrders;
    private List<Products> productList;
    private List<Orders> ordersList;

    LinearLayout layoutProducts;
    LinearLayout layoutOrders;

    TextView tabProducts;
    LinearLayout tabOrders;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupNavbar();

        layoutProducts = findViewById(R.id.containerProduct);
        layoutOrders = findViewById(R.id.containerOrder);

        tabProducts = findViewById(R.id.tabProducts);
        tabOrders = findViewById(R.id.tabOrders);

        recyclerProductView = findViewById(R.id.recycler_view_inventory);
        recyclerOrderView = findViewById(R.id.recycler_view_order);

        recyclerProductView.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrderView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productList.add(new Products("⭐ 4.8", 26, "Per kilo", "Produce", "Fresh Tomatoes", "Locally sourced, high-quality fresh tomatoes, hand-picked.", "Available", "dwo24ndw ", 100, "A+", "http://ThisisImage", "04, 12, 2025"));
        productList.add(new Products( "⭐ 4.6", 40, "Per kilo", "Produce", "Fresh Potatoes", "High quality, organically grown potatoes.", "Available", "dwo24ndw ", 100, "A+", "http://ThisisImage", "07, 26, 2025"));

        adapterProducts = new InventoryManagementAdapter(productList);
        recyclerProductView.setAdapter(adapterProducts);

        ordersList = new ArrayList<>();
        ordersList.add(new Orders("sdqni231f", "0913djica", "Callen", 100, "09-30-2025"));
        ordersList.add(new Orders("fsf3254fw", "45edy7wwf", "Kyerie", 122, "05-10-2025"));
        ordersList.add(new Orders("y5ty3wrgt", "dsfe35tgs", "Kyerie", 2, "01-05-2025"));

        adapterOrders = new CustomerOrdersAdapter(ordersList);
        recyclerOrderView.setAdapter(adapterOrders);

        tabProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutProducts.setVisibility(View.VISIBLE);
                layoutOrders.setVisibility(View.GONE);

                tabProducts.setSelected(true);
                tabOrders.setSelected(false);

                tabProducts.setBackgroundResource(R.drawable.tab_selector);
                tabOrders.setBackgroundResource(R.drawable.tab_selector);
            }
        });

        tabOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutProducts.setVisibility(View.GONE);
                layoutOrders.setVisibility(View.VISIBLE);

                tabOrders.setSelected(true);
                tabProducts.setSelected(false);

                tabOrders.setBackgroundResource(R.drawable.tab_selector);
                tabProducts.setBackgroundResource(R.drawable.tab_selector);
            }
        });
    }

    private void setupNavbar() {
        Navbar navbarFragment = new Navbar();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.navbarContainer, navbarFragment);
        transaction.commit();
    }
}