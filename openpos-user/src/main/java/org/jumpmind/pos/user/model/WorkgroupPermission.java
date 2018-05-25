package org.jumpmind.pos.user.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table
public class WorkgroupPermission extends Entity {

    @Column(primaryKey=true)
    private String workgroupId;
    
    @Column(primaryKey=true)
    private String permissionId;

    public String getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(String workgroupRowId) {
        this.workgroupId = workgroupRowId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionRowId) {
        this.permissionId = permissionRowId;
    }
    
}
