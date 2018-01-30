package org.jumpmind.pos.translate;

import java.math.BigDecimal;

public interface ILegacyGiftCardBeanModel {
    String getGiftCardNumber();
    BigDecimal getGiftCardAmount();
    public String getGiftCardAmountLabel();
}
