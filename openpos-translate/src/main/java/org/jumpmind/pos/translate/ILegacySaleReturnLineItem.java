package org.jumpmind.pos.translate;

import java.math.BigDecimal;
import java.util.List;


public interface ILegacySaleReturnLineItem {
    String getPosItemID();
    int getLineNumber();
    String getItemID();
    String getItemDescription();
    BigDecimal getItemQuantityDecimal();
    ILegacyCurrency getExtendedDiscountedSellingPrice();
    String getItemSizeCode();
    List<String> getUPCList();
}
