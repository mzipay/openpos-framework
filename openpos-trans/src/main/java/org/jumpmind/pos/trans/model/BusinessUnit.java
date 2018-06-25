package org.jumpmind.pos.trans.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table
public class BusinessUnit extends Entity {

    @Column(primaryKey = true)
    private String id;
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
}
