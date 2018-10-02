package org.jumpmind.pos.service.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Table;

@Table(name="module")
public class ModuleModel extends AbstractModel {

    @Column(primaryKey=true) 
    String installationId;
    
    @Column
    String currentVersion;
    
    public ModuleModel() {
    }
    
    public ModuleModel(String installationId, String currentVersion) {
        this.installationId = installationId;
        this.currentVersion = currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }
    
    public String getCurrentVersion() {
        return currentVersion;
    }
    
    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }
    
    public String getInstallationId() {
        return installationId;
    }
}
