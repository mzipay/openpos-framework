package org.jumpmind.jumppos.core.screen;

import java.io.Serializable;
import java.util.List;

import org.jumpmind.jumppos.core.model.FormDisplayField;

public class DefaultItem implements IItem, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private Integer index;
    private String description;
    private String subtitle;
    private String amount;
    private List<FormDisplayField> fields;
    private boolean selected = false;

    public DefaultItem() {}
    
    public DefaultItem(String description, String subtitle, String amount) {
        this.description = description;
        this.subtitle = subtitle;
        this.amount = amount;
    }
    
    @Override
    public Integer getIndex() {
        return this.index;
    }

    @Override
    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getSubtitle() {
        return subtitle;
    }
    @Override
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    @Override
    public String getAmount() {
        return amount;
    }
    @Override
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public List<FormDisplayField> getFields() {
        return fields;
    }

    @Override
    public void setFields(List<FormDisplayField> fields) {
        this.fields = fields;
    }
    
    @Override
    public boolean isSelected() {
        return this.selected;
    }
    
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}

