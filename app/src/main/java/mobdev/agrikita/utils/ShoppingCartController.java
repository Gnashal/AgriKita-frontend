package mobdev.agrikita.utils;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.models.Product;

public class ShoppingCartController {
    private static ShoppingCartController thisInstance;
    private List<Product> cart;

    private ShoppingCartController() {
        cart = new ArrayList<>();
    }

    public static synchronized ShoppingCartController getInstance()  {
        if (thisInstance == null) {
            thisInstance = new ShoppingCartController();
        }
        return thisInstance;
    }

    public void addToCart(Product item) {
        cart.add(item);
    }

    public List<Product> getCartItems() {
        return new ArrayList<>(cart);
    }

    public void clearTheCart() {
        cart.clear();
    }
}
