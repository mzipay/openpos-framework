package org.jumpmind.jumppos.domain.business;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A Person or Organization who purchases, may purchase, or did purchase goods
 * and or services from the retail enterprise.
 */
@Entity
public class Customer  {
    
    @Id
    private String customerId;
    private String customerName;
    private String employeeId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerID) {
        this.customerId = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeID) {
        this.employeeId = employeeID;
    }

}
