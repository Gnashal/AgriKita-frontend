package mobdev.agrikita.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.utils.ShoppingCartController;

public class ShoppingCartPage extends AppCompatActivity {

    ListView shoppingCartList;
    List<Products> productList;
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


        shpc_subtotal.setText("₱ "+String.format("%.2f", getSubTotalCost(productList)));
        shpc_shipping.setText("₱ "+String.format("%.2f", getShippingCost()));
        shpc_total.setText("₱ "+String.format("%.2f", getSubTotalCost(productList) + getShippingCost()));

        // Buttons Functionalities
        shpc_togoCheckout.setOnClickListener(v -> goToCheckout());
        shpc_contshopping.setOnClickListener(v -> goToMarketplace());
    }

    private void goToCheckout() {
        Toast.makeText(this, "Going Checkout", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(ShoppingCartPage.this, ));
    }

    private void goToMarketplace() {
        Toast.makeText(this, "Have Fun Shopping!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ShoppingCartPage.this, Marketplace.class));
    }

    private double getShippingCost() {
        return 100.00;
    }

    private double getSubTotalCost(List<Products> productList) {
        double totalHere = 0;
        
        // looping through list 
        for (Products item : productList) {
            totalHere += (item.getPrice() * item.getQuantityToBuy());
        }
        
        return totalHere;
    }
}