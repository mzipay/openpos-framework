package org.jumpmind.pos.persist;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public abstract class Entity {
    
    private static final long serialVersionUID = 1L;
    
    @Column(primaryKey = true,
            size = "36",
            description = "A unique, automatically assigned key used to identify a row.")
    private String rowId;
    
    @Column(required=true,
            description="Timestamp when this entry was created.")
    private Date createTime = new Date();

    @Column(required=true, size="50",
            description="The user who last updated this entry.")    
    private String createBy;

    @Column(description="Timestamp when a user last updated this entry.")
    private Date lastUpdateTime = new Date();

    @Column(required=true, size="50",
            description="The user who last updated this entry.")    
    private String lastUpdateBy;
    
    @SuppressWarnings("unused")
    private transient Map<String, Object> systemData = new HashMap<>(); 

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

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }
    

}
