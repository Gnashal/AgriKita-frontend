package mobdev.agrikita.pages.shop;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.CustomerOrdersAdapter;
import mobdev.agrikita.adapters.InventoryManagementAdapter;
import mobdev.agrikita.controllers.OrderService;
import mobdev.agrikita.controllers.ShopService;
import mobdev.agrikita.models.order.Orders;
import mobdev.agrikita.controllers.ProductService;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.models.shop.response.GetShopByShopIDResponse;
import mobdev.agrikita.models.user.CurrentUser;
import mobdev.agrikita.models.user.response.UserResponse;
import mobdev.agrikita.controllers.UserService;

public class InventoryManagement extends AppCompatActivity {
    private RecyclerView recyclerProductView;
    private RecyclerView recyclerOrderView;
    private InventoryManagementAdapter adapterProducts;
    private CustomerOrdersAdapter adapterOrders;
    private List<Products> productList;
    private List<Orders> ordersList;

    LinearLayout layoutProducts;
    LinearLayout layoutOrders;

    TextView tabProducts, shopName, shopDesc;
    ImageView shopImg;
    LinearLayout tabOrders;
    Button createProduct;
    ProductService productService;
    OrderService orderService;
    ShopService shopService;
    ProgressBar progressBarProds, progressBarShops;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory_management);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat insetsController = new WindowInsetsControllerCompat(window, decorView);
        insetsController.setAppearanceLightStatusBars(true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productService = new ProductService(this);
        orderService = new OrderService(this);
        shopService = new ShopService(this);

        layoutProducts = findViewById(R.id.containerProduct);
        layoutOrders = findViewById(R.id.containerOrder);

        tabProducts = findViewById(R.id.tabProducts);
        tabOrders = findViewById(R.id.tabOrders);

        recyclerProductView = findViewById(R.id.recycler_view_inventory);
        recyclerOrderView = findViewById(R.id.recycler_view_order);

        createProduct = findViewById(R.id.addProductButton);

        shopImg = findViewById(R.id.shopImage);
        shopName = findViewById(R.id.shopName);
        shopDesc = findViewById(R.id.shopDesc);

        progressBarProds = findViewById(R.id.progressBarProds);
        progressBarShops = findViewById(R.id.progressBarShops);


        progressBarProds.setVisibility(View.VISIBLE);
        progressBarShops.setVisibility(View.VISIBLE);

        SearchView searchViewProduct = findViewById(R.id.searchProductView);
        int searchTextId = searchViewProduct.getContext().getResources()
                .getIdentifier("search_src_text", "id", "android");
        int searchIconId = searchViewProduct.getContext().getResources()
                .getIdentifier("search_mag_icon", "id", "android");
        EditText searchEditTextProduct = searchViewProduct.findViewById(searchTextId);
        ImageView searchIconProduct = searchViewProduct.findViewById(searchIconId);
        if (searchEditTextProduct != null) {
            searchEditTextProduct.setTextColor(ContextCompat.getColor(this, R.color.black));
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
        adapterProducts = new InventoryManagementAdapter(productList);
        recyclerProductView.setAdapter(adapterProducts);

        ordersList = new ArrayList<>();
        adapterOrders = new CustomerOrdersAdapter(this, ordersList);
        recyclerOrderView.setAdapter(adapterOrders);

        CurrentUser user = CurrentUser.getInstance(this);
        user.fetchUserData(CurrentUser.getInstance(this).getUid(), new UserService.FetchUserCallback() {
            @Override
            public void onSuccess(UserResponse response) {
                String shopId = user.getShopId();
                fetchProducts(shopId);
                fetchOrders(shopId);
                fetchShopInfo(shopId);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(InventoryManagement.this, "Failed to load user: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

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

        adapterProducts.setOnItemClickListener(product -> {
            Intent intent = new Intent(InventoryManagement.this, ManageProducts.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
    }

    private void fetchOrders(String shopId) {
        orderService.getOrdersByShopID(shopId, new OrderService.OrderCallback() {
            @Override
            public void onOrdersFetched(List<Orders> orders) {
                adapterOrders.updateData(orders);
                progressBarShops.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(InventoryManagement.this, "Failed to fetch orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBarShops.setVisibility(View.GONE);
            }
        });
    }


    private void fetchProducts(String shopId) {
        productService.getProductsByShopID(shopId, new ProductService.ProductCallback() {
            @Override
            public void onProductsFetched(List<Products> products) {
                adapterProducts.updateData(products);
                progressBarProds.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(InventoryManagement.this, "Failed to fetch products: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBarProds.setVisibility(View.GONE);
            }
        });
    }

    private void fetchShopInfo(String shopId) {
        shopService.getShopById(shopId, new ShopService.ShopCallback() {
            @Override
            public void onSuccess(GetShopByShopIDResponse shop) {
                shopName.setText(shop.getName());
                shopDesc.setText(shop.getDescription());

                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.agrikita_logo)
                        .error(R.drawable.error_no_image)
                        .circleCrop();

                Glide.with(InventoryManagement.this)
                        .load(shop.getImageUrl())
                        .apply(requestOptions)
                        .into(shopImg);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(InventoryManagement.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}