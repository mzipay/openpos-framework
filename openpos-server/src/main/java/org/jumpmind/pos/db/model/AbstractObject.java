package org.jumpmind.pos.db.model;

import java.sql.JDBCType;
import java.util.Date;

import org.jumpmind.pos.db.Column;

public class AbstractObject {

    private static final long serialVersionUID = 1L;
    
    @Column(name="create_time", required=true, type=JDBCType.TIMESTAMP,
            description="Timestamp when this entry was created.")
    Date createTime = new Date();

    @Column(name="create_by", required=true, type=JDBCType.VARCHAR, size="50",
            description="The user who last updated this entry.")    
    String createBy;

    @Column(name="last_update_time", type=JDBCType.TIMESTAMP,
            description="Timestamp when a user last updated this entry.")
    Date lastUpdateTime = new Date();

    @Column(name="last_update_by", required=true, type=JDBCType.VARCHAR, size="50",
            description="The user who last updated this entry.")    
    String lastUpdateBy;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
