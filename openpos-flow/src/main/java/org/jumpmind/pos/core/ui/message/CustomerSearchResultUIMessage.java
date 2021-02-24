package org.jumpmind.pos.core.ui.message;

public class CustomerSearchResultUIMessage extends SelectionListUIMessage {

    private static final long serialVersionUID = 1L;
    public CustomerSearchResultUIMessage(){
        this.setScreenType(UIMessageType.CUSTOMER_SEARCH_RESULT);
    }
}