package mobdev.agrikita.controllers;

import java.util.ArrayList;
import java.util.List;

public class CheckoutController {

    public static class Item {
        public String name;
        public int quantity;
        public int price;

        public Item(String name, int quantity, int price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
    }

    private List<Item> purchasedItems;
    private int shippingFee;

    public CheckoutController() {
        purchasedItems = new ArrayList<>();
        shippingFee = 150; 
    } 

    public void addItem(String name, int quantity, int price) {
        purchasedItems.add(new Item(name, quantity, price));
    }

    public List<Item> getPurchasedItems() {
        return purchasedItems;
    }

    public int calculateSubtotal() {
        int subtotal = 0;
        for (Item item : purchasedItems) {
            subtotal += item.price * item.quantity;
        }
        return subtotal;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public int calculateTotal() {
        return calculateSubtotal() + shippingFee;
    }
}