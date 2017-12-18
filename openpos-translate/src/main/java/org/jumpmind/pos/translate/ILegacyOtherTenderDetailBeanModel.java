package org.jumpmind.pos.translate;

public interface ILegacyOtherTenderDetailBeanModel {

    ILegacyCurrency[] getTenderAmounts();
    void setTenderAmounts(ILegacyCurrency[] tenderAmounts);
    
    ILegacyCurrency getTotal();
    void setTotal(ILegacyCurrency total);
    
}
