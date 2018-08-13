package org.jumpmind.pos.translate;

public interface ILegacyStatusBeanModel {
    String getScreenName();
    boolean isOnline();
    int checkThresholdStatus(String storeId); 
}
