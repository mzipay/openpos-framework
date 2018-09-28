package org.jumpmind.pos.test.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table(name="table")
public class TestTableModel extends Entity {

    @Column(primaryKey = true)
    int id;

    @Column
    String example;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setExample(String data) {
        this.example = data;
    }

    public String getExample() {
        return example;
    }

}
