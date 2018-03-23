package org.jumpmind.pos.db.model;

import java.sql.JDBCType;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Table;

@Table(
        name = "resource",
        description = "An application, function, screen and/or field avaialbe for Workgroup access permissions.")
public class Resource extends AbstractObject {

    @Column(
            name = "id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "A unique, automatically assigned key used to identify a Resource.")
    private String id;

    @Column(
            name = "parent_resource_id",
            type = JDBCType.VARCHAR,
            size = "16",
            description = "A unique, automatically assigned key used to identify a Resource.")
    private String parentResourceId;

    @Column(
            name = "description",
            type = JDBCType.VARCHAR,
            size = "255",
            description = "The Resource id that is the parent of this Resource.  Used for hierarchical workgroups with casscading permissions.")
    private String description;
}
