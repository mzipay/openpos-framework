package org.jumpmind.pos.db.model;

import java.sql.JDBCType;
import java.util.Date;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Table;

@Table(
        name = "password_history",
        description = "A history of secret terms which are used to authenticate an Operator identification and thereby permits the Operator to access specific Resources and Workstation within the enterprise.")
public class PasswordHistory extends AbstractObject {

    @Column(
            name = "id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "A unique, automatically assigned key used to identify eacg successive password chosen by the Operator.")
    private String id;

    @Column(name = "operator_id", required = true, type = JDBCType.VARCHAR, size = "16", description = "The corresponding Operator id.")
    private String operatorId;

    @Column(
            name = "password",
            required = true,
            type = JDBCType.VARCHAR,
            size = "64",
            description = "The password which permits the Operator to access specific Resources and Workstation within the enterprise.")
    private String password;

    @Column(name = "expiration_time", type = JDBCType.TIMESTAMP, description = "The date and time afterwhich this password can no longer be used to sign on.")
    Date expirationTime;

}
