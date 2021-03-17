package org.jumpmind.pos.core.ui.message;

import lombok.Data;
import org.jumpmind.pos.core.ui.ActionItem;
import org.jumpmind.pos.core.ui.UIMessage;

@Data
public class CustomerDetailsUIMessage extends UIMessage {
    private static final long serialVersionUID = 1L;

    private String title;

    private ActionItem unlinkButton;
    private ActionItem editButton;
    private ActionItem doneButton;

    private UICustomerDetailsItem customer;

    private Boolean membershipEnabled;
    private String membershipLabel;
    private String contactLabel;
    private String noPromotionsText;
    private String rewardsLabel;
    private String expiresLabel;
    private String rewardHistoryLabel;
    private String expiredLabel;
    private String redeemedLabel;
    private String noMembershipsFoundLabel;

    public CustomerDetailsUIMessage() {
        setScreenType(UIMessageType.CUSTOMER_DETAILS_DIALOG);
    }
}
