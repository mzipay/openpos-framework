package org.jumpmind.pos.persist.model;

import java.util.HashMap;
import java.util.Map;

import org.jumpmind.pos.persist.AbstractModel;

public class SearchCriteria {
    
    Map<String, Object> criteria;
    
    Class<? extends AbstractModel> entityClass;
    
    public SearchCriteria() {
        
    }
    
    public SearchCriteria(Class<? extends AbstractModel> entityClass) {
        this.entityClass = entityClass;
    }
    
    public SearchCriteria(Map<String, Object> criteria, Class<? extends AbstractModel> entityClass) {
        this.criteria = criteria;
        this.entityClass = entityClass;
    }
    
    public Map<String, Object> getCriteria() {
        return criteria;
    }
    
    public void setCriteria(Map<String, Object> criteria) {
        this.criteria = criteria;
    }
    
    public void addCriteria(String field, Object value) {
        if(this.criteria == null) {
            this.criteria = new HashMap<String, Object>();
        }
        this.criteria.put(field,  value);
    }
    
    public Class<? extends AbstractModel> getEntityClass() {
        return entityClass;
    }
    
    public void setEntityClass(Class<? extends AbstractModel> entityClass) {
        this.entityClass = entityClass;
    }
    
}