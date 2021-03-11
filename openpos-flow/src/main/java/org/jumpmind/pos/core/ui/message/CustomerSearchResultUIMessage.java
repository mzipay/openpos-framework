package org.jumpmind.pos.core.ui.message;
import lombok.Data;
import java.util.List;
import org.jumpmind.pos.core.ui.ActionItem;
import org.jumpmind.pos.core.ui.UIMessage;

@Data
public class CustomerSearchResultUIMessage extends UIMessage {

    private static final long serialVersionUID = 1L;
    private List<UICustomerDetailsItem> results;
    private ActionItem selectButton;
    private ActionItem editButton;
    private String instructions;

    public CustomerSearchResultUIMessage(){
        this.setScreenType(UIMessageType.CUSTOMER_SEARCH_RESULT);
    }
}