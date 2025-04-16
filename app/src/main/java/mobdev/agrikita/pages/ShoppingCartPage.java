package mobdev.agrikita.pages;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.R;
import mobdev.agrikita.adapters.ShoppingCartProductAdapter;
import mobdev.agrikita.models.Product;
import mobdev.agrikita.utils.ShoppingCartController;

public class ShoppingCartPage extends AppCompatActivity {

    private ListView shoppingCartList;
    private List<Product> productList;
    private ShoppingCartProductAdapter adapter;

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

        shoppingCartList = findViewById(R.id.shoppingcart_list);

        productList = ShoppingCartController.getInstance().getCartItems();
        adapter = new ShoppingCartProductAdapter(this, productList);

        shoppingCartList.setAdapter(adapter);
    }
}