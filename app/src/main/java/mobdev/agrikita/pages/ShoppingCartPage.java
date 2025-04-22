package mobdev.agrikita.pages;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.ShoppingCartProductAdapter;
import mobdev.agrikita.models.Product;
import mobdev.agrikita.utils.ShoppingCartController;

public class ShoppingCartPage extends AppCompatActivity {

    ListView shoppingCartList;
    List<Product> productList;
    ShoppingCartProductAdapter adapter;
    MaterialButton shpc_togoCheckout, shpc_contshopping;
    TextView shpc_subtotal, shpc_shipping, shpc_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shoppingcart_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Other buttons
        shpc_togoCheckout = findViewById(R.id.shoppingcart_to_go_checkout);
        shpc_contshopping = findViewById(R.id.shoppingcart_continue_shopping);

        shpc_subtotal = findViewById(R.id.shoppingcart_subtotal_display);
        shpc_shipping = findViewById(R.id.shoppingcart_shipping_display);
        shpc_total = findViewById(R.id.shoppingcart_total_display);

        // This part is for the list view
        shoppingCartList = findViewById(R.id.shoppingcart_list);

        productList = ShoppingCartController.getInstance().getCartItems();
        adapter = new ShoppingCartProductAdapter(this, productList);

        shoppingCartList.setAdapter(adapter);
    }
}