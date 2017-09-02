package org.jumpmind.jumppos.domain.party;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jumpmind.jumppos.domain.party.codes.EmployeeStatusCodes;

/**
 * An individual that works for a retail store, accepts direction from the
 * retail store management and satisfies the statutory criteria requiring that
 * payroll taxes and benefit contributions be paid by the retailer.
 */
@Entity
public class Employee extends Worker {

    int statusCode;

    String employeeId;

    Date effectiveDate;

    @OneToMany(targetEntity=EmployeeStoreAssignment.class, mappedBy="employee")
    Collection<EmployeeStoreAssignment> storeAssignments;

    public Employee() {
    }

    public Employee(String id) {
        this.employeeId = id;
    }

    public Employee(Date effectiveDate, int statusCode, String assignedEmployeeId) {
        this.setEffectiveDate(effectiveDate);
        this.setStatusCode(statusCode);
        this.setEmployeeId(assignedEmployeeId);
    }

    /**
     * @return This is the id the home office uses to track this employee.
     */
    public String getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(String id) {
        this.employeeId = id;
    }

    /**
     * @return the status of for this employee.
     * @see org.jumpmind.pos.partyrole.codes.EmployeeStatusCodes
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @see EmployeeStatusCodes
     */
    public void setStatusCode(int statusCode) {
        if (statusCode != EmployeeStatusCodes.ACTIVE && statusCode != EmployeeStatusCodes.INACTIVE) {
            throw new IllegalStateException(
                    "The status actionCode must be either ACTIVE or INACTIVE.");
        }
        this.statusCode = statusCode;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
