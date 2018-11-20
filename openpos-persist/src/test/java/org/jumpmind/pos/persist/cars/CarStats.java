package org.jumpmind.pos.persist.cars;

import org.jumpmind.pos.persist.AbstractModel;

public class CarStats extends AbstractModel {
    
    private static final long serialVersionUID = 1L;
    private int count;
    private String model;
    
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    
    
}
