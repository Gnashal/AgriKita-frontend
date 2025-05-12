package mobdev.agrikita.pages.addons.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.ShoppingCartProductAdapter;
import mobdev.agrikita.models.products.Products;
import mobdev.agrikita.controllers.ShoppingCartController;
import mobdev.agrikita.pages.index.Home;
import mobdev.agrikita.pages.marketplace.Marketplace;

public class ShoppingCartPage extends AppCompatActivity
        implements ShoppingCartProductAdapter.CartUpdateListener{
    private ListView shoppingCartList;
    private List<Products> productList;
    private ShoppingCartProductAdapter adapter;
    private MaterialButton shpc_togoCheckout, shpc_contshopping;
    private TextView shpc_subtotal, shpc_shipping, shpc_total;
    private ImageView back_btn;

    private double subtotal, shipping, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shoppingcart_page);
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

        // Other buttons
        shpc_togoCheckout = findViewById(R.id.shoppingcart_to_go_checkout);
        shpc_contshopping = findViewById(R.id.shoppingcart_continue_shopping);

        shpc_subtotal = findViewById(R.id.shoppingcart_subtotal_display);
        shpc_shipping = findViewById(R.id.shoppingcart_shipping_display);
        shpc_total = findViewById(R.id.shoppingcart_total_display);
        back_btn = findViewById(R.id.back_btn);
        // This part is for the list view
        shoppingCartList = findViewById(R.id.shoppingcart_list);

        productList = ShoppingCartController.getInstance().getCartItems();
        adapter = new ShoppingCartProductAdapter(this, productList, this);

        shoppingCartList.setAdapter(adapter);

        subtotal = getSubTotalCost(productList);
        shipping = getShippingCost(subtotal);
        total = subtotal + shipping;
        shpc_subtotal.setText("₱ "+String.format("%.2f", subtotal));
        shpc_shipping.setText("₱ "+String.format("%.2f", shipping));
        shpc_total.setText("₱ "+String.format("%.2f", total));

        // Buttons Functionalities
        back_btn.setOnClickListener(v -> startActivity(new Intent(this, Home.class)));
        shpc_togoCheckout.setOnClickListener(v -> {
            if (productList.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                goToCheckout();
            }
        });
        shpc_contshopping.setOnClickListener(v -> goToMarketplace());
    }

    private void goToCheckout() {
        Intent intent = new Intent(this, Checkout.class);
        intent.putExtra("subtotal", subtotal);
        intent.putExtra("shipping", shipping);
        intent.putExtra("total", total);
        startActivity(intent);
    }

    private void goToMarketplace() {
        Toast.makeText(this, "Have Fun Shopping!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ShoppingCartPage.this, Marketplace.class));
    }

    private double getShippingCost(double subTotal){
        if (subTotal > 100) {
            return subTotal * 0.07;
        } else return 0;
    }

    private double getSubTotalCost(List<Products> productList) {
        double totalHere = 0;
        
        // looping through list 
        for (Products item : productList) {
            totalHere += (item.getPrice() * item.getQuantityToBuy());
        }
        
        return totalHere;
    }
    private void updateCartTotals() {
        subtotal = getSubTotalCost(productList);
        shipping = getShippingCost(subtotal);
        total = subtotal + shipping;

        shpc_subtotal.setText("₱ "+String.format("%.2f", subtotal));
        shpc_shipping.setText("₱ "+String.format("%.2f", shipping));
        shpc_total.setText("₱ "+String.format("%.2f", total));
    }

    @Override
    public void onQuantityChanged() {
        updateCartTotals();
    }

    @Override
    public void onItemRemoved(int position) {
        updateCartTotals();
        if (productList.isEmpty()) {
            Toast.makeText(this, "Your cart is now empty", Toast.LENGTH_SHORT).show();
        }
    }

}