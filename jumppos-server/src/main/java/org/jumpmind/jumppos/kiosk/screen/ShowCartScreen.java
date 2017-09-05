package org.jumpmind.jumppos.kiosk.screen;

public class ShowCartScreen extends CartScreen {

    private static final long serialVersionUID = 1L;

    public ShowCartScreen(Cart cart) {
        super(cart);
        setType(SHOW_CART_SCREEN);
    }

}
