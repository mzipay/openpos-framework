package org.jumpmind.jumppos.domain.business;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.party.Address;

@Entity
@XmlRootElement
public class BusinessUnit extends BaseEntity {

    @Id
    private String id;
    private String name;
    private String lastBusinessDate;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    private BusinessUnitStatus status = BusinessUnitStatus.CLOSED;

    private String timezoneOffset;
    
    @ManyToMany(mappedBy="businessUnits")
    private Collection<BusinessUnitGroup> businessUnitGroups;

    public BusinessUnit() {
    }

    public BusinessUnit(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBusinessUnitGroups(Collection<BusinessUnitGroup> retailStoreGroups) {
        this.businessUnitGroups = retailStoreGroups;
    }
    
    @XmlTransient
    public Collection<BusinessUnitGroup> getBusinessUnitGroups() {
        return businessUnitGroups;
    }
    
    public void setStatus(BusinessUnitStatus status) {
        this.status = status;
    }

    public BusinessUnitStatus getStatus() {
        return status;
    }

    public void setLastBusinessDate(String lastOpenBusinessDate) {
        this.lastBusinessDate = lastOpenBusinessDate;
    }

    public String getLastBusinessDate() {
        return lastBusinessDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setTimezoneOffset(String timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public String getTimezoneOffset() {
        return timezoneOffset;
    }

}
