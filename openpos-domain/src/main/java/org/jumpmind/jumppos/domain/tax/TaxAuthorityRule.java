package org.jumpmind.jumppos.domain.tax;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.business.BusinessUnitGroup;

@Entity
public class TaxAuthorityRule extends BaseEntity {
    
    @Id
    private String id;
    
    @OneToOne
    private BusinessUnitGroup businessUnitGroup;
    
    @OneToOne
    private TaxAuthority taxAuthority;
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    public void setBusinessUnitGroup(BusinessUnitGroup retailStoreGroup) {
        this.businessUnitGroup = retailStoreGroup;
    }
    
    public BusinessUnitGroup getBusinessUnitGroup() {
        return businessUnitGroup;
    }
    
    public void setTaxAuthority(TaxAuthority taxAuthority) {
        this.taxAuthority = taxAuthority;
    }
    
    public TaxAuthority getTaxAuthority() {
        return taxAuthority;
    }
    

}
