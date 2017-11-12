package org.jumpmind.jumppos.kiosk.screen;

import org.jumpmind.jumppos.core.screen.ScreenType;

public class ShowCartScreen extends CartScreen {

    private static final long serialVersionUID = 1L;

    public ShowCartScreen(Cart cart) {
        super(cart);
        setType(ScreenType.Cart);
    }

}
