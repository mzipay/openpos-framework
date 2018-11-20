package org.jumpmind.pos.test.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Table;

@Table(name="table")
public class TestTableModel extends AbstractModel {

    private static final long serialVersionUID = 1L;

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
