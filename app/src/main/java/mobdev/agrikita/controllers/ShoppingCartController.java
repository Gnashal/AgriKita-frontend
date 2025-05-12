package mobdev.agrikita.controllers;

import java.util.ArrayList;
import java.util.List;

import mobdev.agrikita.models.order.OrderItem;
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

    public List<OrderItem> convertAndClearCart() {
        List<OrderItem> orderItems = new ArrayList<>();

        // Convert each Products item to OrderItem
        for (Products product : cart) {
            OrderItem orderItem = new OrderItem(
                    product.getProductID(),
                    product.getShopID(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getImageUrl(),
                    product.getQuantityToBuy()
            );
            orderItems.add(orderItem);
        }

        clearTheCart();

        return orderItems;
    }
}
