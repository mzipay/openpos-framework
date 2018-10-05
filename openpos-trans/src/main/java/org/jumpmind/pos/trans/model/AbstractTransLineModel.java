package org.jumpmind.pos.trans.model;

import org.jumpmind.pos.persist.Column;

public class AbstractTransLineModel extends AbstractTransModel {

    private static final long serialVersionUID = 1L;
    
    @Column(primaryKey = true)
    int lineSequenceNumber;
    
    boolean voided;
    
    LineEntryMethodCode entryMethodCode;    
    
    public void setLineSequenceNumber(int lineSequenceNumber) {
        this.lineSequenceNumber = lineSequenceNumber;
    }
    
    public int getLineSequenceNumber() {
        return lineSequenceNumber;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }

    public LineEntryMethodCode getEntryMethodCode() {
        return entryMethodCode;
    }

    public void setEntryMethodCode(LineEntryMethodCode entryMethodCode) {
        this.entryMethodCode = entryMethodCode;
    }

}
