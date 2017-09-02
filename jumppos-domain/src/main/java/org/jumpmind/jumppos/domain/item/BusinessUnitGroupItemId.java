package org.jumpmind.jumppos.domain.item;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.business.BusinessUnitGroup;

@Embeddable
public class BusinessUnitGroupItemId implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne
    private Item item;

    @OneToOne
    private BusinessUnitGroup businessUnitGroup;

    public BusinessUnitGroupItemId() {
    }

    public BusinessUnitGroupItemId(Item item, BusinessUnitGroup group) {
        this.item = item;
        this.businessUnitGroup = group;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setBusinessUnitGroup(BusinessUnitGroup group) {
        this.businessUnitGroup = group;
    }

    public BusinessUnitGroup getBusinessUnitGroup() {
        return businessUnitGroup;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((businessUnitGroup == null) ? 0 : businessUnitGroup.getId().hashCode());
        result = prime * result + ((item == null) ? 0 : item.getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BusinessUnitGroupItemId other = (BusinessUnitGroupItemId) obj;
        if (businessUnitGroup == null) {
            if (other.businessUnitGroup != null)
                return false;
        } else if (!businessUnitGroup.getId().equals(other.businessUnitGroup.getId()))
            return false;
        if (item == null) {
            if (other.item != null)
                return false;
        } else if (!item.getId().equals(other.item.getId()))
            return false;
        return true;
    }

}
