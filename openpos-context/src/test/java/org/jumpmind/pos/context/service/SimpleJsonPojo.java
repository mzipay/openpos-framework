package org.jumpmind.pos.context.service;

import java.util.Date;

public class SimpleJsonPojo {

    private String id;
    private int seqeunce;
    private Date createDate;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getSeqeunce() {
        return seqeunce;
    }
    public void setSeqeunce(int seqeunce) {
        this.seqeunce = seqeunce;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
}
