package org.jumpmind.pos.core.screen;

import java.io.Serializable;
import java.util.List;

import org.jumpmind.pos.core.model.FormDisplayField;

public class TenderItem implements IItem, Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer index;
    
    private String type;
    private String number;
    private String amount;

    @Override
    public Integer getIndex() {
        return index;
    }

    @Override
    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String getID() {
        // Not implemented
        return "";
    }

    @Override
    public void setID(String id) {
        // Not implemented
    }

    @Override
    public String getDescription() {
        // Not implemented
        return "";
    }

    @Override
    public void setDescription(String description) {
        // Not implemented
    }

    @Override
    public String getSubtitle() {
        // Not implemented
        return "";
    }

    @Override
    public void setSubtitle(String subtitle) {
        // Not implemented
    }

    @Override
    public List<FormDisplayField> getFields() {
        // Not implemented
        return null;
    }

    @Override
    public void setFields(List<FormDisplayField> fields) {
        // Not implemented
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
    public boolean isSelected() {
        // Not implemented
        return false;
    }

    @Override
    public void setSelected(boolean selected) {
        // Not implemented
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}