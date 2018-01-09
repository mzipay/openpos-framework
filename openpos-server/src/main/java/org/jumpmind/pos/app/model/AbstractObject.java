package org.jumpmind.pos.app.model;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractObject implements Serializable {

    private static final long serialVersionUID = 1L;
    
    Date createTime;
    Date lastUpdateTime;
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }
    
}
