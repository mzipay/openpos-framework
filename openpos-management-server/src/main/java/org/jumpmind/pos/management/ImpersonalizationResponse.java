package org.jumpmind.pos.management;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImpersonalizationResponse {
    boolean openposManagementServer = true;
    
    @Autowired
    OpenposManagementServerConfig config;

    public boolean isOpenposManagementServer() {
        return openposManagementServer;
    }

    public void setOpenposManagementServer(boolean openposManagementServer) {
        this.openposManagementServer = openposManagementServer;
    }
    
    public String getDevicePattern() {
        return this.config.getDevicePattern();
    }
    
    public void setDevicePattern() {
    }
}
