package mobdev.agrikita.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
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

    Button createProduct;

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

        createProduct = findViewById(R.id.addProductButton);

        SearchView searchViewProduct = findViewById(R.id.searchProductView);
        int searchTextId = searchViewProduct.getContext().getResources()
                .getIdentifier("search_src_text", "id", "android");
        int searchIconId = searchViewProduct.getContext().getResources()
                .getIdentifier("search_mag_icon", "id", "android");
        EditText searchEditTextProduct = searchViewProduct.findViewById(searchTextId);
        ImageView searchIconProduct = searchViewProduct.findViewById(searchIconId);
        if (searchEditTextProduct != null) {
            searchEditTextProduct.setTextColor(Color.BLACK);
            searchEditTextProduct.setHintTextColor(Color.GRAY);
        }
        if (searchIconProduct != null) {
            searchIconProduct.setColorFilter(Color.GRAY);
        }

        SearchView searchViewOrders = findViewById(R.id.searchOrderView);
        ImageView searchIconOrder = searchViewOrders.findViewById(searchIconId);
        EditText searchEditTextOrder = searchViewOrders.findViewById(searchTextId);
        if (searchEditTextOrder != null) {
            searchEditTextOrder.setTextColor(Color.BLACK);
            searchEditTextOrder.setHintTextColor(Color.GRAY);
        }
        if (searchIconOrder != null) {
            searchIconOrder.setColorFilter(Color.GRAY);
        }

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
        ordersList.add(new Orders("y5ty3wrgtfae3rrf4ew3q3rrf32w", "dsfe35tgs", "Kyerie", 2, "01-05-2025"));

        adapterOrders = new CustomerOrdersAdapter(ordersList);
        recyclerOrderView.setAdapter(adapterOrders);

        tabProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutProducts.getVisibility() != View.VISIBLE) {
                    layoutProducts.setVisibility(View.VISIBLE);
                    layoutOrders.setVisibility(View.GONE);
                }

                tabProducts.setSelected(true);
                tabOrders.setSelected(false);

                tabProducts.setBackgroundResource(R.drawable.tab_selector);
                tabOrders.setBackgroundResource(R.drawable.tab_selector);
            }
        });

        tabOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutOrders.getVisibility() != View.VISIBLE) {
                    layoutOrders.setVisibility(View.VISIBLE);
                    layoutProducts.setVisibility(View.GONE);
                }

                tabOrders.setSelected(true);
                tabProducts.setSelected(false);

                tabOrders.setBackgroundResource(R.drawable.tab_selector);
                tabProducts.setBackgroundResource(R.drawable.tab_selector);
            }
        });

        searchViewProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterProducts.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterProducts.getFilter().filter(newText);
                return false;
            }
        });

        searchViewOrders.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterOrders.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterOrders.getFilter().filter(newText);
                return false;
            }
        });

        createProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCreateProduct = new Intent(InventoryManagement.this, CreateProduct.class);
                startActivity(goToCreateProduct);
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