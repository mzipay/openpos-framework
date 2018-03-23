package org.jumpmind.pos.db.model;

import java.sql.JDBCType;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Table;

@Table(
        name = "workgroup_resource_access",
        description = "A table that associates workgroups with resources and specifies their access level.")
public class WorkgroupResourceAccess extends AbstractObject {

    @Column(
            name = "workgroup_id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "The corresponding Workgroup id.")
    private String workgroupId;

    @Column(
            name = "resource_id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "The corresponding Resource id.")
    private String resourceId;
    
    @Column(
            name = "access_permissions",
            required = true,
            type = JDBCType.VARCHAR,
            size = "5",
            description = "The access permissions for the Workgroup to the Resource. Linux style permissions string with some additional attributes Read/Write/Delete/Admin/Execute (RWDAX).")
    private String accessPermissions;
    

}
