package org.jumpmind.pos.tax.model;

import java.util.Date;

public class RetailTransactionLineItem {

    private String id;

    private Integer voidsLine;

    private Date beginDateTimestamp;

    private boolean voidFlag;

    private Date endDateTimestamp;

    private String typeCode;

    private RetailTransaction retailTransaction;

    public void setRetailTransaction(RetailTransaction retailTransaction) {
        this.retailTransaction = retailTransaction;
    }

    public RetailTransaction getRetailTransaction() {
        return retailTransaction;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Date getBeginDateTimestamp() {
        return beginDateTimestamp;
    }

    public void setBeginDateTimestamp(Date beginDateTimestamp) {
        this.beginDateTimestamp = beginDateTimestamp;
    }

    public Date getEndDateTimestamp() {
        return endDateTimestamp;
    }

    public void setEndDateTimestamp(Date endDateTimestamp) {
        this.endDateTimestamp = endDateTimestamp;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public boolean isVoidFlag() {
        return voidFlag;
    }

    public void setVoidFlag(boolean voidFlag) {
        this.voidFlag = voidFlag;
    }

    public Integer getVoidsLine() {
        return voidsLine;
    }

    public void setVoidsLine(Integer voidsLine) {
        this.voidsLine = voidsLine;
    }

}
