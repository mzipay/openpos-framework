package org.jumpmind.pos.translate;

import java.util.List;

public interface ILegacyReasonBeanModel {

    void setSelectedReasonCode(int index);
    List<String> getReasonCodes();

}
