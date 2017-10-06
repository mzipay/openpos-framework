package org.jumpmind.jumppos.kiosk.screen;

import org.jumpmind.jumppos.core.screen.ScreenType;

public class BrowseExternalCatalogScreen extends CartScreen {

    private static final long serialVersionUID = 1L;

    private String url;

    public BrowseExternalCatalogScreen(String url, Cart cart) {
        super(cart);
        setType(ScreenType.EmbeddedWebPage);
        setRefreshAlways(false);
        this.url = url;
        this.cart = cart;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
