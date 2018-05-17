package org.jumpmind.pos.tax.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;

public class BusinessUnitGroup extends Entity {

    @Column(primaryKey = true)
    private String id;

    @Column
    private String name;

    @Column
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
