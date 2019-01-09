package org.jumpmind.pos.persist.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.jumpmind.db.model.Table;

public class EntityMetaData {

    private Table table;
    private Class<?> clazz;
    private List<Field> entityIdFields = new ArrayList<Field>();
    private List<Field> entityFields = new ArrayList<Field>();
    
    public EntityMetaData() {
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Field> getEntityIdFields() {
        return entityIdFields;
    }

    public void setEntityIdFields(List<Field> entityIdFields) {
        this.entityIdFields = entityIdFields;
    }
    
    public void setEntityFields(List<Field> entityFields) {
        this.entityFields = entityFields;
    }
    
    public List<Field> getEntityFields() {
        return entityFields;
    }
    
    public Class<?> getClazz() {
        return clazz;
    }
    
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

}
