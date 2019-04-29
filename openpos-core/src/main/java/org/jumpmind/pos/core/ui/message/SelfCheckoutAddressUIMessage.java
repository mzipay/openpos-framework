package org.jumpmind.pos.core.ui.message;

import java.util.List;

public class SelfCheckoutAddressUIMessage extends SelfCheckoutFormUIMessage {

    private static final long serialVersionUID = 1L;

    public SelfCheckoutAddressUIMessage() {
        super();
    }

    public void addDefaultAddressFields() {
        this.getForm().addTextField("streetAddress", "Street Address", "", true);
        this.getForm().addTextField("addressLine2", "Address Line 2", "", false);
        this.getForm().addTextField("locality", "City", "", true);
        this.getForm().addTextField("state", "State", "", true);
        this.getForm().addTextField("postalCode", "Postal Code", "", true);
        this.getForm().addTextField("country", "Country", "", true);
    }

    public void addAddressFieldsWithComboState(List<String> states) {
        this.getForm().addTextField("streetAddress", "Street Address", "", true);
        this.getForm().addTextField("addressLine2", "Address Line 2", "", false);
        this.getForm().addTextField("locality", "City", "", true);
        this.getForm().addComboBox("state", "State", states, true);
        this.getForm().addTextField("postalCode", "Postal Code", "", true);
        this.getForm().addTextField("country", "Country", "", true);
    }

}
