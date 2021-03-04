package org.jumpmind.pos.core.ui.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UILoyaltyReward implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Double amount;
    private Date expirationDate;
    private boolean canApply;
}
