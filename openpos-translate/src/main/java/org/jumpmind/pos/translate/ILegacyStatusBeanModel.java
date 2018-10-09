package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.model.POSSessionInfo;
import org.jumpmind.pos.core.model.SystemStatus;

public interface ILegacyStatusBeanModel {
    String getScreenName();
    boolean isOnline();
    SystemStatus getSystemStatus();
    int checkThresholdStatus(POSSessionInfo posSessionInfo); 
}
