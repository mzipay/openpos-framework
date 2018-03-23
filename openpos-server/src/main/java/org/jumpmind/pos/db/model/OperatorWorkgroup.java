package org.jumpmind.pos.db.model;

import java.sql.JDBCType;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Table;

@Table(
        name = "operator_workgroup",
        description = "A many to many table that associates operators with respective workgroups.")
public class OperatorWorkgroup extends AbstractObject {

    @Column(
            name = "operator_id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "The corresponding Operator id.")
    private String operatorId;


    @Column(
            name = "workgroup_id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "The corresponding Workgroup id.")
    private String workgroupeId;


}
