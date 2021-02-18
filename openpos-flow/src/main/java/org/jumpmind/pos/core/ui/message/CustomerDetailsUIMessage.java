package org.jumpmind.pos.core.ui.message;

import org.jumpmind.pos.core.ui.ActionItem;
import org.jumpmind.pos.core.ui.UIMessage;

public class CustomerDetailsUIMessage extends UIMessage {
    private static final long serialVersionUID = 1L;

    private String title;

    private ActionItem loyaltyPromotions;
    private ActionItem unlinkButton;
    private ActionItem editButton;
    private ActionItem doneButton;

    private UICustomerDetails customer;

    public CustomerDetailsUIMessage() {
        setScreenType(UIMessageType.CUSTOMER_DETAILS_DIALOG);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public ActionItem getLoyaltyPromotions() {
        return loyaltyPromotions;
    }

    public void setLoyaltyPromotions(ActionItem loyaltyPromotions) {
        this.loyaltyPromotions = loyaltyPromotions;
    }

    public ActionItem getUnlinkButton() {
        return unlinkButton;
    }

    public void setUnlinkButton(ActionItem unlinkButton) {
        this.unlinkButton = unlinkButton;
    }

    public ActionItem getEditButton() {
        return editButton;
    }

    public void setEditButton(ActionItem editButton) {
        this.editButton = editButton;
    }

    public ActionItem getDoneButton() {
        return doneButton;
    }

    public void setDoneButton(ActionItem doneButton) {
        this.doneButton = doneButton;
    }

    public UICustomerDetails getCustomer() {
        return customer;
    }

    public void setCustomer(UICustomerDetails customer) {
        this.customer = customer;
    }
}
