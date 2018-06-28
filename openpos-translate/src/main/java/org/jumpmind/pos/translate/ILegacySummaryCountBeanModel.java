package org.jumpmind.pos.translate;

import java.math.BigDecimal;

public interface ILegacySummaryCountBeanModel {

    String getLabel();
    String getDisplayAmount();
    BigDecimal getAmount();
    String getActionName();
}
