package org.jumpmind.jumppos.domain.party;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.business.BusinessUnit;

/**
 * An associative entity that links EMPLOYEE and RETAIL STORE. The assumption
 * here is that a RETAIL STORE has many EMPLOYEEs and EMPLOYEEs can operate in
 * many different RETAIL STOREs.
 */
@Entity
public class EmployeeStoreAssignment extends BaseEntity {

    @Id
    private int id;

    @OneToOne
    private Employee employee;

    @OneToOne
    private BusinessUnit store;

    private Date effectiveDate;
    private Date expirationDate;

    public EmployeeStoreAssignment() {
    }

    public EmployeeStoreAssignment(Date effective, BusinessUnit store, Employee emp) {
        this.effectiveDate = effective;
        this.store = store;
        this.employee = emp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BusinessUnit getStore() {
        return store;
    }

    public void setStore(BusinessUnit store) {
        this.store = store;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

}
