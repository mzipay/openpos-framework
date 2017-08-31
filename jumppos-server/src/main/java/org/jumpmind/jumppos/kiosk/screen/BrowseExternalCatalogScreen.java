package org.jumpmind.jumppos.kiosk.screen;

import org.jumpmind.jumppos.core.screen.EmbeddedWebPageScreen;

public class BrowseExternalCatalogScreen extends EmbeddedWebPageScreen {

    private static final long serialVersionUID = 1L;

    private Cart cart = new Cart();

    public BrowseExternalCatalogScreen() {
        super();
    }

    public BrowseExternalCatalogScreen(String url) {
        super(url);
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}
