package org.jumpmind.jumppos.common.store;

import java.io.Serializable;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class WorkstationInfo implements Serializable, Comparable<WorkstationInfo> {

    private static final long serialVersionUID = 1L;

    String businessUnitId;

    String workstationId;

    String brandId;

    String typeId;

    public WorkstationInfo() {

    }

    public WorkstationInfo(String storeId, String workstationId, String brandId, String typeId) {
        this.businessUnitId = storeId;
        this.workstationId = workstationId;
        this.brandId = brandId;
        this.typeId = typeId;
    }
    
    public WorkstationInfo(String storeId, String workstationId) {
        this.businessUnitId = storeId;
        this.workstationId = workstationId;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String storeId) {
        this.businessUnitId = storeId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String workstationTypeId) {
        this.typeId = workstationTypeId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 41).append(businessUnitId).append(workstationId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        WorkstationInfo rhs = (WorkstationInfo) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(businessUnitId, rhs.businessUnitId)
                .append(workstationId, rhs.workstationId).isEquals();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(businessUnitId).append(" ").append(workstationId).append(": ");
        sb.append(brandId).append(" ").append(typeId);
        return sb.toString();
    }

    public int compareTo(WorkstationInfo myClass) {
        return new CompareToBuilder()
        // .appendSuper(super.compareTo(o)
                .append(this.businessUnitId, myClass.businessUnitId).append(this.workstationId,
                        myClass.workstationId).toComparison();
    }
}
