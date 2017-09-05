package org.jumpmind.jumppos.kiosk.screen;

import org.jumpmind.jumppos.core.screen.DefaultScreen;

public class CartScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;

    protected Cart cart = new Cart();

    public CartScreen() {
    }

    public CartScreen(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
