package org.jumpmind.pos.persist.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.jumpmind.db.model.Table;

public class EntityMetaData {

    private Table table;
    private List<Field> naturalKeyFields = new ArrayList<Field>();
    
    public Table getTable() {
        return table;
    }
    public void setTable(Table table) {
        this.table = table;
    }
    public List<Field> getNaturalKeyFields() {
        return naturalKeyFields;
    }
    public void setNaturalKeyFields(List<Field> naturalKeyFields) {
        this.naturalKeyFields = naturalKeyFields;
    }
    
}
