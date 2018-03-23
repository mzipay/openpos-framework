package org.jumpmind.pos.db.model;

import java.sql.JDBCType;
import java.util.Date;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Table;

@Table(
        name = "operator_security_question",
        description = "    <table name=\"operator_security_question\" description=\"A record of the Operator's answer to a security question that is put to the Operator when they have forgotten their Password and the system is trying to verify their identity and allow a new Password to be set.")
public class OperatorSecurityQuestion extends AbstractObject {

    @Column(
            name = "operator_id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "The corresponding Operator id.")
    private String operatorId;

    @Column(
            name = "security_question_id",
            primaryKey = true,
            required = true,
            type = JDBCType.VARCHAR,
            size = "16",
            description = "The corresponding SecurityQuestion id.")
    private String securityQuestionId;

    @Column(
            name = "answer",
            required = true,
            type = JDBCType.VARCHAR,
            size = "255",
            description = "A record of the Operator's answer to the indicated SecurityQuestion.")
    private String answer;

    @Column(name = "effective_time", type = JDBCType.TIMESTAMP, description = "The date and time at which this OperatorSecurityQuestion was chosen by the Operator.")
    Date effectiveTime;

    @Column(name = "expiration_time", type = JDBCType.TIMESTAMP, description = "The date and time at which this OperatorSecurityQuestion ceases to be valie because a new SecurityHint has been chosen.")
    Date expirationTime;

}
