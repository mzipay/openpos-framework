package org.jumpmind.jumppos.pos.screen;

import org.jumpmind.jumppos.core.screen.DefaultScreen;
import org.jumpmind.jumppos.core.screen.ScreenType;

public class PaymentStatusScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;
    
    public PaymentStatusScreen() {
        setType(ScreenType.PaymentStatus);
    }
}
