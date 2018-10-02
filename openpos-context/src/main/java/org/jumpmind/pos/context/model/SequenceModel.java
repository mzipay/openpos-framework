package org.jumpmind.pos.context.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Table;

@Table(name="sequence")
public class SequenceModel extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey=true)
    String sequenceName;
    
    @Column
    long currentValue;
    
    @Column
    int incrementBy = 1;
    
    @Column
    long maxValue = 999999;
    
    @Column
    long minValue = 1;
    
    @Column
    boolean cycleFlag = true;
    
    public void setSequenceName(String seqName) {
        this.sequenceName = seqName;
    }
    
    public String getSequenceName() {
        return sequenceName;
    }
    
    public void setCurrentValue(long seqValue) {
        this.currentValue = seqValue;
    }
    
    public long getCurrentValue() {
        return currentValue;
    }
    
    public void setCycleFlag(boolean cycle) {
        this.cycleFlag = cycle;
    }
    
    public boolean isCycleFlag() {
        return cycleFlag;
    }
    
    public void setIncrementBy(int incremenetBy) {
        this.incrementBy = incremenetBy;
    }
    
    public int getIncrementBy() {
        return incrementBy;
    }
    
    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }
    
    public long getMaxValue() {
        return maxValue;
    }
    
    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }
    
    public long getMinValue() {
        return minValue;
    }
    
}
