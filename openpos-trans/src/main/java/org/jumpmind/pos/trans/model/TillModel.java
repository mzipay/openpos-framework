package org.jumpmind.pos.trans.model;

import java.math.BigDecimal;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="till")
public class TillModel extends AbstractModel {

    private static final long serialVersionUID = 1L;
    
    @Column(primaryKey=true)
    String tillId;
    
    @Column(primaryKey=true)
    String businessUnitId;
    
    @Column
    TillStatusCode statusCode;
    
    @Column
    BigDecimal openingFloatAmount;

    public String getTillId() {
        return tillId;
    }

    public void setTillId(String tillId) {
        this.tillId = tillId;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public TillStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(TillStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public BigDecimal getOpeningFloatAmount() {
        return openingFloatAmount;
    }

    public void setOpeningFloatAmount(BigDecimal openingFloatAmount) {
        this.openingFloatAmount = openingFloatAmount;
    }

    
}
