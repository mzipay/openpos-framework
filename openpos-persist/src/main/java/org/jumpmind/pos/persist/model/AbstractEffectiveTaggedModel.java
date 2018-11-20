package org.jumpmind.pos.persist.model;

import java.util.Date;

import org.jumpmind.pos.persist.Column;

abstract public class AbstractEffectiveTaggedModel extends AbstractTaggedModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey=true)
    Date effectiveStartTime;
    
    @Column 
    Date effectiveEndTime;
    
    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }
    
    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }
    
    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }
    
    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }
}
