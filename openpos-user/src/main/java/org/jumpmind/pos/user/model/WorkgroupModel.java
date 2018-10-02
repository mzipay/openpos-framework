package org.jumpmind.pos.user.model;

import java.util.List;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Table;

@Table(name = "workgroup",
       description = "A group within a workforce that normally works together and has similar access control permissions.")
public class WorkgroupModel extends AbstractModel {
    
    @Column(primaryKey=true)
    private String workgroupId;

    @Column(size = "255",
            description = "A description for the Workgroup.")
    private String description;
    
    private List<PermissionModel> permissions;

    public String getWorkgroupId() {
    	return workgroupId;
    }
    
    public void setWorkgroupId(String workgroupId) {
    	this.workgroupId = workgroupId;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PermissionModel> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionModel> permissions) {
        this.permissions = permissions;
    }
}
