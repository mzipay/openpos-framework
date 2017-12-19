package org.jumpmind.pos.app.model;

import java.io.Serializable;

public class TransactionId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String storeId;

    private String workstationId;

    private String businessDate;

    private String brandId = "0";

    private String companyId = "1";

    private int sequenceNumber;

    public TransactionId() {
    }

    public TransactionId(String companyId, String brandId,  String storeId, String workstationId, String businessDate, int sequenceNumber) {
        this.storeId = storeId;
        this.workstationId = workstationId;
        this.businessDate = businessDate;
        this.brandId = brandId;
        this.companyId = companyId;
        this.sequenceNumber = sequenceNumber;
    }
    
    public TransactionId(String storeId, String workstationId, String businessDate, int sequenceNumber) {
        this.storeId = storeId;
        this.workstationId = workstationId;
        this.businessDate = businessDate;
        this.sequenceNumber = sequenceNumber;
    }    

    public String getStoreId() {
        return storeId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public long toLong() {
        return Long.parseLong(new StringBuilder(companyId).append(brandId).append(storeId).append(workstationId).append(businessDate)
                .append(String.format("%010d", sequenceNumber)).toString());
    }

}
