package org.jumpmind.pos.management;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A response that looks like an OpenPOS server personalization response, but
 * includes an extra flag that indicates that the response is coming from an
 * OpenPOS Management Server.  The client can use this information to alter its
 * behavior for negotiating a connection. 
 */
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
