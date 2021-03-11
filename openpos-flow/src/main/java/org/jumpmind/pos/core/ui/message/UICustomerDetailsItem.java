package org.jumpmind.pos.core.ui.message;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UICustomerDetailsItem extends SelectableItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String loyaltyNumber;
    private String email;
    private String phoneNumber;
    private UIAddress address;
    private List<UIMembership> memberships = new ArrayList<>();
}
