package org.jumpmind.pos.core.ui.message;

import lombok.Data;
import org.jumpmind.pos.core.ui.ActionItem;
import org.jumpmind.pos.core.ui.UIMessage;

@Data
public class CustomerDetailsUIMessage extends UIMessage {
    private static final long serialVersionUID = 1L;

    private String title;

    private ActionItem loyaltyPromotions;
    private ActionItem unlinkButton;
    private ActionItem editButton;
    private ActionItem doneButton;

    private UICustomerDetails customer;

    private Boolean membershipEnabled;
    private String membershipLabel;

    public CustomerDetailsUIMessage() {
        setScreenType(UIMessageType.CUSTOMER_DETAILS_DIALOG);
    }
}
