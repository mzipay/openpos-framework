package org.jumpmind.pos.tax.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table(description = "Tax Authority Rule")
public class TaxAuthorityRule extends Entity {

    @Column(primaryKey = true)
    private String id;

    private BusinessUnitGroup businessUnitGroup;

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
