package org.jumpmind.jumppos.pos.state.model;

import java.util.ArrayList;
import java.util.List;

//@Component
//@TransactionScope
public class PosTransaction {

    private String userId;
    private List<PosLineItem> lines = new ArrayList<PosLineItem>();
    private List<TenderLineItem> tenderLines = new ArrayList<TenderLineItem>();
    private String grandTotal;

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

    public String getGrandTotal() {
        return grandTotal;
    }
    
    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

}
