package mobdev.agrikita.utils;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.models.products.Products;

public class ShoppingCartController {
    private static ShoppingCartController thisInstance;
    private List<Products> cart;

    private ShoppingCartController() {
        cart = new ArrayList<>();
    }

    public static synchronized ShoppingCartController getInstance()  {
        if (thisInstance == null) {
            thisInstance = new ShoppingCartController();
        }
        return thisInstance;
    }

    public void addToCart(Products item) {
        cart.add(item);
    }

    public List<Products> getCartItems() {
        return new ArrayList<>(cart);
    }

    public void clearTheCart() {
        cart.clear();
    }
}
