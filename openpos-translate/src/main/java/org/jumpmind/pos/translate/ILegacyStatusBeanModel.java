package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.model.POSSessionInfo;

public interface ILegacyStatusBeanModel {
    String getScreenName();
    boolean isOnline(ILegacyScreen screen);
    int checkThresholdStatus(POSSessionInfo posSessionInfo); 
}
