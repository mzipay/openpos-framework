package org.jumpmind.pos.pos.screen;

import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.ScreenType;

public class PaymentStatusScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;
    
    public PaymentStatusScreen() {
        setType(ScreenType.PaymentStatus);
    }
}
