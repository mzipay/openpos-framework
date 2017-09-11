package org.jumpmind.jumppos.kiosk.screen;

public class BrowseExternalCatalogScreen extends CartScreen {

    private static final long serialVersionUID = 1L;

    private String url;

    public BrowseExternalCatalogScreen(String url, Cart cart) {
        super(cart);
        setType(EMBEDDED_WEB_PAGE);
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
