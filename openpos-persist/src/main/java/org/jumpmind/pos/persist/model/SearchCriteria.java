package org.jumpmind.pos.persist.model;

import java.util.HashMap;
import java.util.Map;

import org.jumpmind.pos.persist.AbstractModel;

public class SearchCriteria {

    Map<String, Object> criteria;

    Class<? extends AbstractModel> entityClass;
    
    boolean useAnd = true;

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
        if (this.criteria == null) {
            this.criteria = new HashMap<String, Object>();
        }

        if (field.equals("phone") && value instanceof String) {
            String[] parsed = parsePhone((String) value);
            if (parsed != null) {
                this.criteria.put("areaCode", parsed[0]);
                this.criteria.put("phoneNumber", parsed[1]);
            }
        } else {
            this.criteria.put(field, value);
        }
    }

    public static String[] parsePhone(String phoneNumber) {
        String[] parsed = new String[2];
        if (phoneNumber != null) {
            phoneNumber.replaceAll("\\(", "");
            phoneNumber.replaceAll("\\)", "");

            String[] phoneParts = phoneNumber.split("-");
            if (phoneNumber.length() == 10) {
                parsed[0] = phoneNumber.substring(0, 3);
                parsed[1] = phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
            } else if (phoneParts.length > 2) {
                parsed[0] = phoneParts[0];
                parsed[1] = phoneParts[1] + " - " + phoneParts[2];
            } else if (phoneParts.length > 1) {
                parsed[1] = phoneParts[0] + " - " + phoneParts[1];
            }
            return parsed;
        }
        return null;
    }

    public Class<? extends AbstractModel> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<? extends AbstractModel> entityClass) {
        this.entityClass = entityClass;
    }
    
    public void setUseAnd(boolean and) {
        this.useAnd = and;
    }
    
    public boolean isUseAnd() {
        return useAnd;
    }

}