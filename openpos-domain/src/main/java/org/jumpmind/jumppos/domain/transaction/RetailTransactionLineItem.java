package org.jumpmind.jumppos.domain.transaction;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class RetailTransactionLineItem extends BaseEntity {
    
    @Id
    private String id;

    private Integer voidsLine;

    private Date beginDateTimestamp;

    private boolean voidFlag;

    private Date endDateTimestamp;

    private String typeCode;
    
    @OneToOne
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
