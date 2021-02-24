package org.jumpmind.pos.core.ui.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class UICustomerDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String loyaltyNumber;
    private String email;
    private String phoneNumber;
    private UIAddress address;
}
