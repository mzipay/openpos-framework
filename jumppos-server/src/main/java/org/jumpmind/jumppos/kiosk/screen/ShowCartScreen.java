package org.jumpmind.jumppos.kiosk.screen;

import org.jumpmind.jumppos.core.screen.DefaultScreen;

public class ShowCartScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;

    private Cart cart;

    public ShowCartScreen() {
        this.setType(SELL_SCREEN_TYPE);
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
    
    public Cart getCart() {
        return cart;
    }

}
