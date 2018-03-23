package org.jumpmind.pos.db.model;

import java.sql.JDBCType;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Table;

@Table(
        name = "workgroup",
        description = "A group within a workforce that normally works together and has similar access control permissions.")
public class Workgroup extends AbstractObject {

    @Column(
            name = "id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "A unique, automatically assigned key used to identify a Workgroup.")
    private String id;

    @Column(
            name = "parent_workgroup_id",
            type = JDBCType.VARCHAR,
            size = "16",
            description = "A description for the Workgroup.")
    private String parentWorkgroupId;

    @Column(
            name = "description",
            type = JDBCType.VARCHAR,
            size = "255",
            description = "A description for the Workgroup.")
    private String description;
    
}
