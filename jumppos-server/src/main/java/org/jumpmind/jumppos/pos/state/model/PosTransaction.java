package org.jumpmind.jumppos.pos.state.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jumpmind.jumppos.core.flow.config.TransactionScope;
import org.springframework.stereotype.Component;

//@Component
//@TransactionScope
public class PosTransaction {
    
    private String userId;
    private List<PosLineItem> lines = new ArrayList<PosLineItem>();
    private List<TenderLineItem> tenderLines = new ArrayList<TenderLineItem>();
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public List<PosLineItem> getLines() {
        return lines;
    }
    public void setLines(List<PosLineItem> lines) {
        this.lines = lines;
    }
    public List<TenderLineItem> getTenderLines() {
        return tenderLines;
    }
    public void setTenderLines(List<TenderLineItem> tenderLines) {
        this.tenderLines = tenderLines;
    }
    
    public BigDecimal getGrandTotal() {
        BigDecimal grandTotal = new BigDecimal(0);
        for (PosLineItem line : lines) {
            grandTotal = grandTotal.add(line.getExtendedAmount());
        }
        return grandTotal;
    }
    
    
    

}
