package org.jumpmind.pos.user.model;

import java.util.Date;

public class PasswordHistory {
    
    private String id;
    private String operatorId;
    private String password;
    private Date expirationTime;
    private Date createTime;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOperatorId() {
        return operatorId;
    }
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Date getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
}
