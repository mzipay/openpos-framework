package org.jumpmind.pos.db.model;

import java.sql.JDBCType;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Table;

@Table(
        name = "security_question",
        description = "A question that is put ot the Customer or Operator when they have forgotten their Password and the system is trying to verify their identity.")
public class SecurityQuestion extends AbstractObject {

    @Column(
            name = "id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "A unique, automatically assigned key used to identify a security question.")
    private String id;

    @Column(
            name = "question",
            required = true,
            type = JDBCType.VARCHAR,
            size = "255",
            description = "A question that is put to a Customer or Operator when they have forgotten their Password and the system is trying to verify their identity and allow a new Password to be set.")
    private String question;

}
